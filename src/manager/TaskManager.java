package manager;

import tasks.*;

import java.util.List;

public interface TaskManager {
    void createTask(Task task);
    void createEpic(Epic epic);
    void createSubTask(SubTask subTask);
    List<Task> getAllTasks();
    List<Epic> getAllEpics();
    List<SubTask> getAllSubTasks();
    Task getTaskById(int taskId);
    Epic getEpicById(int epicId);
    SubTask getSubTaskById(int subTaskId);
    void updateTask(Task task);
    void updateEpic(Epic epic);
    void updateSubTask(SubTask subTask);
    void deleteAllTasks();
    void deleteAllEpics();
    void deleteAllSubTasks();
    void deleteTaskById(int taskId);
    void deleteEpicById(int epicId);
    void deleteSubTaskById(Integer subTaskId);
    List<SubTask> getAllSubTasksInEpic(Epic epic);
    List<Task> getHistory();
}
