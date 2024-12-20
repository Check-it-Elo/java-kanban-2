package http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Task;
import service.TaskManager;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;


public class TaskHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;

    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    public TaskHandler(TaskManager taskManager) {
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

        System.out.println("Получен GET-запрос");

        List<Task> tasks = taskManager.getAllTasks();
        String jsonResponse = gson.toJson(tasks);
        sendText(exchange, jsonResponse);
    }

    private void handlePost(HttpExchange exchange) throws IOException {

        System.out.println("Получен POST-запрос c телом: " + exchange.getRequestBody());

        BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
        Task task = gson.fromJson(reader, Task.class);
        taskManager.addTask(task);
        sendText(exchange, gson.toJson(task));
    }


    private void handleDelete(HttpExchange exchange) throws IOException {

        String id = exchange.getRequestURI().getPath().split("/")[2];
        int taskId = Integer.parseInt(id);
        taskManager.deleteTaskById(taskId);
        sendText(exchange, "Задача удалена");
    }


}

