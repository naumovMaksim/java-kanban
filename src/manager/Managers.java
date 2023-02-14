package manager;

import manager.file.FileBackedTasksManager;
import manager.inMemory.*;
import manager.interfaces.HistoryManager;
import manager.interfaces.TaskManager;
import server.HttpTaskManager;
import server.KVServer;

import java.io.File;


public class Managers {
    public static TaskManager getDefault() {
        return new HttpTaskManager(KVServer.PORT);
        //return new FileBackedTasksManager(new File("resources/testReport.csv"));
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
