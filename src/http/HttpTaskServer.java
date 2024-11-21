package http;

import com.sun.net.httpserver.HttpServer;
import service.InMemoryTaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {

    private static final int PORT = 8080;
    private final InMemoryTaskManager taskManager;

    public HttpTaskServer(InMemoryTaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public void start() {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

            // Регистрируем обработчики
            server.createContext("/tasks", new TaskHandler(taskManager));
            server.createContext("/subtasks", new SubtaskHandler(taskManager));
            server.createContext("/epics", new EpicHandler(taskManager));
            server.createContext("/history", new HistoryHandler(taskManager));
            server.createContext("/prioritized", new PrioritizedHandler(taskManager));

            server.setExecutor(null);
            server.start();

            System.out.println("Server started on port " + PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);

        httpTaskServer.start();
    }
}