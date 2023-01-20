package manager.file;

import exception.ManagerSaveException;
import manager.interfaces.HistoryManager;
import manager.interfaces.TaskManager;
import manager.memory.InMemoryTaskManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.enums.StatusTypeEnum;
import tasks.enums.TaskTypeEnum;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;


public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {

    public static void main(String[] args) throws IOException {
        File fileUpload = new File("src/manager/file/testReport.csv");
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(fileUpload);

        fileBackedTasksManager.createTask(new Task("Ноутбук", TaskTypeEnum.TASK,
                "Купить новый ноутбук", StatusTypeEnum.NEW));
        fileBackedTasksManager.createEpic(new Epic("Продукты", TaskTypeEnum.EPIC,
                "Купить продукты", StatusTypeEnum.NEW));
        fileBackedTasksManager.createSubTask(new SubTask("Пойти в магазин", TaskTypeEnum.SUB_TASK,
                "Купить все в магазине", StatusTypeEnum.NEW, 2));

        fileBackedTasksManager.getTaskById(1);
        fileBackedTasksManager.getEpicById(2);
        fileBackedTasksManager.getSubTaskById(3);

        System.out.println("Проверка записи в файл: " + "\n" + Files.readString(Path.of("src/manager/file/testReport.csv")) + "\n");

        File fileLoadTest = new File("src/manager/file/testLoadReport.csv");
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

    public void save(){
        try(FileWriter fileWriter = new FileWriter(file)) {
            Collection<Task> tasks = super.getAllTasks();
            Collection<Epic> epics = super.getAllEpics();
            Collection<SubTask> subTasks = super.getAllSubTasks();

            fileWriter.write("id,type,name,status,description,epic\n");
            for (Task task : tasks) {
                fileWriter.write(toString(task));
            }
            for (Epic epic : epics) {
                fileWriter.write(toString(epic));
            }
            for (SubTask subTask : subTasks) {
                fileWriter.write(toString(subTask));
            }
            fileWriter.write("\n");
            fileWriter.write(Objects.requireNonNull(historyToString(getHistoryManager())));
        } catch (IOException e){
            throw new ManagerSaveException(e.getMessage());
        }
    }

    public String toString(Task task){
        String subTaskEpicId = " ";

        if(task.getTaskTypeEnum().equals(TaskTypeEnum.SUB_TASK)){
            subTaskEpicId = String.valueOf(((SubTask) task).getEpicId());
        }
        return String.format("%s,%s,%s,%s,%s,%s\n",
                task.getId(),
                task.getTaskTypeEnum(),
                task.getName(),
                task.getAllTasksStatus(),
                task.getDescription(),
                subTaskEpicId);
    }

    public Task fromString(String result){
        int id;
        TaskTypeEnum taskType;
        String name;
        StatusTypeEnum status;
        String description;

        String[] line = result.split(",");
        if (line[1].equals("TASK") || line[1].equals("EPIC") || line[1].equals("SUB_TASK")) {
            id = Integer.parseInt(line[0]);
            taskType = TaskTypeEnum.valueOf(line[1]);
            name = line[2];
            status = StatusTypeEnum.valueOf(line[3]);
            description = line[4];

            switch (taskType) {
                case EPIC:
                    Epic epic = new Epic(name, taskType, description, status);
                    epic.setId(id);
                    createEpic(epic);
                    return epic;
                case TASK:
                    Task task = new Task(name, taskType, description, status);
                    task.setId(id);
                    createTask(task);
                    return task;
                case SUB_TASK:
                    SubTask subTask = new SubTask(name, taskType, description, status, Integer.parseInt(line[5]));
                    subTask.setId(id);
                    createSubTask(subTask);
                    return subTask;
                }
            }
        return null;
    }

    public static String historyToString(HistoryManager historyManager){
        if (historyManager.getHistory() != null){
            StringBuilder line = new StringBuilder();
            for (Task task : historyManager.getHistory()){
                line.append(task.getId()).append(",");
            }
            if (line.length() != 0){
                line.deleteCharAt(line.length() - 1);
            }
            return line.toString();
        }
        return null;
    }

    public static List<Integer> historyFromString(String value){
        List<Integer> history = new ArrayList<>();
        if (!value.isEmpty()){
            String[] line = value.split(",");
            for (String s : line){
                history.add(Integer.parseInt(s));
            }
        return history;
        }
        return null;
    }

    public static FileBackedTasksManager loadFromFile(File file){
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        try (BufferedReader br = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))){
            while (br.ready()){
                String line = br.readLine();
                if (line.equals("")) {
                    String lines = br.readLine();
                    for (int id : Objects.requireNonNull(historyFromString(lines))) {
                        if (fileBackedTasksManager.getTaskById(id) != null) {
                            fileBackedTasksManager.getTaskById(id);
                        } else if (fileBackedTasksManager.getEpicById(id) != null) {
                            fileBackedTasksManager.getEpicById(id);
                        } else if (fileBackedTasksManager.getSubTaskById(id) != null) {
                            fileBackedTasksManager.getSubTaskById(id);
                        }
                    }
                }
                if(!line.equals("")) {
                    fileBackedTasksManager.fromString(line);
                }
            }
            return fileBackedTasksManager;
        } catch (IOException e){
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
    public List<Task> getAllTasks() {
        List<Task> taskList = super.getAllTasks();
        save();
        return taskList;
    }

    @Override
    public List<Epic> getAllEpics() {
        List<Epic> epicList = super.getAllEpics();
        save();
        return epicList;
    }

    @Override
    public List<SubTask> getAllSubTasks() {
        List<SubTask> subTaskList = super.getAllSubTasks();
        save();
        return subTaskList;
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
    public List<SubTask> getAllSubTasksInEpic(Epic epic) {
        List<SubTask> subTaskList = super.getAllSubTasksInEpic(epic);
        save();
        return subTaskList;
    }

    @Override
    public List<Task> getHistory() {
        List<Task> historyManager = super.getHistory();
        save();
        return historyManager;
    }
}
