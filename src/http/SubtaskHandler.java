package http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import model.Subtask;
import service.InMemoryTaskManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class SubtaskHandler extends BaseHttpHandler {

    private final InMemoryTaskManager taskManager;
    private final Gson gson = new Gson();

    public SubtaskHandler(InMemoryTaskManager taskManager) {
        this.taskManager = taskManager;
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            String response;

            switch (method) {
                case "GET":
                    response = getAllSubtasks();
                    sendText(exchange, response, 200);
                    break;

                case "POST":
                    String subtaskJson = getRequestBody(exchange);
                    Subtask subtask = gson.fromJson(subtaskJson, Subtask.class);
                    taskManager.addSubtask(subtask);
                    sendText(exchange, "Подзадача успешно создана", 201);
                    break;

                case "DELETE":
                    String subtaskId = getQueryParameter(exchange, "id");
                    if (subtaskId != null) {
                        taskManager.deleteSubtaskById(Integer.parseInt(subtaskId));
                        sendText(exchange, "Подзадача успешно удалена", 200);
                    } else {
                        sendNotFound(exchange);
                    }
                    break;

                default:
                    sendText(exchange, "Метод не поддерживается", 405);
            }
        } catch (NumberFormatException e) {
            sendText(exchange, "Некорректный ID подзадачи", 400);
        } catch (NotFoundException e) {
            sendNotFound(exchange);
        } catch (Exception e) {
            sendText(exchange, "Ошибка сервера", 500);
        }
    }

    private String getAllSubtasks() {
        List<Subtask> subtasks = taskManager.getAllSubtasks();
        return gson.toJson(subtasks);
    }

    private String getRequestBody(HttpExchange exchange) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
        StringBuilder requestBody = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            requestBody.append(line);
        }
        return requestBody.toString();
    }

    private String getQueryParameter(HttpExchange exchange, String param) {
        String query = exchange.getRequestURI().getQuery();
        if (query != null) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue[0].equals(param)) {
                    return keyValue.length > 1 ? keyValue[1] : null;
                }
            }
        }
        return null;
    }
}