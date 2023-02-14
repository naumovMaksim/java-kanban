package manager.file;

import exception.ManagerSaveException;
import manager.inMemory.InMemoryTaskManager;
import manager.interfaces.TaskManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.enums.StatusTypeEnum;
import tasks.enums.Type;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;


public class FileBackedTasksManager extends InMemoryTaskManager {
    public static void main(String[] args) {
        TaskManager fileBackedTasksManager = new FileBackedTasksManager();

        fileBackedTasksManager.createTask(new Task("Ноутбук", Type.TASK,
                "Купить новый ноутбук", StatusTypeEnum.NEW,
                LocalDateTime.of(2023, 2, 3, 10, 0), 5));
        fileBackedTasksManager.createTask(new Task("Кот", Type.TASK,
                "Покормить кота", StatusTypeEnum.DONE,
                LocalDateTime.of(2023, 2, 3, 11, 0), 5));
        fileBackedTasksManager.createEpic(new Epic("Продукты", Type.EPIC,
                "Купить продукты", StatusTypeEnum.NEW,
                LocalDateTime.of(2023, 2, 3, 12, 0), 5));
        fileBackedTasksManager.createSubTask(new SubTask("Пойти в магазин", Type.SUB_TASK,
                "Купить все в магазине", StatusTypeEnum.NEW, 3,
                LocalDateTime.of(2023, 2, 3, 13, 0), 5));

        fileBackedTasksManager.getTaskById(1);
        fileBackedTasksManager.getTaskById(2);
        fileBackedTasksManager.getSubTaskById(4);

        try {
            System.out.println("Проверка записи в файл: " + "\n" + Files.readString(Path.of("resources/testReport.csv")) + "\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        File fileLoadTest = new File("resources/testReport.csv");
        FileBackedTasksManager fileBackedTasksManager2 = loadFromFile(fileLoadTest);

        System.out.println("Проверка считывания из файла:" + "\n" + "История: " + fileBackedTasksManager2.getHistory() + "\n");
        System.out.println(fileBackedTasksManager2.getAllTasks() + "\n" + fileBackedTasksManager2.getAllEpics() + "\n" +
                fileBackedTasksManager2.getAllSubTasks());

//        TaskManager fileBackedTasksManager = Managers.getDefault();
//
//        int taskId = fileBackedTasksManager.createTask(new Task("Ноутбук", type.TASK,
//                "Купить новый ноутбук", StatusTypeEnum.NEW, Instant.ofEpochSecond(1), 5));
//        int epicId = fileBackedTasksManager.createEpic(new Epic("Продукты", type.EPIC,
//                "Купить продукты", StatusTypeEnum.NEW, Instant.ofEpochSecond(10), 5));
//        int subTaskId = fileBackedTasksManager.createSubTask(new SubTask("Пойти в магазин", type.SUB_TASK,
//                "Купить все в магазине", StatusTypeEnum.NEW, 2, Instant.ofEpochSecond(20), 5));
//
//        fileBackedTasksManager.getTaskById(1);
//        fileBackedTasksManager.getEpicById(2);
//        fileBackedTasksManager.getSubTaskById(3);
//        fileBackedTasksManager.deleteTaskById(taskId);
//
//        System.out.println("Проверка записи в файл: " + "\n" + Files.readString(Path.of("resources/testReport.csv")) + "\n");
//
//        File fileLoadTest = new File("resources/testReport.csv");
//        FileBackedTasksManager fileBackedTasksManager2 = loadFromFile(fileLoadTest);
//        System.out.println("Сохраненное состояние");
//        System.out.println(fileBackedTasksManager.getHistory() + "\n");
//        System.out.println(fileBackedTasksManager.getAllTasks() + "\n" + fileBackedTasksManager.getAllEpics() + "\n" +
//                fileBackedTasksManager.getAllSubTasks());
//        System.out.println("Загруженное состояние");
//        System.out.println(fileBackedTasksManager2.getHistory() + "\n");
//        System.out.println(fileBackedTasksManager2.getAllTasks() + "\n" + fileBackedTasksManager2.getAllEpics() + "\n" +
//                fileBackedTasksManager2.getAllSubTasks());
    }

    private final File file = new File("resources/testReport.csv");

    public void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            Collection<Task> tasks = super.getAllTasks();
            Collection<Epic> epics = super.getAllEpics();
            Collection<SubTask> subTasks = super.getAllSubTasks();

            writer.write(CSVTaskFormat.getHeader());
            writer.newLine();
            for (Task task : tasks) {
                writer.write(CSVTaskFormat.toString(task));
            }
            for (Epic epic : epics) {
                writer.write(CSVTaskFormat.toString(epic));
            }
            for (SubTask subTask : subTasks) {
                writer.write(CSVTaskFormat.toString(subTask));
            }
            writer.newLine();
            writer.write(Objects.requireNonNull(CSVTaskFormat.historyToString(getHistoryManager())));
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        final FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager();
        try {
            final String csv = Files.readString(file.toPath());
            final String[] lines = csv.split(System.lineSeparator());
            int generatorId = 0;
            List<Integer> history = Collections.emptyList();
            for (int i = 1; i < lines.length; i++) {
                String line = lines[i];
                if (i == 2) {
                    history = CSVTaskFormat.historyFromString(lines[i]);
                    break;
                }
                String[] taskLine = line.split("\n");
                for (String l : taskLine) {
                    final Task task = CSVTaskFormat.fromString(l);
                    if (task != null) {
                        final int id = task.getId();
                        if (id > generatorId) {
                            generatorId = id;
                        }
                        fileBackedTasksManager.addAnyTask(task);
                    }
                }
            }
            for (Map.Entry<Integer, SubTask> e : fileBackedTasksManager.subTasksMemory.entrySet()) {
                final SubTask subtask = e.getValue();
                final Epic epic = fileBackedTasksManager.epicsMemory.get(subtask.getEpicId());
                epic.addSubTaskId(subtask.getId());
            }
            for (Integer taskId : history) {
                fileBackedTasksManager.getHistoryManager().add(fileBackedTasksManager.findTask(taskId));
            }
            fileBackedTasksManager.id = generatorId;
        } catch (IOException e) {
            throw new ManagerSaveException("Can't read form file: " + file.getName(), e);
        }
        return fileBackedTasksManager;
    }

    @Override
    public int createTask(Task task) {
        if (task != null) {
            super.createTask(task);
            save();
            return task.getId();
        } else {
            return -1;
        }
    }

    @Override
    public int createEpic(Epic epic) {
        if (epic != null) {
            super.createEpic(epic);
            save();
            return epic.getId();
        } else {
            return -1;
        }
    }

    @Override
    public int createSubTask(SubTask subTask) {
        if (subTask != null && subTask.getEpicId() > 0) {
            super.createSubTask(subTask);
            save();
            return subTask.getId();
        } else {
            return -1;
        }
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubTasks() {
        super.deleteAllSubTasks();
        save();
    }

    @Override
    public void deleteTaskById(int taskId) {
        super.deleteTaskById(taskId);
        save();
    }

    @Override
    public void deleteEpicById(int epicId) {
        super.deleteEpicById(epicId);
        save();
    }

    @Override
    public void deleteSubTaskById(Integer subTaskId) {
        super.deleteSubTaskById(subTaskId);
        save();
    }

    @Override
    public Task getTaskById(int taskId) {
        Task task = super.getTaskById(taskId);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(int epicId) {
        Epic epic = super.getEpicById(epicId);
        save();
        return epic;
    }

    @Override
    public SubTask getSubTaskById(int subTaskId) {
        SubTask subTask = super.getSubTaskById(subTaskId);
        save();
        return subTask;
    }

    @Override
    public List<Task> getHistory() {
        List<Task> historyManager = super.getHistory();
        save();
        return historyManager;
    }

    @Override
    protected void addAnyTask(Task task) {
        super.addAnyTask(task);
    }

    @Override
    protected Task findTask(Integer id) {
        return super.findTask(id);
    }
}
