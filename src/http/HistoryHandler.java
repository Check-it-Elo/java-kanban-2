package http;

import com.sun.net.httpserver.HttpExchange;
import com.google.gson.Gson;
import model.Task;
import service.InMemoryTaskManager;

import java.io.IOException;
import java.util.List;

public class HistoryHandler extends BaseHttpHandler {

    private final InMemoryTaskManager taskManager;
    private final Gson gson = new Gson();

    public HistoryHandler(InMemoryTaskManager taskManager) {
        this.taskManager = taskManager;
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            List<Task> history = taskManager.getHistory();

            String jsonResponse = gson.toJson(history);
            sendText(exchange, jsonResponse, 200);
        } else {
            sendNotFound(exchange);
        }
    }
}