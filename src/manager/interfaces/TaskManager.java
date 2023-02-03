package manager.interfaces;

import exception.ValidateException;
import tasks.*;

import java.util.List;

public interface TaskManager {
    int createTask(Task task) throws ValidateException;

    int createEpic(Epic epic);

    int createSubTask(SubTask subTask) throws ValidateException;

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

    List<Task> getPrioritizedTasks();
}
