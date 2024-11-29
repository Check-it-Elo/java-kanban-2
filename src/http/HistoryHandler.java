package http;

import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpHandler;
import model.Task;
import service.TaskManager;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;

    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    public HistoryHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            handleGet(exchange);
        } else {
            sendError(exchange);
        }
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        List<Task> history = taskManager.getHistory();
        String jsonResponse = gson.toJson(history);
        sendText(exchange, jsonResponse);
    }
}