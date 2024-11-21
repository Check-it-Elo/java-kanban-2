package http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import service.TaskManager;
import service.Managers;

import java.io.IOException;
import java.io.OutputStream;

public abstract class BaseHttpHandler implements HttpHandler {
    protected TaskManager taskManager;

    public BaseHttpHandler() {
        this.taskManager = Managers.getDefault();
    }

    protected void sendText(HttpExchange exchange, String text, int statusCode) throws IOException {
        exchange.sendResponseHeaders(statusCode, text.length());
        OutputStream os = exchange.getResponseBody();
        os.write(text.getBytes());
        os.close();
    }

    protected void sendNotFound(HttpExchange exchange) throws IOException {
        sendText(exchange, "Объект не был найден", 404);
    }

    protected void sendHasInteractions(HttpExchange exchange) throws IOException {
        sendText(exchange, "При создании или обновлении задача пересекается с уже существующими", 406);
    }
}