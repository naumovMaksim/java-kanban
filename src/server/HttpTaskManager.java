package server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import manager.file.FileBackedTasksManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

public class HttpTaskManager extends FileBackedTasksManager {

    private static KVTaskClient client;
    private static Gson gson;

    public HttpTaskManager(int port) {
        client = new KVTaskClient(port);
        gson = new Gson();
    }

    public void loadFile() {
        ArrayList<Task> tasks = gson.fromJson(client.load("tasks"), (Type) Task.class);
        if (Objects.nonNull(tasks)) {
            for (Task task : tasks) {
                int id = task.getId();
                this.tasksMemory.put(id, task);
                this.prioritizedTasks.add(task);
                if (id > this.id) {
                    this.id = id;
                }
            }
        }
        ArrayList<Epic> epics = gson.fromJson(client.load("tasks"), (Type) Epic.class);
        if (Objects.nonNull(epics)) {
            for (Epic epic : epics) {
                int id = epic.getId();
                this.epicsMemory.put(id, epic);
                if (id > this.id) {
                    this.id = id;
                }
            }
        }
        ArrayList<SubTask> subTasks = gson.fromJson(client.load("tasks"), (Type) SubTask.class);
        if (Objects.nonNull(subTasks)) {
            for (SubTask subTask : subTasks) {
                int id = subTask.getId();
                this.subTasksMemory.put(id, subTask);
                this.prioritizedTasks.add(subTask);
                if (id > this.id) {
                    this.id = id;
                }
            }
        }
    }

    public void saveFile() {
        String tasksJson = gson.toJson(getAllTasks());
        client.put("tasks", tasksJson);

        String epicsJson = gson.toJson(getAllEpics());
        client.put("epics", epicsJson);

        String subtasksJson = gson.toJson(getAllSubTasks());
        client.put("subtasks", subtasksJson);

        String historyJson = gson.toJson(getHistory());
        client.put("history", historyJson);
    }
}
