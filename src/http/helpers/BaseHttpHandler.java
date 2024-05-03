package http.helpers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import enums.Endpoint;
import models.Task;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;


public class BaseHttpHandler {

    public static Endpoint getEndpoint(HttpExchange exchange) {
        String path = exchange.getRequestURI().getPath();
        String requestMethod = exchange.getRequestMethod();
        String[] split = path.split("/");
        switch (requestMethod) {
            case "GET":
                if ("epics".equals(split[1]) && split.length == 4) return Endpoint.GET_EPIC_SUBTASK;
                if (split.length == 2) return Endpoint.GET_ALL;
                if (split.length == 3) return Endpoint.GET_BY_ID;
            case "POST":
                return Endpoint.POST_CREATE;
            case "DELETE":
                return Endpoint.DELETE;
            default:
                return Endpoint.UNKNOWN;
        }
    }

    public static <T extends Task> Optional<T> parseTask(Class<T> tClass, InputStream taskStream, Gson gson) throws IOException {
        String body = new String(taskStream.readAllBytes(), StandardCharsets.UTF_8);
        JsonElement jsonElement = JsonParser.parseString(body);
        if (jsonElement.isJsonObject()) {
            T task = gson.fromJson(body, tClass);
            return Optional.of(task);
        }
        return Optional.empty();
    }

    public static int getTaskId(HttpExchange exchange) {
        String[] split = exchange.getRequestURI().getPath().split("/");
        return Integer.parseInt(split[split.length - 1]);
    }

    public static int getEpicId(HttpExchange exchange) {
        String[] split = exchange.getRequestURI().getPath().split("/");
        return Integer.parseInt(split[split.length - 2]);
    }
}
