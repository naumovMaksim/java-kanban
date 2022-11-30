package Tasks;

import java.util.Objects;

public class SubTask extends Task {
    protected int subTuskID;

    public SubTask(String taskName, String description, Status taskStatus) {
        super(taskName, description, taskStatus);
    }

    public int getSubTuskId() {
        return subTuskID;
    }

    public void setSubTuskID(int subTuskID) {
        this.subTuskID = subTuskID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SubTask subTask = (SubTask) o;
        return subTuskID == subTask.subTuskID;
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), subTuskID);
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "subTuskID=" + subTuskID +
                ", id=" + id +
                ", taskName='" + taskName + '\'' +
                ", description='" + description + '\'' +
                ", taskStatus=" + taskStatus +
                '}';
    }
}
