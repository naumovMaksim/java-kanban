package server;

import adapter.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import manager.Managers;
import manager.file.FileBackedTasksManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.enums.StatusTypeEnum;
import tasks.enums.Type;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {
    HttpTaskServer server;
    Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTimeAdapter.class, new LocalDateTimeAdapter()).create();
    Task task;
    Epic epic;
    SubTask subTask;
    FileBackedTasksManager fileBackedTasksManager;

    @BeforeEach
    void beforeEach() throws IOException {
        server = new HttpTaskServer();
        fileBackedTasksManager = Managers.FileBackedTasksManager();

        task = new Task("Task", Type.TASK, "description", StatusTypeEnum.NEW,
                LocalDateTime.of(2023, 2, 3, 6, 0), 1);
        epic = new Epic("Epic", Type.EPIC, "description", StatusTypeEnum.NEW,
                LocalDateTime.of(2023, 2, 3, 7, 0), 1);
        subTask = new SubTask("SubTask", Type.SUB_TASK, "description", StatusTypeEnum.NEW, 2,
                LocalDateTime.of(2023, 2, 3, 8, 0), 1);

        fileBackedTasksManager.createTask(task);
        fileBackedTasksManager.createEpic(epic);
        fileBackedTasksManager.createSubTask(subTask);

        server.serverStart();
    }

    @AfterEach
    void afterEach() {
        server.serverStop();
    }


}