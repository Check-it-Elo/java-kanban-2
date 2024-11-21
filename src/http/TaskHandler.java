package http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import model.Task;
import service.InMemoryTaskManager;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class TaskHandler extends BaseHttpHandler {

    private final InMemoryTaskManager taskManager;
    private final Gson gson = new Gson();

    public TaskHandler(InMemoryTaskManager taskManager) {
        this.taskManager = taskManager;
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            String response;

            switch (method) {
                case "GET":
                    response = getAllTasks();
                    sendText(exchange, response, 200);
                    break;

                case "POST":
                    String taskJson = getRequestBody(exchange);
                    Task task = gson.fromJson(taskJson, Task.class);
                    taskManager.addTask(task);
                    sendText(exchange, "Задача успешно создана", 201);
                    break;

                case "DELETE":
                    String taskId = getQueryParameter(exchange, "id");
                    if (taskId != null) {
                        taskManager.deleteTaskById(Integer.parseInt(taskId));
                        sendText(exchange, "Задача успешно удалена", 200);
                    } else {
                        sendNotFound(exchange);
                    }
                    break;

                default:
                    sendText(exchange, "Метод не поддерживается", 405);
            }
        } catch (NumberFormatException e) {
            sendText(exchange, "Некорректный ID задачи", 400);
        } catch (NotFoundException e) {
            sendNotFound(exchange);
        } catch (Exception e) {
            sendText(exchange, "Ошибка сервера", 500);
        }
    }


    // Преобразуем список задач в JSON
    private String getAllTasks() {
        List<Task> tasks = taskManager.getAllTasks();
        return gson.toJson(tasks);
    }


    //Получаем тело задачи в JSON
    private String getRequestBody(HttpExchange exchange) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
        StringBuilder requestBody = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            requestBody.append(line);
        }
        return requestBody.toString();
    }


    //Извлечение параметра
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

