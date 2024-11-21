package http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import model.Epic;
import service.InMemoryTaskManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class EpicHandler extends BaseHttpHandler {


    private final InMemoryTaskManager taskManager;
    private final Gson gson = new Gson();

    public EpicHandler(InMemoryTaskManager taskManager) {
        this.taskManager = taskManager;
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            String response;

            switch (method) {
                case "GET":
                    response = getAllEpics();
                    sendText(exchange, response, 200);
                    break;

                case "POST":
                    String epicJson = getRequestBody(exchange);
                    Epic epic = gson.fromJson(epicJson, Epic.class);
                    taskManager.addEpic(epic);
                    sendText(exchange, "Эпик успешно создан", 201);
                    break;

                case "DELETE":
                    String epicId = getQueryParameter(exchange, "id");
                    if (epicId != null) {
                        taskManager.deleteEpicById(Integer.parseInt(epicId));
                        sendText(exchange, "Эпик успешно удален", 200);
                    } else {
                        sendNotFound(exchange);
                    }
                    break;

                default:
                    sendText(exchange, "Метод не поддерживается", 405);
            }
        } catch (NumberFormatException e) {
            sendText(exchange, "Некорректный ID эпика", 400);
        } catch (NotFoundException e) {
            sendNotFound(exchange);
        } catch (Exception e) {
            sendText(exchange, "Ошибка сервера", 500);
        }
    }

    private String getAllEpics() {
        List<Epic> epics = taskManager.getAllEpics();
        return gson.toJson(epics);
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