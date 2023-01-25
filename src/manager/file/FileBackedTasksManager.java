package manager.file;

import exception.ManagerSaveException;
import manager.Managers;
import manager.inMemory.InMemoryTaskManager;
import manager.interfaces.TaskManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.enums.StatusTypeEnum;
import tasks.enums.type;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;


public class FileBackedTasksManager extends InMemoryTaskManager {

    public static void main(String[] args) throws IOException {
        TaskManager fileBackedTasksManager = Managers.getDefault();

        fileBackedTasksManager.createTask(new Task("Ноутбук", type.TASK,
                "Купить новый ноутбук", StatusTypeEnum.NEW));
        fileBackedTasksManager.createEpic(new Epic("Продукты", type.EPIC,
                "Купить продукты", StatusTypeEnum.NEW));
        fileBackedTasksManager.createSubTask(new SubTask("Пойти в магазин", type.SUB_TASK,
                "Купить все в магазине", StatusTypeEnum.NEW, 2));

        fileBackedTasksManager.getTaskById(1);
        fileBackedTasksManager.getEpicById(2);
        fileBackedTasksManager.getSubTaskById(3);

        System.out.println("Проверка записи в файл: " + "\n" + Files.readString(Path.of("resources/testReport.csv")) + "\n");

        File fileLoadTest = new File("resources/testLoadReport.csv");
        FileBackedTasksManager fileBackedTasksManager2 = loadFromFile(fileLoadTest);

        System.out.println("Проверка считывания из файла:" + "\n" + "История: " + fileBackedTasksManager2.getHistory() + "\n");
        System.out.println(fileBackedTasksManager2.getAllTasks() + "\n" + fileBackedTasksManager2.getAllEpics() + "\n" +
                fileBackedTasksManager2.getAllSubTasks());
    }

    private final File file;

    public FileBackedTasksManager(File file) {
        super();
        this.file = file;
    }

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

    public static FileBackedTasksManager loadFromFile(File file) {// ваша реализация этого метода показалась сложноватой, если можно, могу отставть свою реализацию?
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        try (BufferedReader br = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            while (br.ready()) {
                String line = br.readLine();
                if (line.equals("")) {
                    String lines = br.readLine();
                    for (int id : Objects.requireNonNull(CSVTaskFormat.historyFromString(lines))) {
                        if (fileBackedTasksManager.getTaskById(id) != null) {
                            fileBackedTasksManager.getTaskById(id);
                        } else if (fileBackedTasksManager.getEpicById(id) != null) {
                            fileBackedTasksManager.getEpicById(id);
                        } else if (fileBackedTasksManager.getSubTaskById(id) != null) {
                            fileBackedTasksManager.getSubTaskById(id);
                        }
                    }
                } else if (!line.equals(CSVTaskFormat.getHeader())) {
                    Task task = CSVTaskFormat.fromString(line);
                    if (task == null) {
                        break;
                    }
                    type Tasktype = task.getType();
                    switch (Tasktype) {
                        case TASK:
                            fileBackedTasksManager.createTask(task);
                            break;
                        case EPIC:
                            fileBackedTasksManager.createEpic((Epic) task);
                            break;
                        case SUB_TASK:
                            fileBackedTasksManager.createSubTask((SubTask) task);
                            break;
                    }
                }
            }
            return fileBackedTasksManager;
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void createSubTask(SubTask subTask) {
        super.createSubTask(subTask);
        save();
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
}
