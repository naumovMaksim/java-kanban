package manager.file;

import manager.interfaces.HistoryManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.enums.StatusTypeEnum;
import tasks.enums.type;

import java.time.Instant;
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
        return String.format("%s,%s,%s,%s,%s,%s",
                task.getId(),
                task.getType(),
                task.getName(),
                task.getAllTasksStatus(),
                task.getDescription(),
                subTaskEpicId +
                        "," + task.getStartTime() +
                        "," + task.getDuration() +
                        "," + task.getEndTime() + "\n");
    }

    public static Task fromString(String result) {
        int id;
        type taskType;
        String name;
        StatusTypeEnum status;
        String description;
        Instant startTime;
        Instant endTime;
        long duration;

        String[] line = result.split(",");
        if (line[1].equals("TASK") || line[1].equals("EPIC") || line[1].equals("SUB_TASK")) {
            id = Integer.parseInt(line[0]);
            taskType = type.valueOf(line[1]);
            name = line[2];
            status = StatusTypeEnum.valueOf(line[3]);
            description = line[4];
            startTime = Instant.parse(line[6]);
            duration = Long.parseLong(line[7]);
            endTime = Instant.parse(line[8]);

            switch (taskType) {
                case EPIC:
                    Epic epic = new Epic(name, taskType, description, status, startTime, duration);
                    epic.setId(id);
                    epic.setEndTime(endTime);
                    return epic;
                case TASK:
                    Task task = new Task(name, taskType, description, status, startTime, duration);
                    task.setId(id);
                    return task;
                case SUB_TASK:
                    SubTask subTask = new SubTask(name, taskType, description, status, Integer.parseInt(line[5]), startTime, duration);
                    subTask.setId(id);
                    return subTask;
            }
        }
        return null;
    }
}
