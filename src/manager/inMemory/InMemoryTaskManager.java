package manager.inMemory;

import manager.*;
import manager.interfaces.HistoryManager;
import manager.interfaces.TaskManager;
import tasks.*;
import tasks.enums.StatusTypeEnum;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    final private HistoryManager historyManager = Managers.getDefaultHistory();

    protected final Map<Integer, Task> tasksMemory = new HashMap<>();
    protected final Map<Integer, Epic> epicsMemory = new HashMap<>();
    protected final Map<Integer, SubTask> subTasksMemory = new HashMap<>();
    protected int id = 1;

    @Override
    public int createTask(Task task) {
        int taskId = -1;
        if (task != null) {
            taskId = id++;
            task.setId(taskId);
            tasksMemory.put(task.getId(), task);
        }
        return taskId;
    }

    @Override
    public int createEpic(Epic epic) {
        int epicId = -1;
        if (epic != null) {
            epicId = id++;
            epic.setId(epicId);
            updateEpicStatus(epic);
            epicsMemory.put(epic.getId(), epic);
        }
        return epicId;
    }

    @Override
    public int createSubTask(SubTask subTask) {
        int subtaskId = -1;
        if (subTask != null & !epicsMemory.isEmpty()) {
            subtaskId = id++;
            subTask.setId(subtaskId);
            subTasksMemory.put(subTask.getId(), subTask);
            Epic epic = epicsMemory.get(subTask.getEpicId());
            epic.addSubTaskId(subTask.getId());
            updateEpicStatus(epic);
        }
        return subtaskId;
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasksMemory.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epicsMemory.values());
    }

    @Override
    public List<SubTask> getAllSubTasks() {
        return new ArrayList<>(subTasksMemory.values());
    }

    @Override
    public Task getTaskById(int taskId) {
        historyManager.add(tasksMemory.get(taskId));
        return tasksMemory.get(taskId);
    }

    @Override
    public Epic getEpicById(int epicId) {
        historyManager.add(epicsMemory.get(epicId));
        return epicsMemory.get(epicId);
    }

    @Override
    public SubTask getSubTaskById(int subTaskId) {
        historyManager.add(subTasksMemory.get(subTaskId));
        return subTasksMemory.get(subTaskId);
    }

    @Override
    public void updateTask(Task task) {
        tasksMemory.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) {
        final Epic savedEpic = epicsMemory.get(epic.getId());
        savedEpic.setName(epic.getName());
        savedEpic.setDescription(epic.getDescription());
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        final int id = subTask.getId();
        final int epicId = subTask.getEpicId();
        final SubTask savedSubtask = subTasksMemory.get(id);
        if (savedSubtask == null) {
            return;
        }
        final Epic epic = epicsMemory.get(epicId);
        if (epic == null) {
            return;
        }
        subTasksMemory.put(id, subTask);
        updateEpicStatus(epic);
    }

    @Override
    public void deleteAllTasks() {
        tasksMemory.clear();
    }

    @Override
    public void deleteAllEpics() {
        epicsMemory.clear();
        subTasksMemory.clear();
    }

    @Override
    public void deleteAllSubTasks() {
        subTasksMemory.clear();
        for (Epic epic : epicsMemory.values()) {
            epic.clearSubTaskId();
            updateEpicStatus(epic);
        }
    }

    @Override
    public void deleteTaskById(int taskId) {
        tasksMemory.remove(taskId);
        historyManager.remove(taskId);
    }

    @Override
    public void deleteEpicById(int epicId) {
        Epic epic = epicsMemory.get(epicId);
        for (Integer subTaskId : epic.getSubtaskIds()) {
            subTasksMemory.remove(subTaskId);
            historyManager.remove(subTaskId);
        }
        epicsMemory.remove(epicId);
        historyManager.remove(epicId);
    }

    @Override
    public void deleteSubTaskById(Integer subTaskId) {
        subTasksMemory.remove(subTaskId);
        historyManager.remove(subTaskId);
        for (Epic epic : epicsMemory.values()) {
            epic.getSubtaskIds().remove(subTaskId);
            updateEpicStatus(epic);
            epicsMemory.put(epic.getId(), epic);
        }
    }

    @Override
    public List<SubTask> getAllSubTasksInEpic(Epic epic) {
        List<SubTask> getAllSubTasksInEpic = new ArrayList<>();
        for (int i : epic.getSubtaskIds()) {
            SubTask subTask = subTasksMemory.get(i);
            getAllSubTasksInEpic.add(subTask);
        }
        return getAllSubTasksInEpic;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    protected HistoryManager getHistoryManager() {
        return historyManager;
    }

    private void updateEpicStatus(Epic epic) {
        int counterNew = 0;
        int counterDone = 0;

        Set<StatusTypeEnum> statusTypeEnumSet = new HashSet<>();
        for (int subId : epic.getSubtaskIds()) {
            SubTask subTask = subTasksMemory.get(subId);
            StatusTypeEnum statusTypeEnum = subTask.getAllTasksStatus();
            statusTypeEnumSet.add(statusTypeEnum);
            if (statusTypeEnum.equals(StatusTypeEnum.NEW)) {
                counterNew++;
            } else if (statusTypeEnum.equals(StatusTypeEnum.DONE)) {
                counterDone++;
            }
        }
        if (statusTypeEnumSet.isEmpty()) {
            epic.setTaskStatus(StatusTypeEnum.NEW);
            return;
        }
        if (statusTypeEnumSet.contains(StatusTypeEnum.NEW) & statusTypeEnumSet.size() <= counterNew) {
            epic.setTaskStatus(StatusTypeEnum.NEW);
            return;
        }
        if (statusTypeEnumSet.contains(StatusTypeEnum.DONE) & statusTypeEnumSet.size() <= counterDone) {
            epic.setTaskStatus(StatusTypeEnum.DONE);
            return;
        }
        epic.setTaskStatus(StatusTypeEnum.IN_PROGRESS);
    }

    protected void addAnyTask(Task task) {
        final int id = task.getId();
        switch (task.getType()) {
            case TASK:
                tasksMemory.put(id, task);
                break;
            case SUB_TASK:
                subTasksMemory.put(id, (SubTask) task);
                break;
            case EPIC:
                epicsMemory.put(id, (Epic) task);
                break;
        }
    }

    protected Task findTask(Integer id) {
        final Task task = tasksMemory.get(id);
        if (task != null) {
            return task;
        }
        final SubTask subtask = subTasksMemory.get(id);
        if (subtask != null) {
            return subtask;
        }
        return epicsMemory.get(id);
    }
}
