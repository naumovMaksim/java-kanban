package Tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {

    protected List <Integer> subTuskIDs;

    public Epic(String taskName, String description, Status taskStatus) {

        super(taskName, description, taskStatus);
        subTuskIDs = new ArrayList<>();
    }

    public List<Integer> getSubTuskId() {
        return subTuskIDs;
    }

    public void setSubTuskId(int subTaskId) {
        subTuskIDs.add(subTaskId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subTuskIDs, epic.subTuskIDs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subTuskIDs);
    }

    @Override
    public String toString() {
        String result = "Epic{" +
                "subTuskIDs=" + subTuskIDs +
                ", id=" + id +
                ", taskName='" + taskName + '\'';
        if (description != null) {
            result = result + ", description.length='" + description.length() + '\'';
        } else result = result + ", description.length='null'";
        result = result + ", taskStatus=" + taskStatus + '}';
        return result;
    }
}
