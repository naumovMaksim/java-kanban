package manager.file;

import manager.interfaces.HistoryManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.enums.StatusTypeEnum;
import tasks.enums.Type;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CSVTaskFormat {
    static DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
//    DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
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

        if (task.getType().equals(Type.SUB_TASK)) {
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
        Type taskType;
        String name;
        StatusTypeEnum status;
        String description;
        LocalDateTime startTime;
        LocalDateTime endTime;
        long duration;

        String[] line = result.split(",");
        if (line[1].equals("TASK") || line[1].equals("EPIC") || line[1].equals("SUB_TASK")) {
            id = Integer.parseInt(line[0]);
            taskType = Type.valueOf(line[1]);
            name = line[2];
            status = StatusTypeEnum.valueOf(line[3]);
            description = line[4];
            startTime = LocalDateTime.parse(line[6], formatter);
            duration = Long.parseLong(line[7]);
            endTime = LocalDateTime.parse(line[8], formatter);

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
