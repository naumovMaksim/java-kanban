package tasks;

import java.util.Objects;

public class SubTask extends Task {
    protected int subTuskId;

    public SubTask(String taskName, String description, Status taskStatus) {
        super(taskName, description, taskStatus);
    }

    public int getSubTuskId() {
        return subTuskId;
    }

    public void addSubTaskId(int subTuskId) {
        this.subTuskId = subTuskId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SubTask subTask = (SubTask) o;
        return subTuskId == subTask.subTuskId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subTuskId);
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "subTuskId=" + subTuskId +
                ", id=" + id +
                ", taskName='" + taskName + '\'' +
                ", description='" + description + '\'' +
                ", taskStatus=" + taskStatus +
                '}';
    }
}
