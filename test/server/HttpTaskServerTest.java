package server;

import adapter.LocalDateTimeAdapter;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import exception.KvManagerSaveException;
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

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {
    HttpTaskServer server;
    Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
    Task task;
    Epic epic;
    SubTask subTask;
    FileBackedTasksManager manager;

    @BeforeEach
    void beforeEach() throws IOException {
        manager = Managers.FileBackedTasksManager();
        server = new HttpTaskServer(manager);

        task = new Task("Task", Type.TASK, "description", StatusTypeEnum.NEW,
                LocalDateTime.of(2023, 2, 3, 6, 0), 1);
        epic = new Epic("Epic", Type.EPIC, "description", StatusTypeEnum.NEW,
                LocalDateTime.of(2023, 2, 3, 7, 0), 1);
        subTask = new SubTask("SubTask", Type.SUB_TASK, "description", StatusTypeEnum.NEW, 2,
                LocalDateTime.of(2023, 2, 3, 8, 0), 1);

        manager.createTask(task);
        manager.createEpic(epic);
        manager.createSubTask(subTask);

        server.serverStart();
    }

    @AfterEach
    void afterEach() {
        server.serverStop();
    }

    @Test
    void GETAllTasks() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            JsonElement json = JsonParser.parseString(response.body());
            List<Task> taskList = gson.fromJson(json, new TypeToken<ArrayList<Task>>(){}.getType());
            assertEquals(task, taskList.get(0));
        } catch (IOException | InterruptedException e) {
            throw new KvManagerSaveException(e);
        }
    }

    @Test
    void GETAllTasksWithWrongData() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/gg");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(404, response.statusCode());
        } catch (IOException | InterruptedException e) {
            throw new KvManagerSaveException(e);
        }
    }

    @Test
    void GETAllEpics() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            JsonElement json = JsonParser.parseString(response.body());
            List<Epic> taskList = gson.fromJson(json, new TypeToken<ArrayList<Epic>>(){}.getType());
            assertEquals(epic, taskList.get(0));
            assertEquals(epic.getSubtaskIds(), taskList.get(0).getSubtaskIds());
        } catch (IOException | InterruptedException e) {
            throw new KvManagerSaveException(e);
        }
    }

    @Test
    void GETAllEpicsWithWrongData() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/gg");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(404, response.statusCode());
        } catch (IOException | InterruptedException e) {
            throw new KvManagerSaveException(e);
        }
    }

    @Test
    void GETAllSubTasks() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            JsonElement json = JsonParser.parseString(response.body());
            List<SubTask> taskList = gson.fromJson(json, new TypeToken<ArrayList<SubTask>>(){}.getType());
            assertEquals(subTask, taskList.get(0));
            assertEquals(subTask.getEpicId(), taskList.get(0).getEpicId());
        } catch (IOException | InterruptedException e) {
            throw new KvManagerSaveException(e);
        }
    }

    @Test
    void GETAllSubTasksWithWrongData() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/gg");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(404, response.statusCode());
        } catch (IOException | InterruptedException e) {
            throw new KvManagerSaveException(e);
        }
    }

    @Test
    void GETTask() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task?id=1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            JsonElement json = JsonParser.parseString(response.body());
            Task task1 = gson.fromJson(json, Task.class);
            assertEquals(task, task1);
        } catch (IOException | InterruptedException e) {
            throw new KvManagerSaveException(e);
        }
    }

    @Test
    void GETTaskWithWrongData() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task?id=10");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(404, response.statusCode());
        } catch (IOException | InterruptedException e) {
            throw new KvManagerSaveException(e);
        }
    }

    @Test
    void GETEpic() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic?id=2");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            JsonElement json = JsonParser.parseString(response.body());
            Epic epic1 = gson.fromJson(json, Epic.class);
            assertEquals(epic, epic1);
            assertEquals(epic.getSubtaskIds(), epic1.getSubtaskIds());
        } catch (IOException | InterruptedException e) {
            throw new KvManagerSaveException(e);
        }
    }

    @Test
    void GETEpicWithWrongData() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic?id=10");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(404, response.statusCode());
        } catch (IOException | InterruptedException e) {
            throw new KvManagerSaveException(e);
        }
    }

    @Test
    void GETSubTask() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask?id=3");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            JsonElement json = JsonParser.parseString(response.body());
            SubTask subTask1 = gson.fromJson(json, SubTask.class);
            assertEquals(subTask, subTask1);
            assertEquals(subTask.getEpicId(), subTask1.getEpicId());
        } catch (IOException | InterruptedException e) {
            throw new KvManagerSaveException(e);
        }
    }

    @Test
    void GETSubTaskWithWrongData() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask?id=10");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(404, response.statusCode());
        } catch (IOException | InterruptedException e) {
            throw new KvManagerSaveException(e);
        }
    }

    @Test
    void POSTTask() {
        Task task1 = new Task("Task1", Type.TASK, "description", StatusTypeEnum.NEW,
                LocalDateTime.of(2023, 3, 3, 6, 0), 1);
        manager.createTask(task1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task");
        String jsonString = gson.toJson(task1);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                .build();

        HttpClient client1 = HttpClient.newHttpClient();
        URI url1 = URI.create("http://localhost:8080/tasks/task?id=4");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url1)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());

            HttpResponse<String> response1 = client1.send(request1, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response1.statusCode());
            JsonElement json = JsonParser.parseString(response1.body());
            Task task2 = gson.fromJson(json, Task.class);
            assertEquals(task1, task2);
        } catch (IOException | InterruptedException e) {
            throw new KvManagerSaveException(e);
        }
    }

    @Test
    void POSTTaskWithWrongData() {
        Task task1 = new Task("Task1", Type.TASK, "description", StatusTypeEnum.NEW,
                LocalDateTime.of(2023, 3, 3, 6, 0), 1);
        manager.createTask(task1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task");
        String jsonString = gson.toJson(null);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                .build();

        final IOException exception = assertThrows(IOException.class,
                () -> {
                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    assertEquals(404, response.statusCode());
                });
        assertEquals("HTTP/1.1 header parser received no bytes", exception.getMessage());
    }

    @Test
    void POSTEpic() {
        Epic epic1 = new Epic("Epic1", Type.EPIC, "description", StatusTypeEnum.NEW,
                LocalDateTime.of(2023, 3, 3, 6, 0), 1);
        manager.createEpic(epic1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic");
        String jsonString = gson.toJson(epic1);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                .build();

        HttpClient client1 = HttpClient.newHttpClient();
        URI url1 = URI.create("http://localhost:8080/tasks/epic?id=4");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url1)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());

            HttpResponse<String> response1 = client1.send(request1, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response1.statusCode());
            JsonElement json = JsonParser.parseString(response1.body());
            Epic epic2 = gson.fromJson(json, Epic.class);
            assertEquals(epic1, epic2);
        } catch (IOException | InterruptedException e) {
            throw new KvManagerSaveException(e);
        }
    }

    @Test
    void POSTEpicWithWrongData() {
        Epic epic1 = new Epic("Epic1", Type.EPIC, "description", StatusTypeEnum.NEW,
                LocalDateTime.of(2023, 3, 3, 6, 0), 1);
        manager.createEpic(epic1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic");
        String jsonString = gson.toJson(null);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                .build();

        final IOException exception = assertThrows(IOException.class,
                () -> {
                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    assertEquals(404, response.statusCode());
                });
        assertEquals("HTTP/1.1 header parser received no bytes", exception.getMessage());
    }

    @Test
    void POSTSubtask() {
        SubTask subTask1 = new SubTask("SubTask1", Type.SUB_TASK, "description", StatusTypeEnum.NEW, 2,
                LocalDateTime.of(2023, 3, 3, 6, 0), 1);
        manager.createSubTask(subTask1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        String jsonString = gson.toJson(subTask1);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                .build();

        HttpClient client1 = HttpClient.newHttpClient();
        URI url1 = URI.create("http://localhost:8080/tasks/subtask?id=4");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url1)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());

            HttpResponse<String> response1 = client1.send(request1, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response1.statusCode());
            JsonElement json = JsonParser.parseString(response1.body());
            SubTask subTask2 = gson.fromJson(json, SubTask.class);
            assertEquals(subTask1, subTask2);
        } catch (IOException | InterruptedException e) {
            throw new KvManagerSaveException(e);
        }
    }

    @Test
    void POSTSubTaskWithWrongData() {
        Epic epic1 = new Epic("Epic1", Type.EPIC, "description", StatusTypeEnum.NEW,
                LocalDateTime.of(2023, 3, 3, 6, 0), 1);
        manager.createEpic(epic1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        String jsonString = gson.toJson(null);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                .build();

        final IOException exception = assertThrows(IOException.class,
                () -> {
                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    assertEquals(404, response.statusCode());
                });
        assertEquals("HTTP/1.1 header parser received no bytes", exception.getMessage());
    }

    @Test
    void DELETEAllTasks() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpClient client1 = HttpClient.newHttpClient();
        URI url1 = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url1)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());

            HttpResponse<String> response1 = client1.send(request1, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response1.statusCode());
            JsonElement json = JsonParser.parseString(response1.body());
            List<Task> taskList = gson.fromJson(json, new TypeToken<ArrayList<Task>>(){}.getType());
            assertEquals(Collections.emptyList(), taskList);
        } catch (IOException | InterruptedException e) {
            throw new KvManagerSaveException(e);
        }
    }

    @Test
    void DELETEAllTasksWithWrongData() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/gg");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpClient client1 = HttpClient.newHttpClient();
        URI url1 = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url1)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(404, response.statusCode());

            HttpResponse<String> response1 = client1.send(request1, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response1.statusCode());
            JsonElement json = JsonParser.parseString(response1.body());
            List<Task> taskList = gson.fromJson(json, new TypeToken<ArrayList<Task>>(){}.getType());
            assertEquals(List.of(task), taskList);
        } catch (IOException | InterruptedException e) {
            throw new KvManagerSaveException(e);
        }
    }

    @Test
    void DELETEAllEpics() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpClient client1 = HttpClient.newHttpClient();
        URI url1 = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url1)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());

            HttpResponse<String> response1 = client1.send(request1, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response1.statusCode());
            JsonElement json = JsonParser.parseString(response1.body());
            List<Epic> taskList = gson.fromJson(json, new TypeToken<ArrayList<Epic>>(){}.getType());
            assertEquals(Collections.emptyList(), taskList);
        } catch (IOException | InterruptedException e) {
            throw new KvManagerSaveException(e);
        }
    }

    @Test
    void DELETEAllEpicsWithWrongData() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/gg");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpClient client1 = HttpClient.newHttpClient();
        URI url1 = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url1)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(404, response.statusCode());

            HttpResponse<String> response1 = client1.send(request1, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response1.statusCode());
            JsonElement json = JsonParser.parseString(response1.body());
            List<Epic> taskList = gson.fromJson(json, new TypeToken<ArrayList<Epic>>(){}.getType());
            assertEquals(List.of(epic), taskList);
        } catch (IOException | InterruptedException e) {
            throw new KvManagerSaveException(e);
        }
    }

    @Test
    void DELETEAllSubTasks() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpClient client1 = HttpClient.newHttpClient();
        URI url1 = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url1)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());

            HttpResponse<String> response1 = client1.send(request1, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response1.statusCode());
            JsonElement json = JsonParser.parseString(response1.body());
            List<SubTask> taskList = gson.fromJson(json, new TypeToken<ArrayList<SubTask>>(){}.getType());
            assertEquals(Collections.emptyList(), taskList);
        } catch (IOException | InterruptedException e) {
            throw new KvManagerSaveException(e);
        }
    }

    @Test
    void DELETEAllSubTasksWithWrongData() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/gg");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpClient client1 = HttpClient.newHttpClient();
        URI url1 = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url1)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(404, response.statusCode());

            HttpResponse<String> response1 = client1.send(request1, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response1.statusCode());
            JsonElement json = JsonParser.parseString(response1.body());
            List<SubTask> taskList = gson.fromJson(json, new TypeToken<ArrayList<SubTask>>(){}.getType());
            assertEquals(List.of(subTask), taskList);
        } catch (IOException | InterruptedException e) {
            throw new KvManagerSaveException(e);
        }
    }

    @Test
    void DELETETask() {
        Task task1 = new Task("Task1", Type.TASK, "description", StatusTypeEnum.NEW,
                LocalDateTime.of(2023, 3, 3, 6, 0), 1);
        manager.createTask(task1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task?id=1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpClient client1 = HttpClient.newHttpClient();
        URI url1 = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url1)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());

            HttpResponse<String> response1 = client1.send(request1, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response1.statusCode());
            JsonElement json = JsonParser.parseString(response1.body());
            List<Task> taskList = gson.fromJson(json, new TypeToken<ArrayList<Task>>(){}.getType());
            assertEquals(1, taskList.size());
            assertEquals(task1, taskList.get(0));
        } catch (IOException | InterruptedException e) {
            throw new KvManagerSaveException(e);
        }
    }

    @Test
    void DELETETaskWithWrongData() {
        Task task1 = new Task("Task1", Type.TASK, "description", StatusTypeEnum.NEW,
                LocalDateTime.of(2023, 3, 3, 6, 0), 1);
        manager.createTask(task1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task?id=10");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpClient client1 = HttpClient.newHttpClient();
        URI url1 = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url1)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(404, response.statusCode());

            HttpResponse<String> response1 = client1.send(request1, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response1.statusCode());
            JsonElement json = JsonParser.parseString(response1.body());
            List<Task> taskList = gson.fromJson(json, new TypeToken<ArrayList<Task>>(){}.getType());
            assertEquals(2, taskList.size());
            assertEquals(List.of(task, task1), taskList);
        } catch (IOException | InterruptedException e) {
            throw new KvManagerSaveException(e);
        }
    }

    @Test
    void DELETEEpic() {
        Epic epic1 = new Epic("Epic1", Type.EPIC, "description", StatusTypeEnum.NEW,
                LocalDateTime.of(2023, 3, 3, 6, 0), 1);
        manager.createEpic(epic1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic?id=2");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpClient client1 = HttpClient.newHttpClient();
        URI url1 = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url1)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());

            HttpResponse<String> response1 = client1.send(request1, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response1.statusCode());
            JsonElement json = JsonParser.parseString(response1.body());
            List<Epic> taskList = gson.fromJson(json, new TypeToken<ArrayList<Epic>>(){}.getType());
            assertEquals(1, taskList.size());
            assertEquals(epic1, taskList.get(0));
        } catch (IOException | InterruptedException e) {
            throw new KvManagerSaveException(e);
        }
    }

    @Test
    void DELETEEpicWithWrongData() {
        Epic epic1 = new Epic("Epic1", Type.EPIC, "description", StatusTypeEnum.NEW,
                LocalDateTime.of(2023, 3, 3, 6, 0), 1);
        manager.createEpic(epic1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic?id=10");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpClient client1 = HttpClient.newHttpClient();
        URI url1 = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url1)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(404, response.statusCode());

            HttpResponse<String> response1 = client1.send(request1, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response1.statusCode());
            JsonElement json = JsonParser.parseString(response1.body());
            List<Epic> taskList = gson.fromJson(json, new TypeToken<ArrayList<Epic>>(){}.getType());
            assertEquals(2, taskList.size());
            assertEquals(List.of(epic, epic1), taskList);
        } catch (IOException | InterruptedException e) {
            throw new KvManagerSaveException(e);
        }
    }

    @Test
    void DELETESubTask() {
        SubTask subTask1 = new SubTask("SubTask1", Type.SUB_TASK, "description", StatusTypeEnum.NEW, 2,
                LocalDateTime.of(2023, 3, 3, 6, 0), 1);
        manager.createSubTask(subTask1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask?id=3");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpClient client1 = HttpClient.newHttpClient();
        URI url1 = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url1)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());

            HttpResponse<String> response1 = client1.send(request1, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response1.statusCode());
            JsonElement json = JsonParser.parseString(response1.body());
            List<SubTask> taskList = gson.fromJson(json, new TypeToken<ArrayList<SubTask>>(){}.getType());
            assertEquals(1, taskList.size());
            assertEquals(subTask1, taskList.get(0));
        } catch (IOException | InterruptedException e) {
            throw new KvManagerSaveException(e);
        }
    }

    @Test
    void DELETESubTaskWithWrongData() {
        SubTask subTask1 = new SubTask("SubTask1", Type.SUB_TASK, "description", StatusTypeEnum.NEW, 2,
                LocalDateTime.of(2023, 3, 3, 6, 0), 1);
        manager.createSubTask(subTask1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask?id=10");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpClient client1 = HttpClient.newHttpClient();
        URI url1 = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url1)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(404, response.statusCode());

            HttpResponse<String> response1 = client1.send(request1, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response1.statusCode());
            JsonElement json = JsonParser.parseString(response1.body());
            List<SubTask> taskList = gson.fromJson(json, new TypeToken<ArrayList<SubTask>>(){}.getType());
            assertEquals(2, taskList.size());
            assertEquals(List.of(subTask, subTask1), taskList);
        } catch (IOException | InterruptedException e) {
            throw new KvManagerSaveException(e);
        }
    }
}