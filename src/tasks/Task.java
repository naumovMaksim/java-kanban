package tasks;

import tasks.enums.StatusTypeEnum;
import tasks.enums.TaskTypeEnum;

import java.util.Objects;

public class Task {
    protected int id;
    protected String taskName;
    protected TaskTypeEnum taskTypeEnum;
    protected String description;
    protected StatusTypeEnum taskStatusTypeEnum;

    public Task(String taskName, TaskTypeEnum taskTypeEnum, String description, StatusTypeEnum taskStatusTypeEnum) {
        this.taskName = taskName;
        this.taskTypeEnum = taskTypeEnum;
        this.description = description;
        this.taskStatusTypeEnum = taskStatusTypeEnum;
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

    public TaskTypeEnum getTaskTypeEnum() {
        return taskTypeEnum;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public StatusTypeEnum getAllTasksStatus() {
        return taskStatusTypeEnum;
    }

    public void setTaskStatus(StatusTypeEnum taskStatusTypeEnum) {
        this.taskStatusTypeEnum = taskStatusTypeEnum;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id & Objects.equals(taskName, task.taskName) & taskStatusTypeEnum == task.taskStatusTypeEnum
                & Objects.equals(description, task.description);
    }

    @Override
    public int hashCode(){
        return Objects.hash(id, taskName, taskStatusTypeEnum, description);
    }

    @Override
    public String toString() {
        String result = "Task{" +
                    "id=" + id +
                    ", taskName='" + taskName + '\'';
        if (description != null) {
            result = result + ", description.length='" + description.length() + '\'';
        } else result = result + ", description.length=null";
        result = result + ", taskStatus=" + taskStatusTypeEnum + '}';
        return result;
    }
}
