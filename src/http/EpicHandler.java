package http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Epic;
import service.TaskManager;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;

    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    public EpicHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        switch (exchange.getRequestMethod()) {
            case "GET":
                handleGet(exchange);
                break;
            case "POST":
                handlePost(exchange);
                break;
            case "DELETE":
                handleDelete(exchange);
                break;
            default:
                sendError(exchange);
                break;
        }
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        List<Epic> epics = taskManager.getAllEpics();
        String jsonResponse = gson.toJson(epics);
        sendText(exchange, jsonResponse);
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
        Epic epic = gson.fromJson(reader, Epic.class);
        taskManager.addEpic(epic);
        sendText(exchange, gson.toJson(epic));
    }

    private void handleDelete(HttpExchange exchange) throws IOException {
        String id = exchange.getRequestURI().getPath().split("/")[2];
        int epicId = Integer.parseInt(id);
        taskManager.deleteEpicById(epicId);
        sendText(exchange, "Эпик удален");
    }
}