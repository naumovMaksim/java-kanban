import exception.ValidateException;
import manager.inMemory.InMemoryHistoryManager;
import manager.interfaces.HistoryManager;
import manager.interfaces.TaskManager;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.enums.StatusTypeEnum;
import tasks.enums.Type;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected T manager;
    protected Task task;
    protected Epic epic;
    protected SubTask subTask;
    HistoryManager historyManager;

    protected void tasksCreation() throws ValidateException {
        task = new Task("Task", Type.TASK, "description", StatusTypeEnum.NEW,
                LocalDateTime.of(2023,2,3,6, 0), 1);
        epic = new Epic("Epic", Type.EPIC, "description", StatusTypeEnum.NEW,
                LocalDateTime.of(2023,2,3,7, 0), 1);
        subTask = new SubTask("SubTask", Type.SUB_TASK, "description", StatusTypeEnum.NEW, 2,
                LocalDateTime.of(2023,2,3,8, 0), 1);

        manager.createTask(task);
        manager.createEpic(epic);
        manager.createSubTask(subTask);
        historyManager = new InMemoryHistoryManager();
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subTask);
    }

    @Test
    public void taskCreate() {
        assertNotNull(task);
        assertEquals(StatusTypeEnum.NEW, task.getAllTasksStatus());
        assertTrue(task.getId() > 0);
        assertNotNull(task.getName());
        assertNotNull(task.getDescription());
    }

    @Test
    public void epicCreate() {
        assertNotNull(epic);
        assertEquals(StatusTypeEnum.NEW, epic.getAllTasksStatus());
        assertTrue(epic.getId() > 0);
        assertNotNull(epic.getName());
        assertNotNull(epic.getDescription());
    }

    @Test
    public void subTaskCreate() {
        assertNotNull(subTask);
        assertEquals(StatusTypeEnum.NEW, subTask.getAllTasksStatus());
        assertTrue(subTask.getId() > 0);
        assertNotNull(subTask.getName());
        assertNotNull(subTask.getDescription());
    }

    @Test
    public void wrongTaskCreate() throws ValidateException {
        assertEquals(-1, manager.createTask(null));
    }

    @Test
    public void wrongEpicCreate() {
        assertEquals(-1, manager.createEpic(null));
    }

    @Test
    public void wrongSubTaskCreate() throws ValidateException {
        assertEquals(-1, manager.createSubTask(null));
    }

    @Test
    public void wrongEpicSubTaskCreate() throws ValidateException {
        assertEquals(-1, manager.createSubTask(new SubTask("SubTask", Type.SUB_TASK,
                "description", StatusTypeEnum.NEW, 0,
                LocalDateTime.of(2023,2,3,7, 0), 1)));
    }

    @Test
    public void getTasksNotEmpty() {
        List<Task> taskList = new ArrayList<>(manager.getAllTasks());

        assertEquals(task, taskList.get(0));
    }

    @Test
    public void getTasksEmpty() {
        manager.deleteAllTasks();

        assertTrue(manager.getAllTasks().isEmpty());
    }

    @Test
    public void getEpicsNotEmpty() {
        List<Task> epicList = new ArrayList<>(manager.getAllEpics());

        assertEquals(epic, epicList.get(0));
    }

    @Test
    public void getEpicsEmpty() {
        manager.deleteAllEpics();

        assertTrue(manager.getAllEpics().isEmpty());
    }

    @Test
    public void getSubTasksNotEmpty() {
        List<Task> subTaskList = new ArrayList<>(manager.getAllSubTasks());

        assertEquals(subTask, subTaskList.get(0));
    }

    @Test
    public void getSubTasksEmpty() {
        manager.deleteAllSubTasks();

        assertTrue(manager.getAllSubTasks().isEmpty());
    }

    @Test
    public void getTaskById() {
        assertEquals(task, manager.getTaskById(task.getId()));
    }

    @Test
    public void getEpicById() {
        assertEquals(epic, manager.getEpicById(epic.getId()));
    }

    @Test
    public void getSubTaskById() {
        assertEquals(subTask, manager.getSubTaskById(subTask.getId()));
    }

    @Test
    public void getTaskByIdWithWrongId() {
        assertNull(manager.getTaskById(0));
    }

    @Test
    public void getEpicByIdWithWrongId() {
        assertNull(manager.getEpicById(0));
    }

    @Test
    public void getSubTaskByIdWithWrongId() {
        assertNull(manager.getSubTaskById(0));
    }

    @Test
    public void updateTask() {
        task.setTaskStatus(StatusTypeEnum.DONE);
        manager.updateTask(task);
        assertEquals(StatusTypeEnum.DONE, task.getAllTasksStatus());
    }

    @Test
    public void updateSubTask() {
        subTask.setTaskStatus(StatusTypeEnum.DONE);
        manager.updateSubTask(subTask);
        assertEquals(StatusTypeEnum.DONE, subTask.getAllTasksStatus());
    }

    @Test
    public void updateTaskWithWrongData() {
        task.setTaskStatus(StatusTypeEnum.DONE);
        manager.updateTask(null);
        assertEquals(StatusTypeEnum.DONE, task.getAllTasksStatus());
    }

    @Test
    public void updateSubTaskWithWrongData() {
        subTask.setTaskStatus(StatusTypeEnum.DONE);
        manager.updateTask(null);
        assertEquals(StatusTypeEnum.DONE, subTask.getAllTasksStatus());
    }

    @Test
    public void updateEpicWhenAllSubTasksDone() {
        epic.setTaskStatus(StatusTypeEnum.DONE);
        subTask.setTaskStatus(StatusTypeEnum.DONE);
        manager.updateEpic(epic);
        assertEquals(StatusTypeEnum.DONE, epic.getAllTasksStatus());
    }

    @Test
    public void updateEpicWhenAllSubTasksDeleted() {
        manager.deleteAllSubTasks();
        manager.updateEpic(epic);
        assertEquals(StatusTypeEnum.NEW, epic.getAllTasksStatus());
    }

    @Test
    public void updateEpicWhenSubTasksDoneAndNew() throws ValidateException {
        subTask.setTaskStatus(StatusTypeEnum.DONE);
        manager.createSubTask(new SubTask("SubTask", Type.SUB_TASK, "description", StatusTypeEnum.NEW, 2,
                LocalDateTime.of(2023,2,3,20, 0), 1));
        manager.updateEpic(epic);
        assertEquals(StatusTypeEnum.IN_PROGRESS, epic.getAllTasksStatus());
    }

    @Test
    public void deleteAllTasks() {
        manager.deleteAllTasks();
        assertTrue(manager.getAllTasks().isEmpty());
    }

    @Test
    public void deleteAllEpics() {
        manager.deleteAllEpics();
        assertTrue(manager.getAllEpics().isEmpty());
    }

    @Test
    public void deleteAllSubTasks() {
        manager.deleteAllSubTasks();
        assertTrue(manager.getAllSubTasks().isEmpty());
    }

    @Test
    void deleteTaskById() {
        manager.deleteTaskById(task.getId());
        assertNull(manager.getTaskById(task.getId()));
    }

    @Test
    void deleteEpicById() {
        manager.deleteEpicById(epic.getId());
        assertNull(manager.getEpicById(epic.getId()));
    }

    @Test
    void deleteSubTaskById() {
        manager.deleteSubTaskById(subTask.getId());
        assertNull(manager.getSubTaskById(subTask.getId()));
    }

    @Test
    void deleteTaskByIdWithWrongData() {
        manager.deleteTaskById(0);
        assertEquals(task, manager.getTaskById(task.getId()));
    }

    @Test
    void deleteEpicByIdWithWrongData() {
        manager.deleteEpicById(0);
        assertEquals(epic, manager.getEpicById(epic.getId()));
    }

    @Test
    void deleteSubTaskByIdWithWrongData() {
        manager.deleteSubTaskById(0);
        assertEquals(subTask, manager.getSubTaskById(subTask.getId()));
    }

    @Test
    void getAllSubTasksInEpic() {
        assertEquals(manager.getAllSubTasks(), manager.getAllSubTasksInEpic(epic));
    }

    @Test
    void getAllSubTasksInEpicWithWrongData() {
        assertEquals(Collections.emptyList(), manager.getAllSubTasksInEpic(null));
    }

    @Test
    void getHistory() {
        manager.getTaskById(task.getId());
        assertEquals(manager.getAllTasks(), manager.getHistory());
    }

    @Test
    void getHistoryWithWrongData() {
        manager.getTaskById(0);
        assertEquals(Collections.emptyList(), manager.getHistory());
    }

    @Test
    void getPrioritizedTasks() {
        List<Task> list = new ArrayList<>(manager.getprioritizedTaskss());
        assertEquals(task, list.get(0));
        assertEquals(epic, list.get(1));
    }
}