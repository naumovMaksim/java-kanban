package server;

import adapter.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import manager.file.FileBackedTasksManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {
    private final HttpServer httpServer;
    private final FileBackedTasksManager tasksManager;
    private final Gson gson;

    public HttpTaskServer(FileBackedTasksManager manager) throws IOException {
        tasksManager = manager;
        httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
        gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
        httpServer.createContext("/tasks", this::handler);
    }

    public void serverStart() {
        System.out.println("Запуск сервера...");
        httpServer.start();
    }

    public void serverStop() {
        httpServer.stop(1);
        System.out.println("Сервер остановлен.");
    }

    private void handleTask(HttpExchange h) throws IOException {
        final String query = h.getRequestURI().getQuery();
        switch (h.getRequestMethod()) {
            case "GET" -> {
                if (query == null) {
                    final List<Task> tasks = tasksManager.getAllTasks();
                    final String response = gson.toJson(tasks);
                    System.out.println("Получили все задачи");
                    sendText(h, response);
                    return;
                }
                String idParam = query.substring(3);
                final int id = Integer.parseInt(idParam);
                if (tasksManager.getTaskById(id) != null) {
                    final Task task = tasksManager.getTaskById(id);
                    final String response = gson.toJson(task);
                    System.out.println("Получили задачу id=" + id);
                    sendText(h, response);
                } else {
                    System.out.println("Id=" + id + " не найден");
                    h.sendResponseHeaders(404, 0);
                }
            }
            case "DELETE" -> {
                if (query == null) {
                    tasksManager.deleteAllTasks();
                    System.out.println("Удалили все задачи");
                    h.sendResponseHeaders(200, 0);
                    return;
                }
                String idParam = query.substring(3);
                final int id = Integer.parseInt(idParam);
                if (tasksManager.getTaskById(id) != null) {
                    tasksManager.deleteTaskById(id);
                    System.out.println("Удалили задачу id=" + id);
                    h.sendResponseHeaders(200, 0);
                } else {
                    System.out.println("Id=" + id + " не найден");
                    h.sendResponseHeaders(404, 0);
                }
            }
            case "POST" -> {
                String json = readText(h);
                if (json.isEmpty()) {
                    System.out.println("Body c задачей  пустой. указывается в теле запроса");
                    h.sendResponseHeaders(400, 0);
                    return;
                }
                final Task task = gson.fromJson(json, Task.class);
                final Integer id = task.getId();
                if (id != null) {
                    tasksManager.updateTask(task);
                    System.out.println("Обновили задачу id=" + id);
                    h.sendResponseHeaders(200, 0);
                } else {
                    tasksManager.createTask(task);
                    System.out.println("Создали задачу id=" + id);
                    final String response = gson.toJson(task);
                    sendText(h, response);
                }
            }
            default -> {
                System.out.println("/task получил: " + h.getRequestMethod());
                h.sendResponseHeaders(405, 0);
            }
        }
    }

    private void handleEpic(HttpExchange h) throws IOException {
        final String query = h.getRequestURI().getQuery();
        switch (h.getRequestMethod()) {
            case "GET" -> {
                if (query == null) {
                    final List<Epic> tasks = tasksManager.getAllEpics();
                    final String response = gson.toJson(tasks);
                    System.out.println("Получили все эпики");
                    sendText(h, response);
                    return;
                }
                String idParam = query.substring(3);
                final int id = Integer.parseInt(idParam);
                if (tasksManager.getEpicById(id) != null) {
                    final Task task = tasksManager.getEpicById(id);
                    final String response = gson.toJson(task);
                    System.out.println("Получили эпик id=" + id);
                    sendText(h, response);
                } else {
                    System.out.println("Id=" + id + " не найден");
                    h.sendResponseHeaders(404, 0);
                }
            }
            case "DELETE" -> {
                if (query == null) {
                    tasksManager.deleteAllEpics();
                    System.out.println("Удалили все эпики");
                    h.sendResponseHeaders(200, 0);
                    return;
                }
                String idParam = query.substring(3);
                final int id = Integer.parseInt(idParam);
                if (tasksManager.getEpicById(id) != null) {
                    tasksManager.deleteEpicById(id);
                    System.out.println("Удалили эпик id=" + id);
                    h.sendResponseHeaders(200, 0);
                } else {
                    System.out.println("Id=" + id + " не найден");
                    h.sendResponseHeaders(404, 0);
                }
            }
            case "POST" -> {
                String json = readText(h);
                if (json.isEmpty()) {
                    System.out.println("Body c эпиком  пустой. указывается в теле запроса");
                    h.sendResponseHeaders(400, 0);
                    return;
                }
                final Epic task = gson.fromJson(json, Epic.class);
                final Integer id = task.getId();
                if (id != null) {
                    tasksManager.updateEpic(task);
                    System.out.println("Обновили эпик id=" + id);
                    h.sendResponseHeaders(200, 0);
                } else {
                    tasksManager.createEpic(task);
                    System.out.println("Создали эпик id=" + id);
                    final String response = gson.toJson(task);
                    sendText(h, response);
                }
            }
            default -> {
                System.out.println("/epic получил: " + h.getRequestMethod());
                h.sendResponseHeaders(405, 0);
            }
        }
    }

    private void handleSubtask(HttpExchange h) throws IOException {
        final String query = h.getRequestURI().getQuery();
        switch (h.getRequestMethod()) {
            case "GET" -> {
                if (query == null) {
                    final List<SubTask> tasks = tasksManager.getAllSubTasks();
                    final String response = gson.toJson(tasks);
                    System.out.println("Получили все подзадачи");
                    sendText(h, response);
                    return;
                }
                String idParam = query.substring(3);
                final int id = Integer.parseInt(idParam);
                if (tasksManager.getSubTaskById(id) != null) {
                    final Task task = tasksManager.getSubTaskById(id);
                    final String response = gson.toJson(task);
                    System.out.println("Получили подзадачу id=" + id);
                    sendText(h, response);
                } else {
                    System.out.println("Id=" + id + " не найден");
                    h.sendResponseHeaders(404, 0);
                }
            }
            case "DELETE" -> {
                if (query == null) {
                    tasksManager.deleteAllSubTasks();
                    System.out.println("Удалили все подзадачи");
                    h.sendResponseHeaders(200, 0);
                    return;
                }
                String idParam = query.substring(3);
                final int id = Integer.parseInt(idParam);
                if (tasksManager.getSubTaskById(id) != null) {
                    tasksManager.deleteSubTaskById(id);
                    System.out.println("Удалили подзадачу id=" + id);
                    h.sendResponseHeaders(200, 0);
                } else {
                    System.out.println("Id=" + id + " не найден");
                    h.sendResponseHeaders(404, 0);
                }
            }
            case "POST" -> {
                String json = readText(h);
                if (json.isEmpty()) {
                    System.out.println("Body c задачей  пустой. указывается в теле запроса");
                    h.sendResponseHeaders(400, 0);
                    return;
                }
                final SubTask task = gson.fromJson(json, SubTask.class);
                final Integer id = task.getId();
                if (id != null) {
                    tasksManager.updateSubTask(task);
                    System.out.println("Обновили подзадачу id=" + id);
                    h.sendResponseHeaders(200, 0);
                } else {
                    tasksManager.createSubTask(task);
                    System.out.println("Создали подзадачу id=" + id);
                    final String response = gson.toJson(task);
                    sendText(h, response);
                }
            }
            default -> {
                System.out.println("/subtask получил: " + h.getRequestMethod());
                h.sendResponseHeaders(405, 0);
            }
        }
    }

    public void handler(HttpExchange h) {
        try (h) {
            System.out.println("\n/tasks: " + h.getRequestURI());
            final String path = h.getRequestURI().getPath().substring(7);
            switch (path) {
                case "" -> {
                    if (!h.getRequestMethod().equals("GET")) {
                        System.out.println("/ Ждёт GET-запрос, а получил: " + h.getRequestMethod());
                        h.sendResponseHeaders(405, 0);
                    }
                    final String response = gson.toJson(tasksManager.getprioritizedTaskss());
                    sendText(h, response);
                }
                case "history" -> {
                    if (!h.getRequestMethod().equals("GET")) {
                        System.out.println("/history ждёт GET-запрос, а получил: " + h.getRequestMethod());
                        h.sendResponseHeaders(405, 0);
                    }
                    final String response = gson.toJson(tasksManager.getHistory());
                    sendText(h, response);
                }
                case "task" -> handleTask(h);
                case "subtask" -> handleSubtask(h);
                case "subtask/epic" -> {
                    if (!h.getRequestMethod().equals("GET")) {
                        System.out.println("/subtask/epic ждёт GET-запрос, а получил: " + h.getRequestMethod());
                        h.sendResponseHeaders(405, 0);
                    }
                    final String query = h.getRequestURI().getQuery();
                    String idParam = query.substring(3);// ?id=
                    final int id = Integer.parseInt(idParam);
                    Epic epic = tasksManager.getEpicById(id);
                    final List<SubTask> subtasks = tasksManager.getAllSubTasksInEpic(epic);
                    final String response = gson.toJson(subtasks);
                    System.out.println("Получили подзадачи эпика id=" + id);
                    sendText(h, response);
                }
                case "epic" -> handleEpic(h);
                default -> {
                    System.out.println("Неизвестный зарос: " + h.getRequestURI());
                    h.sendResponseHeaders(404, 0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }

    private String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
    }
}

