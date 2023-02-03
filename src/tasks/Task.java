package tasks;

import tasks.enums.StatusTypeEnum;
import tasks.enums.type;

import java.time.Instant;
import java.util.Objects;

public class Task {
    protected int id;
    protected String name;
    protected type type;
    protected String description;
    protected StatusTypeEnum status;
    protected Instant startTime;
    protected long duration;

    public Task(String name, type type, String description, StatusTypeEnum status) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.status = status;
    }

    public Task(String name, type type, String description, StatusTypeEnum status, Instant startTime, long duration) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
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

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public Instant getEndTime() {
        return startTime.plusSeconds(duration);
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
        result = result + ", taskStatus=" + status + ", startTime: " + startTime + ", duration:" + duration +
                ", endTime: " + getEndTime() +'}';
        return result;
    }
}
