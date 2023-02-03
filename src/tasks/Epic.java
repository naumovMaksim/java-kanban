package tasks;

import tasks.enums.StatusTypeEnum;
import tasks.enums.Type;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {

    protected List<Integer> subtaskIds;
    protected LocalDateTime endTime;

    public Epic(String name, Type type, String description, StatusTypeEnum status) {
        super(name, type, description, status);
        subtaskIds = new ArrayList<>();
    }

    public Epic(String name, Type type, String description, StatusTypeEnum status, LocalDateTime startTime, long duration) {
        super(name, type, description, status, startTime, duration);
        subtaskIds = new ArrayList<>();
        this.endTime = super.getEndTime();
    }

    public List<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void addSubTaskId(int subTaskId) {
        subtaskIds.add(subTaskId);
    }

    public void clearSubTaskId() {
        subtaskIds.clear();
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public LocalDateTime getEndTime(){
        return endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtaskIds, epic.subtaskIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtaskIds);
    }

    @Override
    public String toString() {
        String result = "Epic{" +
                "subtaskIds=" + subtaskIds +
                ", id=" + id +
                ", name='" + name + '\'';
        if (description != null) {
            result = result + ", description.length='" + description.length() + '\'';
        } else result = result + ", description.length='null'";
        result = result + ", taskStatus=" + status + ", startTime: " + startTime + ", duration:" + duration +
                ", endTime: " + getEndTime() +'}';
        return result;
    }
}
