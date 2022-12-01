package tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {

    protected List<Integer> subTuskIds;

    public Epic(String taskName, String description, Status taskStatus) {

        super(taskName, description, taskStatus);
        subTuskIds = new ArrayList<>();
    }

    public List<Integer> getSubTuskId() {
        return subTuskIds;
    }

    public void addSubTaskId(int subTaskId) {
        subTuskIds.add(subTaskId);
    }
    public void clearSubTaskId(){
        subTuskIds.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subTuskIds, epic.subTuskIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subTuskIds);
    }

    @Override
    public String toString() {
        String result = "Epic{" +
                "subTuskIds=" + subTuskIds +
                ", id=" + id +
                ", taskName='" + taskName + '\'';
        if (description != null) {
            result = result + ", description.length='" + description.length() + '\'';
        } else result = result + ", description.length='null'";
        result = result + ", taskStatus=" + taskStatus + '}';
        return result;
    }
}
