package http;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class BaseHttpHandler {


    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected void sendNotFound(HttpExchange h) throws IOException {
        h.sendResponseHeaders(404, -1);
        h.close();
    }

    protected void sendConflict(HttpExchange h) throws IOException {
        h.sendResponseHeaders(406, -1);
        h.close();
    }

    protected void sendError(HttpExchange h) throws IOException {
        h.sendResponseHeaders(500, -1);
        h.close();
    }


}