package Manager;

import Tasks.*;

import java.util.*;

public class TaskManagerMemory implements TaskManager {

    protected HashMap <Integer, Task> taskMemory = new HashMap<>();
    protected HashMap <Integer, Epic> epicMemory = new HashMap<>();
    protected HashMap <Integer, SubTask> subTuskMemory = new HashMap<>();
    protected int id = 1;

    @Override
    public void createTask(Task task){
        if (task != null) {
            task.setId(id++);
            taskMemory.put(task.getId(), task);
        }
    }

    @Override
    public void createEpic(Epic epic){
        if (epic != null) {
            epic.setId(id++);
            setStatus(epic);
            epicMemory.put(epic.getId(), epic);
        }
    }

    @Override
    public void createSubTusk(SubTask subTask, int epicId){
        if (subTask != null & epicId > 0) {
            subTask.setId(epicId);//
            subTask.setSubTuskID(id++);
            subTuskMemory.put(subTask.getSubTuskId(), subTask);
            Epic epic = epicMemory.get(epicId);
            if (epic != null) {
                epic.setSubTuskId(subTask.getSubTuskId());
                setStatus(epic);
                epicMemory.put(epic.getId(), epic);
            }
        }
    }

    @Override
    public ArrayList<Task> getAllTasks(){
        return new ArrayList<>(taskMemory.values());
    }

    @Override
    public ArrayList<Epic> getAllEpics(){
        return new ArrayList<>(epicMemory.values());
    }
    
    @Override
    public ArrayList<SubTask> getAllSubTusks(){
        return new ArrayList<>(subTuskMemory.values());
    }
    
    @Override
    public Task getTaskById(int tuskId){
        return taskMemory.get(tuskId);
    }
    
    @Override
    public Epic getEpicById(int epicId){
        return epicMemory.get(epicId);
    }

    @Override
    public ArrayList<SubTask> getSubTask(int id){
        Epic epic = epicMemory.get(id);
        ArrayList<SubTask> subTaskArrayList = new ArrayList<>();
        List<Integer> sub = epic.getSubTuskId();
        for (Integer i: sub){
            subTaskArrayList.add(subTuskMemory.get(i));
        }
        return subTaskArrayList;
    }

    @Override
    public void updateTask(Task task){
        taskMemory.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic){
        setStatus(epic);
        epicMemory.put(epic.getId(), epic);
    }

    @Override
    public void updateSubTusk(SubTask subTask){
        setStatus(epicMemory.get(subTask.getId()));
    }

    @Override
    public void deleteAllTasks(){
        taskMemory.clear();
    }

    @Override
    public void deleteAllEpics(){
        epicMemory.clear();
    }

    @Override
    public void deleteAllSubTusks(){
        subTuskMemory.clear();
        for (Epic epic: epicMemory.values()){
            epic.getSubTuskId().clear();
            setStatus(epic);
            epicMemory.put(epic.getId(), epic);
        }
    }

    @Override
    public void deleteTaskById(int tId){
        taskMemory.remove(tId);
    }

    @Override
    public void deleteEpicById(int eId){
        epicMemory.remove(eId);
    }

    @Override
    public void deleteSubTaskById(Integer sId){
        subTuskMemory.remove(sId);
        for (Epic epic: epicMemory.values()) {
            epic.getSubTuskId().remove(sId);
            setStatus(epic);
            epicMemory.put(epic.getId(), epic);
        }
    }
    
    @Override
    public ArrayList<SubTask> getAllSubTasksInEpic(Epic epic){
        ArrayList<SubTask> getAllSubTasksInEpic = new ArrayList<>();
        for (int i: epic.getSubTuskId()){
            SubTask subTask = subTuskMemory.get(i);
            getAllSubTasksInEpic.add(subTask);
        }
        return getAllSubTasksInEpic; 
    }

    protected void setStatus(Epic epic){
        Set<Status> statusSet = new HashSet<>();
        for (int subId: epic.getSubTuskId()){
            SubTask subTask = subTuskMemory.get(subId);
            Status status = subTask.getAllTasksStatus();
            statusSet.add(status);
        }
        if (statusSet.isEmpty()){
            epic.setTaskStatus(Status.NEW);
            return;
        }
        if(statusSet.contains(Status.NEW) & statusSet.size() == 1){
            epic.setTaskStatus(Status.NEW);
            return;
        }
        if (statusSet.contains(Status.DONE) & statusSet.size() == 1){
            epic.setTaskStatus(Status.DONE);
            return;
        }
        epic.setTaskStatus(Status.IN_PROGRESS);
    }
}
