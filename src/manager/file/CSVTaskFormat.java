package manager.file;

import manager.interfaces.HistoryManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.enums.StatusTypeEnum;
import tasks.enums.type;

import java.util.ArrayList;
import java.util.List;

public class CSVTaskFormat {
    public static String getHeader() {
        return "id,type,name,status,description,epic";
    }

    public static String historyToString(HistoryManager historyManager) {
        final List<Task> history = historyManager.getHistory();
        if (history.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(history.get(0).getId());
        for (int i = 1; i < history.size(); i++) {
            Task task = history.get(i);
            sb.append(",");
            sb.append(task.getId());
        }
        return sb.toString();
    }

    public static List<Integer> historyFromString(String value) {
        List<Integer> history = new ArrayList<>();
        if (!value.isEmpty()) {
            String[] line = value.split(",");
            for (String s : line) {
                history.add(Integer.parseInt(s));
            }
            return history;
        }
        return history;
    }

    public static String toString(Task task) {
        String subTaskEpicId = "";

        if (task.getType().equals(type.SUB_TASK)) {
            subTaskEpicId = String.valueOf(((SubTask) task).getEpicId());
        }
        return String.format("%s,%s,%s,%s,%s,%s\n",
                task.getId(),
                task.getType(),
                task.getName(),
                task.getAllTasksStatus(),
                task.getDescription(),
                subTaskEpicId);
    }

    public static Task fromString(String result) {
        int id;
        type taskType;
        String name;
        StatusTypeEnum status;
        String description;

        String[] line = result.split(",");
        if (line[1].equals("TASK") || line[1].equals("EPIC") || line[1].equals("SUB_TASK")) {
            id = Integer.parseInt(line[0]);
            taskType = type.valueOf(line[1]);
            name = line[2];
            status = StatusTypeEnum.valueOf(line[3]);
            description = line[4];

            switch (taskType) {
                case EPIC:
                    Epic epic = new Epic(name, taskType, description, status);
                    epic.setId(id);// не понял что нужно сделать: (добавь еще он конструктор в Task, что бы принимал и id тоже. Касается всех case)
                    return epic;
                case TASK:
                    Task task = new Task(name, taskType, description, status);
                    task.setId(id);
                    return task;
                case SUB_TASK:
                    SubTask subTask = new SubTask(name, taskType, description, status, Integer.parseInt(line[5]));
                    subTask.setId(id);
                    return subTask;
            }
        }
        return null;
    }
}
