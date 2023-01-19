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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;


public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {

    public static void main(String[] args) {
        File file1 = new File("src/manager/file/testReport.csv");
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file1);

        fileBackedTasksManager.createTask(new Task("Ноутбук", TaskTypeEnum.TASK,
                "Купить новый ноутбук", StatusTypeEnum.NEW));
        fileBackedTasksManager.createEpic(new Epic("Продукты", TaskTypeEnum.EPIC,
                "Купить продукты", StatusTypeEnum.NEW));
        fileBackedTasksManager.createSubTask(new SubTask("Пойти в магазин", TaskTypeEnum.SUB_TASK,
                "Купить все в магазине", StatusTypeEnum.NEW, 2));

        fileBackedTasksManager.getTaskById(1);
        fileBackedTasksManager.getEpicById(2);
        fileBackedTasksManager.getSubTaskById(3);


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
        String subtaskEpicId;

        String[] line = result.split(",");
        id = Integer.parseInt(line[0]);
        taskType = TaskTypeEnum.valueOf(line[1]);
        name = line[2];
        status = StatusTypeEnum.valueOf(line[3]);
        description = line[4];

        switch (taskType){
            case EPIC:
                Epic epic = new Epic(name, taskType, description, status);
                epic.setId(id);
                return epic;
            case TASK:
                Task task = new Task(name, taskType, description, status);
                task.setId(id);
                return task;
            case SUB_TASK:
                SubTask subTask = new SubTask(name, taskType, description, status, Integer.parseInt(line[5]));
                subTask.setId(id);
                return subTask;
        }
        return null;
    }

    public static String historyToString(HistoryManager historyManager){
        if (historyManager.getHistory() != null){
            StringBuilder line = new StringBuilder();
            for (Task task : historyManager.getHistory()){
                line.append(task.getId()).append(",");
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

//     @Override
//    public HistoryManager getHistory() {
//        HistoryManager historyManager = super.getHistory();
//        save();
//        return historyManager;
//    }
}
