package tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {

    protected List<Integer> epicIds;

    public Epic(String taskName, String description, Status taskStatus) {

        super(taskName, description, taskStatus);
        epicIds = new ArrayList<>();
    }

    public List<Integer> getEpicIds() {
        return epicIds;
    }

    public void addSubTaskId(int subTaskId) {
        epicIds.add(subTaskId);
    }
    public void clearSubTaskId(){
        epicIds.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(epicIds, epic.epicIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicIds);
    }

    @Override
    public String toString() {
        String result = "Epic{" +
                "epicIds=" + epicIds +
                ", id=" + id +
                ", taskName='" + taskName + '\'';
        if (description != null) {
            result = result + ", description.length='" + description.length() + '\'';
        } else result = result + ", description.length='null'";
        result = result + ", taskStatus=" + taskStatus + '}';
        return result;
    }
}
