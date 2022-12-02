package manager;

import tasks.*;

import java.util.*;

public class TaskManagerMemory implements TaskManager {
    HashMap <Integer, Task> tasksMemory = new HashMap<>();
    HashMap <Integer, Epic> epicsMemory = new HashMap<>();
    HashMap <Integer, SubTask> subTasksMemory = new HashMap<>();
    protected int id = 1;

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

    public void createSubTask(SubTask subTask){
        if (subTask != null) {
            subTask.setId(id++);
            subTasksMemory.put(subTask.getId(), subTask);
            Epic epic = epicsMemory.get(subTask.getEpicId());
            if (epic != null) {
                epic.addSubTaskId(subTask.getId());
                updateEpicStatus(epic);
            }
        }
    }

    @Override
    public ArrayList<Task> getAllTasks(){
        return new ArrayList<>(tasksMemory.values());
    }

    @Override
    public ArrayList<Epic> getAllEpics(){
        return new ArrayList<>(epicsMemory.values());
    }
    
    @Override
    public ArrayList<SubTask> getAllSubTasks(){
        return new ArrayList<>(subTasksMemory.values());
    }
    
    @Override
    public Task getTaskById(int taskId){
        return tasksMemory.get(taskId);
    }
    
    @Override
    public Epic getEpicById(int epicId){
        return epicsMemory.get(epicId);
    }

    @Override
    public SubTask getSubTaskById(int subTaskId){
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
    }

    @Override
    public void deleteEpicById(int epicId){
        Epic epic = epicsMemory.get(epicId);
        for (Integer subTaskId: epic.getEpicIds()) {
            subTasksMemory.remove(subTaskId);
        }
        epicsMemory.remove(epicId);
    }

    @Override
    public void deleteSubTaskById(Integer subTaskId){
        subTasksMemory.remove(subTaskId);
        for (Epic epic: epicsMemory.values()) {
            epic.getEpicIds().remove(subTaskId);
            updateEpicStatus(epic);
            epicsMemory.put(epic.getId(), epic);
        }
    }
    
    @Override
    public ArrayList<SubTask> getAllSubTasksInEpic(Epic epic){
        ArrayList<SubTask> getAllSubTasksInEpic = new ArrayList<>();
        for (int i: epic.getEpicIds()){
            SubTask subTask = subTasksMemory.get(i);
            getAllSubTasksInEpic.add(subTask);
        }
        return getAllSubTasksInEpic; 
    }

    protected void updateEpicStatus(Epic epic){
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
