package Manager;

import Tasks.*;

import java.util.ArrayList;

public interface TaskManager {
    void createTask(Task task);
    void createEpic(Epic epic);
    void createSubTusk (SubTask subTask, int epicId);
    ArrayList<Task> getAllTasks();
    ArrayList<Epic> getAllEpics();
    ArrayList<SubTask> getAllSubTusks();
    public Task getTaskById(int tuskId);
    public Epic getEpicById(int epicId);
    ArrayList<SubTask> getSubTask(int id);
    public void updateTask(Task task);
    public void updateEpic(Epic epic);
    public void updateSubTusk(SubTask subTask);
    public void deleteAllTasks();
    public void deleteAllEpics();
    public void deleteAllSubTusks();
    public void deleteTaskById(int tId);
    public void deleteEpicById(int eId);
    public void deleteSubTaskById(Integer sId);
    ArrayList<SubTask> getAllSubTasksInEpic(Epic epic);
}
