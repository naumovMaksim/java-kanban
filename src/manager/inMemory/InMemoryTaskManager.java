package manager.inMemory;

import exception.ValidateException;
import manager.*;
import manager.interfaces.HistoryManager;
import manager.interfaces.TaskManager;
import tasks.*;
import tasks.enums.StatusTypeEnum;


import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    final private HistoryManager historyManager = Managers.getDefaultHistory();

    protected final Map<Integer, Task> tasksMemory = new HashMap<>();
    protected final Map<Integer, Epic> epicsMemory = new HashMap<>();
    protected final Map<Integer, SubTask> subTasksMemory = new HashMap<>();
    private final Comparator<Task> taskComparator = Comparator.comparing(Task::getStartTime);
    private final Set<Task> prioritizedTasks = new TreeSet<>(taskComparator);
    protected int id = 1;

    @Override
    public int createTask(Task task) throws ValidateException {
        int taskId = -1;
        if (task != null) {
            taskId = id++;
            task.setId(taskId);
            try {
                validateTaskPriority(task);
            } catch (ValidateException e) {
                throw new ValidateException(e.getMessage());
            }
            addprioritizedTasks(task);
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
            updateEpicTime(epic);
            epicsMemory.put(epic.getId(), epic);
            addprioritizedTasks(epic);
        }
        return epicId;
    }

    @Override
    public int createSubTask(SubTask subTask) throws ValidateException {
        int subtaskId = -1;
        if (subTask != null & !epicsMemory.isEmpty()) {
            subtaskId = id++;
            subTask.setId(subtaskId);
            try {
                validateTaskPriority(subTask);
            } catch (ValidateException e) {
                throw new ValidateException(e.getMessage());
            }
            subTasksMemory.put(subTask.getId(), subTask);
            Epic epic = epicsMemory.get(subTask.getEpicId());
            if (epic != null) {
                epic.addSubTaskId(subTask.getId());
                updateEpicStatus(epic);
                updateEpicTime(epic);
                addprioritizedTasks(subTask);
            } else {
                return -1;
            }
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
        if (task != null) {
            try {
                validateTaskPriority(task);
            } catch (ValidateException e) {
                return;
            }
            addprioritizedTasks(task);
            tasksMemory.put(task.getId(), task);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epic != null) {
            final Epic savedEpic = epicsMemory.get(epic.getId());
            savedEpic.setName(epic.getName());
            savedEpic.setDescription(epic.getDescription());
            updateEpicStatus(epic);
            updateEpicTime(epic);
        }
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        if (subTask != null) {
            final int id = subTask.getId();
            final int epicId = subTask.getEpicId();
            final SubTask savedSubtask = subTasksMemory.get(id);
            if (savedSubtask == null) {
                return;
            }
            try {
                validateTaskPriority(subTask);
            } catch (ValidateException e) {
                return;
            }
            final Epic epic = epicsMemory.get(epicId);
            if (epic == null) {
                return;
            }
            addprioritizedTasks(subTask);
            subTasksMemory.put(id, subTask);
            updateEpicStatus(epic);
            updateEpicTime(epic);
        }
    }

    @Override
    public void deleteAllTasks() {
        for (Task task : tasksMemory.values()) {
            prioritizedTasks.remove(task);
        }
        tasksMemory.clear();
    }

    @Override
    public void deleteAllEpics() {
        for (Epic epic : epicsMemory.values()) {
            prioritizedTasks.remove(epic);
            for (int subTaskId : epic.getSubtaskIds()) {
                SubTask subTask = subTasksMemory.get(subTaskId);
                prioritizedTasks.remove(subTask);
            }
        }
        epicsMemory.clear();
        subTasksMemory.clear();
    }

    @Override
    public void deleteAllSubTasks() {
        for (Epic epic : epicsMemory.values()) {
            for (int subTaskId : epic.getSubtaskIds()) {
                SubTask subTask = subTasksMemory.get(subTaskId);
                prioritizedTasks.remove(subTask);
            }
            epic.clearSubTaskId();
            updateEpicStatus(epic);
        }
        subTasksMemory.clear();
    }

    @Override
    public void deleteTaskById(int taskId) {
        if (taskId != 0) {
            Task task = tasksMemory.get(taskId);
            tasksMemory.remove(taskId);
            historyManager.remove(taskId);
            prioritizedTasks.remove(task);
        }
    }

    @Override
    public void deleteEpicById(int epicId) {
        if (epicId != 0) {
            Epic epic = epicsMemory.get(epicId);
            for (Integer subTaskId : epic.getSubtaskIds()) {
                SubTask subTask = subTasksMemory.get(subTaskId);
                prioritizedTasks.remove(subTask);
                subTasksMemory.remove(subTaskId);
                historyManager.remove(subTaskId);
            }
            epicsMemory.remove(epicId);
            historyManager.remove(epicId);
            prioritizedTasks.remove(epic);
        }
    }

    @Override
    public void deleteSubTaskById(Integer subTaskId) {
        if (subTaskId != 0) {
            SubTask subTask = subTasksMemory.get(subTaskId);
            subTasksMemory.remove(subTaskId);
            historyManager.remove(subTaskId);
            prioritizedTasks.remove(subTask);
            for (Epic epic : epicsMemory.values()) {
                epic.getSubtaskIds().remove(subTaskId);
                updateEpicStatus(epic);
                updateEpicTime(epic);
                epicsMemory.put(epic.getId(), epic);
            }
        }
    }

    @Override
    public List<SubTask> getAllSubTasksInEpic(Epic epic) {
        if (epic != null) {
            List<SubTask> getAllSubTasksInEpic = new ArrayList<>();
            for (int i : epic.getSubtaskIds()) {
                SubTask subTask = subTasksMemory.get(i);
                getAllSubTasksInEpic.add(subTask);
            }
            return getAllSubTasksInEpic;
        } else {
            return Collections.emptyList();
        }
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

    private void addprioritizedTasks(Task task) {
        prioritizedTasks.add(task);

    }

    public List<Task> getprioritizedTaskss() {
        return List.copyOf(prioritizedTasks);
    }

    private void validateTaskPriority(Task task) throws ValidateException {
        if (!prioritizedTasks.isEmpty()) {
            final LocalDateTime startTime = LocalDateTime.from(task.getStartTime());
            final LocalDateTime endTime = LocalDateTime.from(task.getEndTime());
            for (Task t : prioritizedTasks) {
                final LocalDateTime existStart = t.getStartTime();
                final LocalDateTime existEnd = t.getEndTime();
                if (!endTime.isAfter(existStart)) {// newTimeEnd <= existTimeStart
                    continue;
                }
                if (!existEnd.isAfter(startTime)) {// existTimeEnd <= newTimeStart
                    continue;
                }
                throw new ValidateException("Задачи пересекаются с id=" + t.getId() + " c " + existStart + " по " + existEnd);
            }
            prioritizedTasks.add(task);
        }
    }

    public void updateEpicTime(Epic epic) {
        List<Integer> subTasksIds = epic.getSubtaskIds();
        LocalDateTime epicStarts = LocalDateTime.MAX;
        LocalDateTime epicEnds = LocalDateTime.MIN;
        long epicDuration = 0L;
        if (!subTasksIds.isEmpty()) {
            epic.setStartTime(epicStarts);
            epic.setEndTime(epicEnds);
            for (int id : subTasksIds) {
                SubTask subTask = subTasksMemory.get(id);
                LocalDateTime startTime = subTask.getStartTime();
                LocalDateTime endTime = subTask.getEndTime();
                if (startTime.isBefore(epic.getStartTime())) {
                    epic.setStartTime(startTime);
                }
                if (endTime.isAfter(epic.getEndTime())) {
                    epic.setEndTime(endTime);
                }
                epicDuration += subTask.getDuration();
            }
            epic.setDuration(epicDuration);
        }
    }
}
