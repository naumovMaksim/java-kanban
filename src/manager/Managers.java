package manager;

import manager.file.FileBackedTasksManager;
import manager.inMemory.*;
import manager.interfaces.HistoryManager;
import manager.http.HttpTaskManager;
import server.KVServer;

import java.io.IOException;


public class Managers {
    public static HttpTaskManager getDefault() {
        return new HttpTaskManager(KVServer.PORT);
    }

    public static KVServer getKVserver() throws IOException {
        return new KVServer();
    }

    public static FileBackedTasksManager FileBackedTasksManager() {
        return new FileBackedTasksManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
