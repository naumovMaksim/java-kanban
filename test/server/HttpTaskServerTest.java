package server;

import adapter.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import manager.Managers;
import manager.file.FileBackedTasksManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.enums.StatusTypeEnum;
import tasks.enums.Type;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

    @Test
    void get() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        java.lang.reflect.Type tasksType = new TypeToken<ArrayList<Task>>() {
        }.getType();
        List<Task> list = gson.fromJson(response.body(), tasksType);
        assertNotNull(list);
        assertEquals(List.of(task), list);
    }

    @Test
    void getById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        java.lang.reflect.Type tasksType = new TypeToken<ArrayList<Task>>() {
        }.getType();
        List<Task> list = gson.fromJson(response.body(), tasksType);

        assertEquals(task, list.get(0));
    }
}