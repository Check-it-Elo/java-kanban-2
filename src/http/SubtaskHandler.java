package http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import service.TaskManager;
import model.Subtask;
import com.google.gson.Gson;

import java.io.*;
import java.util.List;

public class SubtaskHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson = new Gson();

    public SubtaskHandler(TaskManager taskManager) {
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
        List<Subtask> subtasks = taskManager.getAllSubtasks();
        String jsonResponse = gson.toJson(subtasks);
        sendText(exchange, jsonResponse);
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
        Subtask subtask = gson.fromJson(reader, Subtask.class);
        taskManager.addSubtask(subtask);
        sendText(exchange, gson.toJson(subtask));
    }

    private void handleDelete(HttpExchange exchange) throws IOException {
        String id = exchange.getRequestURI().getPath().split("/")[2];
        int subtaskId = Integer.parseInt(id);
        taskManager.deleteSubtaskById(subtaskId);
        sendText(exchange, "Подзадача удалена");
    }
}
