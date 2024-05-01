package http.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import enums.Endpoint;
import http.HttpTaskServer;
import interfaces.TaskManager;
import models.Epic;
import models.Subtask;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static http.helpers.BaseHttpHandler.*;

public class EpicHandler implements HttpHandler {

    private final TaskManager manager;
    private final Gson gson;


    public EpicHandler(TaskManager manager, Gson gson) {
        this.manager = manager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange);
        switch (endpoint) {
            case GET_ALL:
                handleGetAllEpics(exchange);
                break;
            case GET_EPIC_SUBTASK:
                handleGetEpicSubtasks(exchange);
                break;
            case GET_BY_ID:
                handleGetEpicById(exchange);
                break;
            case POST_CREATE:
                handlePostCrateNewEpic(exchange);
                break;
            case DELETE:
                handleDeleteEpicById(exchange);
                break;
            default:
                HttpTaskServer.writeResponse(exchange, "Неизвестный энподинт", 404);
                break;
        }
    }

    private void handleGetAllEpics(HttpExchange exchange) throws IOException {
        HttpTaskServer.writeResponse(exchange, gson.toJson(manager.getALlEpics()), 200);
    }

    private void handleGetEpicById(HttpExchange exchange) throws IOException {
        int epicId = getTaskId(exchange);
        if (manager.getEpicById(epicId) != null) {
            HttpTaskServer.writeResponse(exchange, gson.toJson(manager.getEpicById(epicId)), 200);
        } else {
            HttpTaskServer.writeResponse(exchange, "Эпик с id: " + epicId + " не найден.", 404);
        }
    }

    private void handleGetEpicSubtasks(HttpExchange exchange) throws IOException {
        int epicId = getEpicId(exchange);
        if (manager.getEpicById(epicId) != null) {
            List<Subtask> subtaskList = manager.getEpicById(epicId).getSubTasksId().stream().map(manager::getSubTasksById).collect(Collectors.toList());
            HttpTaskServer.writeResponse(exchange, gson.toJson(subtaskList), 200);
        } else {
            HttpTaskServer.writeResponse(exchange, "Эпик с id: " + epicId + " не найден", 404);
        }
    }

    private void handlePostCrateNewEpic(HttpExchange exchange) throws IOException {
        Optional<Epic> parseEpic = parseTask(Epic.class, exchange.getRequestBody(), gson);
        if (parseEpic.isEmpty()) {
            HttpTaskServer.writeResponse(exchange, "Не удалось распарсить задачу", 404);
        }

        Epic epic = new Epic(parseEpic.get().getTitle(), parseEpic.get().getDescription(), parseEpic.get().getTaskType());
        if (epic.getId() == 0) {
            manager.createEpic(epic);
            HttpTaskServer.writeResponse(exchange, "Создан эпик с id: " + epic.getId(), 201);
        } else {
            manager.updateEpic(epic);
            HttpTaskServer.writeResponse(exchange, "Задача с id: " + epic.getId() + " обновлена", 201);
        }

        HttpTaskServer.writeResponse(exchange, gson.toJson(epic), 200);
    }

    private void handleDeleteEpicById(HttpExchange exchange) throws IOException {
        int epicId = getTaskId(exchange);
        if (manager.getEpicById(epicId) != null) {
            manager.deleteEpicById(epicId);
            HttpTaskServer.writeResponse(exchange, "Эпик с id: " + epicId + " удален", 200);
        } else {
            HttpTaskServer.writeResponse(exchange, "Эпие с id: " + epicId + " не найден", 404);
        }
    }
}
