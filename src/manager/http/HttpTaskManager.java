package manager.http;

import adapter.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import manager.file.FileBackedTasksManager;
import manager.client.KVTaskClient;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HttpTaskManager extends FileBackedTasksManager {

    private static KVTaskClient client;
    private final Gson gson;

    public HttpTaskManager(int port) {
        client = new KVTaskClient(port);
        gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
    }

    public void loadFile() {
        ArrayList<Task> tasks = gson.fromJson(client.load("tasks"), new TypeToken<ArrayList<Task>>() {
        }.getType());
        addTasks(tasks);
        ArrayList<Epic> epics = gson.fromJson(client.load("epics"), new TypeToken<ArrayList<Epic>>() {
        }.getType());
        addTasks(epics);
        ArrayList<SubTask> subtasks = gson.fromJson(client.load("subtasks"), new TypeToken<ArrayList<SubTask>>() {
        }.getType());
        addTasks(subtasks);
        List<Integer> history = gson.fromJson(client.load("history"), new TypeToken<ArrayList<Integer>>() {
        }.getType());
        for (Integer taskId : history) {
            getHistoryManager().add(findTask(taskId));
        }
    }

    public void saveFile() {
        String tasksJson = gson.toJson(getAllTasks());
        client.put("tasks", tasksJson);

        String epicsJson = gson.toJson(getAllEpics());
        client.put("epics", epicsJson);

        String subtasksJson = gson.toJson(getAllSubTasks());
        client.put("subtasks", subtasksJson);

        String historyJson = gson.toJson(getHistoryManager().getHistory().stream()
                .map(Task::getId).collect(Collectors.toList()));
        client.put("history", historyJson);
    }
}
