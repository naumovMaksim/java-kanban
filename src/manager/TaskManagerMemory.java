package manager;

import tasks.*;

import java.util.*;

public class TaskManagerMemory implements TaskManager {
    HashMap <Integer, Task> tasksMemory = new HashMap<>();
    HashMap <Integer, Epic> epicsMemory = new HashMap<>();
    HashMap <Integer, SubTask> subTusksMemory = new HashMap<>();
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

    public void createSubTusk(SubTask subTask, int epicId){// Я не понимаю как сделать без передачи "epicId". Как тогда передать subTusk к какому epic он принадлежит? Очень долго пытался что то придумать, но безуспешно:(
        Epic epic = epicsMemory.get(epicId);
        if (subTask != null & epic != null) {
            subTask.setId(epicId);
            subTask.addSubTaskId(id++);
            subTusksMemory.put(subTask.getSubTuskId(), subTask);
            epic.addSubTaskId(subTask.getSubTuskId());
            updateEpicStatus(epic);
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
    public ArrayList<SubTask> getAllSubTusks(){
        return new ArrayList<>(subTusksMemory.values());
    }
    
    @Override
    public Task getTaskById(int tuskId){
        return tasksMemory.get(tuskId);
    }
    
    @Override
    public Epic getEpicById(int epicId){
        return epicsMemory.get(epicId);
    }

    @Override
    public ArrayList<SubTask> getSubTask(int epicId){
        Epic epic = epicsMemory.get(epicId);
        ArrayList<SubTask> subTaskArrayList = new ArrayList<>();
        List<Integer> sub = epic.getSubTuskId();
        for (Integer i: sub){
            subTaskArrayList.add(subTusksMemory.get(i));
        }
        return subTaskArrayList;
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
    public void updateSubTusk(SubTask subTask){
        if (subTusksMemory.containsKey(subTask.getSubTuskId()) & epicsMemory.containsKey(subTask.getId())){
            SubTask savedSubTusk = subTusksMemory.get(subTask.getSubTuskId());
            savedSubTusk.setName(subTask.getName());
            savedSubTusk.setDescription(subTask.getDescription());
            updateEpicStatus(epicsMemory.get(subTask.getId()));
        }
    }

    @Override
    public void deleteAllTasks(){
        tasksMemory.clear();
    }

    @Override
    public void deleteAllEpics(){
        epicsMemory.clear();
        subTusksMemory.clear();
    }

    @Override
    public void deleteAllSubTusks(){
        subTusksMemory.clear();
        for (Epic epic: epicsMemory.values()){
            epic.clearSubTaskId();
            updateEpicStatus(epic);
        }
    }

    @Override
    public void deleteTaskById(int tId){
        tasksMemory.remove(tId);
    }

    @Override
    public void deleteEpicById(int eId){
        epicsMemory.remove(eId);
        ArrayList<Integer> idDelSubTasks = new ArrayList<>();
        for (SubTask subTask : subTusksMemory.values()){
            if (subTask.getId() == eId) {
                idDelSubTasks.add(subTask.getSubTuskId());
            }
        }
        for (Integer id: idDelSubTasks){
            subTusksMemory.remove(id);
        }
    }

    @Override
    public void deleteSubTaskById(Integer sId){
        subTusksMemory.remove(sId);
        for (Epic epic: epicsMemory.values()) {
            epic.getSubTuskId().remove(sId);
            updateEpicStatus(epic);
            epicsMemory.put(epic.getId(), epic);
        }
    }
    
    @Override
    public ArrayList<SubTask> getAllSubTasksInEpic(Epic epic){
        ArrayList<SubTask> getAllSubTasksInEpic = new ArrayList<>();
        for (int i: epic.getSubTuskId()){
            SubTask subTask = subTusksMemory.get(i);
            getAllSubTasksInEpic.add(subTask);
        }
        return getAllSubTasksInEpic; 
    }

    protected void updateEpicStatus(Epic epic){
        int counterNew = 0;
        int counterDone = 0;

        Set<Status> statusSet = new HashSet<>();
        for (int subId: epic.getSubTuskId()){
            SubTask subTask = subTusksMemory.get(subId);
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
