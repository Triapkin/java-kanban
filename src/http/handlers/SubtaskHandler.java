package http.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import enums.Endpoint;
import exceptions.CheckOverException;
import http.HttpTaskServer;
import interfaces.TaskManager;
import models.Subtask;

import java.io.IOException;
import java.util.Optional;

import static http.helpers.BaseHttpHandler.*;

public class SubtaskHandler implements HttpHandler {
    private final TaskManager manager;
    private final Gson gson;

    public SubtaskHandler(TaskManager manager, Gson gson) {
        this.manager = manager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange);
        switch (endpoint) {
            case GET_ALL:
                handleGetAllSubtasks(exchange);
                break;
            case GET_BY_ID:
                handleGetSubtaskById(exchange);
                break;
            case POST_CREATE:
                handlePostCreateNewSubTask(exchange);
                break;
            case DELETE:
                handleDeleteSubtaskById(exchange);
                break;
            default:
                HttpTaskServer.writeResponse(exchange, "Неизвестный энподинт", 404);
                break;
        }
    }

    private void handleGetAllSubtasks(HttpExchange exchange) throws IOException {
        HttpTaskServer.writeResponse(exchange, gson.toJson(manager.getAllSubTasks()), 200);
    }

    private void handleGetSubtaskById(HttpExchange exchange) throws IOException {
        int id = getTaskId(exchange);
        if (manager.getSubTasksById(id) != null) {
            HttpTaskServer.writeResponse(exchange, gson.toJson(manager.getSubTasksById(id)), 200);
        } else {
            HttpTaskServer.writeResponse(exchange, "Подзадача с id: " + id + " не найдена", 404);
        }
    }

    private void handlePostCreateNewSubTask(HttpExchange exchange) throws IOException {
        Optional<Subtask> parsedTask = parseTask(Subtask.class, exchange.getRequestBody(), gson);
        if (parsedTask.isEmpty()) {
            HttpTaskServer.writeResponse(exchange, "Не удалось распарсить подзадачу", 404);
        }

        try {
            Subtask subtask = parsedTask.get();
            if (subtask.getId() == 0) {
                manager.createNewSubTask(subtask);
                HttpTaskServer.writeResponse(exchange, "Создана подзадача с id: " + subtask.getId(), 201);
            } else {
                manager.updateSubTasks(subtask);
                HttpTaskServer.writeResponse(exchange, "Подздача с id: " + subtask.getId() + " обновлена", 201);
            }
        } catch (CheckOverException e) {
            HttpTaskServer.writeResponse(exchange, "Не удалось добавить подзадачу, задачи пересекаются", 406);
        }

        HttpTaskServer.writeResponse(exchange, gson.toJson(parsedTask), 200);
    }

    private void handleDeleteSubtaskById(HttpExchange exchange) throws IOException {
        int subtaskId = getTaskId(exchange);
        if (manager.getSubTasksById(subtaskId) != null) {
            manager.deleteSubTasksById(subtaskId);
            HttpTaskServer.writeResponse(exchange, "Подзадача с id: " + subtaskId + " удалена", 200);
        } else {
            HttpTaskServer.writeResponse(exchange, "Подзадача с id: " + subtaskId + " не найдена", 404);
        }
    }
}
