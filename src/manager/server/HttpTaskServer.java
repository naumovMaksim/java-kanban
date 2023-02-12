package manager.server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import manager.Managers;
import manager.file.FileBackedTasksManager;
import tasks.Task;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.logging.Handler;

public class HttpTaskServer {
    private final HttpServer httpServer;
    private static FileBackedTasksManager fileBackedTasksManager;
    private static Gson gson;

    public HttpTaskServer() throws IOException {
        this.httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
        fileBackedTasksManager = (FileBackedTasksManager) Managers.getDefault();
        gson = new Gson();
        httpServer.createContext("/tasks", new Handler());
    }

    static class Handler implements HttpHandler {

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
                    default:
                        System.out.println("Запрашиваемой задачи нет");
                }
            } catch (IOException e) {
                System.out.println("Ошибка при обработке запроса");
            } finally {
                exchange.close();
            }
        }

        private void handleTask(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            String body = new String(exchange.getRequestBody().readAllBytes(), Charset.defaultCharset());
            final String taskPath = exchange.getRequestURI().getPath().split("/")[2];
            final Optional<String> task = Optional.of(taskPath);
            switch (method) {
                case "GET":
                    String idS = exchange.getRequestURI().getQuery().substring(3);
                    int id = Integer.parseInt(idS);
                    Task task1 = fileBackedTasksManager.getTaskById(id);
                    String response = gson.toJson(task1);
                    // sendtext
            }
        }

        private void handleEpic(HttpExchange exchange) throws IOException {

        }

        private void handleSubtask(HttpExchange exchange) throws IOException {

        }

        protected void write(HttpExchange exchange, String text) throws IOException{
            exchange.getResponseHeaders().add("content-type", "application/json");
            exchange.sendResponseHeaders(200, 0);
            exchange.getRequestBody().
        }
    }
}
