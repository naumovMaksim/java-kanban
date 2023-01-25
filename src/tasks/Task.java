package tasks;

import tasks.enums.StatusTypeEnum;
import tasks.enums.type;

import java.util.Objects;

public class Task {
    protected int id;
    protected String name;
    protected type type;
    protected String description;
    protected StatusTypeEnum status;

    public Task(String name, type type, String description, StatusTypeEnum status) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public type getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public StatusTypeEnum getAllTasksStatus() {
        return status;
    }

    public void setTaskStatus(StatusTypeEnum status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id & Objects.equals(name, task.name) & status == task.status
                & Objects.equals(description, task.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, status, description);
    }

    @Override
    public String toString() {
        String result = "Task{" +
                "id=" + id +
                ", name='" + name + '\'';
        if (description != null) {
            result = result + ", description.length='" + description.length() + '\'';
        } else result = result + ", description.length=null";
        result = result + ", taskStatus=" + status + '}';
        return result;
    }
}
