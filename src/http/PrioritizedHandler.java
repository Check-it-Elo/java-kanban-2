package http;

import com.sun.net.httpserver.HttpExchange;
import com.google.gson.Gson;
import model.Task;
import service.InMemoryTaskManager;

import java.io.IOException;
import java.util.TreeSet;

public class PrioritizedHandler extends BaseHttpHandler {

    private final InMemoryTaskManager taskManager;
    private final Gson gson;

    public PrioritizedHandler(InMemoryTaskManager taskManager) {
        this.taskManager = taskManager;
        this.gson = new Gson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            TreeSet<Task> prioritizedTasks = taskManager.getPrioritizedTasks();

            String jsonResponse = gson.toJson(prioritizedTasks);
            sendText(exchange, jsonResponse, 200);
        } else {
            sendNotFound(exchange);
        }
    }
}