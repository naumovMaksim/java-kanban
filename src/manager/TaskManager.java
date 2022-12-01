package manager;

import tasks.*;

import java.util.ArrayList;

public interface TaskManager {
    void createTask(Task task);
    void createEpic(Epic epic);
    void createSubTusk (SubTask subTask, int epicId);
    ArrayList<Task> getAllTasks();
    ArrayList<Epic> getAllEpics();
    ArrayList<SubTask> getAllSubTusks();
    Task getTaskById(int tuskId);
    Epic getEpicById(int epicId);
    ArrayList<SubTask> getSubTask(int id);
    void updateTask(Task task);
    void updateEpic(Epic epic);
    void updateSubTusk(SubTask subTask);
    void deleteAllTasks();
    void deleteAllEpics();
    void deleteAllSubTusks();
    void deleteTaskById(int tId);
    void deleteEpicById(int eId);
    void deleteSubTaskById(Integer sId);
    ArrayList<SubTask> getAllSubTasksInEpic(Epic epic);
}
