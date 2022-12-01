package tasks;

import java.util.Objects;

public class Task {
    protected int id;
    protected String taskName;
    protected String description;
    protected Status taskStatus;

    public Task(String taskName, String description, Status taskStatus) {
        this.taskName = taskName;
        this.description = description;
        this.taskStatus = taskStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return taskName;
    }

    public void setName(String taskName) {
        this.taskName = taskName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getAllTasksStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Status taskStatus) {
        this.taskStatus = taskStatus;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id & Objects.equals(taskName, task.taskName) & taskStatus == task.taskStatus
                & Objects.equals(description, task.description);
    }

    @Override
    public int hashCode(){
        return Objects.hash(id, taskName, taskStatus, description);
    }

    @Override
    public String toString() {
        String result = "Task{" +
                    "id=" + id +
                    ", taskName='" + taskName + '\'';
        if (description != null) {
            result = result + ", description.length='" + description.length() + '\'';
        } else result = result + ", description.length=null";
        result = result + ", taskStatus=" + taskStatus + '}';
        return result;
    }
}
