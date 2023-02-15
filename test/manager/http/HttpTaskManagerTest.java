package manager.http;

import manager.Managers;
import manager.TaskManagerTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.KVServer;

import java.io.IOException;

class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {
    private KVServer kvServer;

    @BeforeEach
    void beforeEach() throws IOException {
        kvServer = Managers.getKVserver();
        kvServer.start();
        manager = Managers.getDefault();
        tasksCreation();
    }

    @AfterEach
    void afterEach() {
        kvServer.stop();
    }

    @Test
    void load() {
        manager.getTaskById(task.getId());
        manager.saveFile();
        manager.loadFile();
    }
}