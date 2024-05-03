package http.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import http.HttpTaskServer;
import interfaces.TaskManager;

import java.io.IOException;

public class PrioritizedHandler implements HttpHandler {
    private final TaskManager manager;
    private final Gson gson;


    public PrioritizedHandler(TaskManager manager, Gson gson) {
        this.manager = manager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            HttpTaskServer.writeResponse(exchange, gson.toJson(manager.getHistory()), 200);
        }
    }
}
