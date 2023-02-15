package server;

import adapter.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import manager.Managers;
import manager.file.FileBackedTasksManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {
    private final HttpServer httpServer;
    private final FileBackedTasksManager fileBackedTasksManager;
    private final Gson gson;

    public HttpTaskServer() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
        fileBackedTasksManager = Managers.FileBackedTasksManager();
        gson = new GsonBuilder().registerTypeAdapter(LocalDateTimeAdapter.class, new LocalDateTimeAdapter()).create();
        httpServer.createContext("/tasks", new Handler());
    }

    public void serverStart() {
        System.out.println("Запуск сервера...");
        httpServer.start();
    }

    public void serverStop() {
        httpServer.stop(1);
        System.out.println("Сервер остановлен.");
    }

    class Handler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) {
            System.out.println("Началась обработка запроса клиента");
            try {
                final String taskTypePath = exchange.getRequestURI().getPath().split("/")[2];
                switch (taskTypePath) {
                    case "task":
                        handleTask(exchange);
                        break;
                    case "subtask":
                        handleSubtask(exchange);
                        break;
                    case "epic":
                        handleEpic(exchange);
                        break;
                    case "history":
                        handleHistory(exchange);
                        break;
                    default:
                        System.out.println("Запрашиваемой задачи нет");
                }
            } catch (IOException e) {
                System.out.println("Ошибка при обработке запроса");
            }
        }

        private void handleTask(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            String body = new String(exchange.getRequestBody().readAllBytes(), Charset.defaultCharset());
            switch (method) {
                case "GET":
                    if (Objects.nonNull(exchange.getRequestURI().getQuery())) {
                        String idS = exchange.getRequestURI().getQuery().substring(3);
                        int id = Integer.parseInt(idS);
                        Task task1 = fileBackedTasksManager.getTaskById(id);
                        String response = gson.toJson(task1);
                        writeResponse(exchange, response);
                        return;
                    } else {
                        String response = gson.toJson(fileBackedTasksManager.getAllTasks());
                        writeResponse(exchange, response);
                        return;
                    }
                case "POST":
                    try {
                        Task task2 = gson.fromJson(body, Task.class);
                        fileBackedTasksManager.createTask(task2);
                        exchange.sendResponseHeaders(200, 0);
                    } catch (NullPointerException e) {
                        System.out.println(e.getMessage());
                    }
                    return;
                case "DELETE":
                    if (Objects.nonNull(exchange.getRequestURI().getQuery())) {
                        String idS = exchange.getRequestURI().getQuery().substring(3);
                        int id = Integer.parseInt(idS);
                        fileBackedTasksManager.deleteTaskById(id);
                        exchange.sendResponseHeaders(200, 0);
                    } else {
                        fileBackedTasksManager.deleteAllTasks();
                        exchange.sendResponseHeaders(200, 0);
                    }
            }
        }

        private void handleEpic(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            String body = new String(exchange.getRequestBody().readAllBytes(), Charset.defaultCharset());
            switch (method) {
                case "GET":
                    if (Objects.nonNull(exchange.getRequestURI().getQuery())) {
                        String idS = exchange.getRequestURI().getQuery().substring(3);
                        int id = Integer.parseInt(idS);
                        Epic epic = fileBackedTasksManager.getEpicById(id);
                        String response = gson.toJson(epic);
                        writeResponse(exchange, response);
                        return;
                    } else {
                        String response = gson.toJson(fileBackedTasksManager.getAllEpics());
                        writeResponse(exchange, response);
                        return;
                    }
                case "POST":
                    try {
                        Epic epic = gson.fromJson(body, Epic.class);
                        fileBackedTasksManager.createEpic(epic);
                        exchange.sendResponseHeaders(200, 0);
                    } catch (NullPointerException e) {
                        System.out.println(e.getMessage());
                    }
                    return;
                case "DELETE":
                    if (Objects.nonNull(exchange.getRequestURI().getQuery())) {
                        String idS = exchange.getRequestURI().getQuery().substring(3);
                        int id = Integer.parseInt(idS);
                        fileBackedTasksManager.deleteEpicById(id);
                        exchange.sendResponseHeaders(200, 0);
                    } else {
                        fileBackedTasksManager.deleteAllEpics();
                        exchange.sendResponseHeaders(200, 0);
                    }
            }
        }

        private void handleSubtask(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            String body = new String(exchange.getRequestBody().readAllBytes(), Charset.defaultCharset());
            switch (method) {
                case "GET":
                    if (Objects.nonNull(exchange.getRequestURI().getQuery())) {
                        String idS = exchange.getRequestURI().getQuery().substring(3);
                        int id = Integer.parseInt(idS);
                        SubTask subTask = fileBackedTasksManager.getSubTaskById(id);
                        String response = gson.toJson(subTask);
                        writeResponse(exchange, response);
                        return;
                    } else {
                        String response = gson.toJson(fileBackedTasksManager.getAllSubTasks());
                        writeResponse(exchange, response);
                        return;
                    }
                case "POST":
                    try {
                        SubTask subTask = gson.fromJson(body, SubTask.class);
                        fileBackedTasksManager.createSubTask(subTask);
                        exchange.sendResponseHeaders(200, 0);
                    } catch (NullPointerException e) {
                        System.out.println(e.getMessage());
                    }
                    return;
                case "DELETE":
                    if (Objects.nonNull(exchange.getRequestURI().getQuery())) {
                        String idS = exchange.getRequestURI().getQuery().substring(3);
                        int id = Integer.parseInt(idS);
                        fileBackedTasksManager.deleteSubTaskById(id);
                        exchange.sendResponseHeaders(200, 0);
                    } else {
                        fileBackedTasksManager.deleteAllTasks();
                        exchange.sendResponseHeaders(200, 0);
                    }
            }
        }

        protected void handleHistory(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            if (method.equals("GET")) {
                List<Task> history = fileBackedTasksManager.getHistory();
                String response = gson.toJson(history);
                exchange.sendResponseHeaders(200, 0);
                writeResponse(exchange, response);
            }
        }

        protected void writeResponse(HttpExchange h, String text) throws IOException {
            byte[] resp = text.getBytes(UTF_8);
            h.getResponseHeaders().add("Content-Type", "application/json");
            h.sendResponseHeaders(200, resp.length);
            h.getResponseBody().write(resp);
        }
    }
}
