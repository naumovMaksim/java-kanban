package tasks;

import tasks.enums.StatusTypeEnum;
import tasks.enums.TaskTypeEnum;

import java.util.Objects;

public class SubTask extends Task {
    protected int epicId;

    public SubTask(String taskName, TaskTypeEnum taskTypeEnum, String description, StatusTypeEnum taskStatusTypeEnum, int epicId) {
        super(taskName,taskTypeEnum, description, taskStatusTypeEnum);
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
                ", taskName='" + taskName + '\'' +
                ", description='" + description + '\'' +
                ", taskStatus=" + taskStatusTypeEnum +
                '}';
    }
}
