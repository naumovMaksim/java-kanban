package tasks;

import tasks.enums.StatusTypeEnum;
import tasks.enums.Type;


import java.time.LocalDateTime;
import java.util.Objects;

public class SubTask extends Task {
    protected int epicId;

    public SubTask(String name, Type type, String description, StatusTypeEnum status, int epicId) {
        super(name, type, description, status);
        this.epicId = epicId;
    }

    public SubTask(String name, Type type, String description, StatusTypeEnum status, int epicId, LocalDateTime startTime,
                   long duration) {
        super(name, type, description, status, startTime, duration);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SubTask subTask = (SubTask) o;
        return epicId == subTask.epicId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "epicId=" + epicId +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", taskStatus=" + status + ", startTime: " + startTime + ", duration:" + duration +
                ", endTime: " + getEndTime() +'}';
    }
}
