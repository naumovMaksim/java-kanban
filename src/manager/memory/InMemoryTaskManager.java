package manager.memory;

import manager.*;
import tasks.*;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    final private HistoryManager historyManager = Managers.getDefaultHistory();

    final private Map <Integer, Task> tasksMemory = new HashMap<>();
    final private Map <Integer, Epic> epicsMemory = new HashMap<>();
    final private Map <Integer, SubTask> subTasksMemory = new HashMap<>();
    private int id = 1;

    @Override
    public void createTask(Task task){
        if (task != null) {
            task.setId(id++);
            tasksMemory.put(task.getId(), task);
        }
    }

    @Override
    public void createEpic(Epic epic){
        if (epic != null) {
            epic.setId(id++);
            updateEpicStatus(epic);
            epicsMemory.put(epic.getId(), epic);
        }
    }
    @Override
    public void createSubTask(SubTask subTask){
        if (subTask != null & !epicsMemory.isEmpty()) {
            subTask.setId(id++);
            subTasksMemory.put(subTask.getId(), subTask);
            Epic epic = epicsMemory.get(subTask.getEpicId());
            epic.addSubTaskId(subTask.getId());
            updateEpicStatus(epic);
        }
    }

    @Override
    public List<Task> getAllTasks(){
        return new ArrayList<>(tasksMemory.values());
    }

    @Override
    public List<Epic> getAllEpics(){
        return new ArrayList<>(epicsMemory.values());
    }
    
    @Override
    public List<SubTask> getAllSubTasks(){
        return new ArrayList<>(subTasksMemory.values());
    }
    
    @Override
    public Task getTaskById(int taskId){
        historyManager.add(tasksMemory.get(taskId));
        return tasksMemory.get(taskId);
    }
    
    @Override
    public Epic getEpicById(int epicId){
        historyManager.add(epicsMemory.get(epicId));
        return epicsMemory.get(epicId);
    }

    @Override
    public SubTask getSubTaskById(int subTaskId){
        historyManager.add(subTasksMemory.get(subTaskId));
        return subTasksMemory.get(subTaskId);
    }

    @Override
    public void updateTask(Task task){
        tasksMemory.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic){
        final Epic savedEpic = epicsMemory.get(epic.getId());
        savedEpic.setName(epic.getName());
        savedEpic.setDescription(epic.getDescription());
    }

    @Override
    public void updateSubTask(SubTask subTask){
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
    public void deleteAllTasks(){
        tasksMemory.clear();
    }

    @Override
    public void deleteAllEpics(){
        epicsMemory.clear();
        subTasksMemory.clear();
    }

    @Override
    public void deleteAllSubTasks(){
        subTasksMemory.clear();
        for (Epic epic: epicsMemory.values()){
            epic.clearSubTaskId();
            updateEpicStatus(epic);
        }
    }

    @Override
    public void deleteTaskById(int taskId){
        tasksMemory.remove(taskId);
        historyManager.remove(taskId);
    }

    @Override
    public void deleteEpicById(int epicId){
        Epic epic = epicsMemory.get(epicId);
        for (Integer subTaskId: epic.getEpicIds()) {
            subTasksMemory.remove(subTaskId);
            historyManager.remove(subTaskId);
        }
        epicsMemory.remove(epicId);
        historyManager.remove(epicId);
    }

    @Override
    public void deleteSubTaskById(Integer subTaskId){
        subTasksMemory.remove(subTaskId);
        historyManager.remove(subTaskId);
        for (Epic epic: epicsMemory.values()) {
            epic.getEpicIds().remove(subTaskId);
            updateEpicStatus(epic);
            epicsMemory.put(epic.getId(), epic);
        }
    }
    
    @Override
    public List<SubTask> getAllSubTasksInEpic(Epic epic){
        List<SubTask> getAllSubTasksInEpic = new ArrayList<>();
        for (int i: epic.getEpicIds()){
            SubTask subTask = subTasksMemory.get(i);
            getAllSubTasksInEpic.add(subTask);
        }
        return getAllSubTasksInEpic; 
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private void updateEpicStatus(Epic epic){
        int counterNew = 0;
        int counterDone = 0;

        Set<Status> statusSet = new HashSet<>();
        for (int subId: epic.getEpicIds()){
            SubTask subTask = subTasksMemory.get(subId);
            Status status = subTask.getAllTasksStatus();
            statusSet.add(status);
            if (status.equals(Status.NEW)){
                counterNew++;
            } else if (status.equals(Status.DONE)){
                counterDone++;
            }
        }
        if (statusSet.isEmpty()){
            epic.setTaskStatus(Status.NEW);
            return;
        }
        if(statusSet.contains(Status.NEW) & statusSet.size() <= counterNew){
            epic.setTaskStatus(Status.NEW);
            return;
        }
        if (statusSet.contains(Status.DONE) & statusSet.size() <= counterDone){
            epic.setTaskStatus(Status.DONE);
            return;
        }
        epic.setTaskStatus(Status.IN_PROGRESS);
    }
}
