package server;

import com.google.gson.Gson;
import manager.file.FileBackedTasksManager;

public class HttpTaskManager extends FileBackedTasksManager {

    private final KVTaskClient client;
    private final Gson gson;

    public HttpTaskManager(int port) {
        super();
        client = new KVTaskClient(port);
        gson = new Gson();
    }
}
