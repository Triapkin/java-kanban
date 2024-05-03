package http.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import enums.Endpoint;
import exceptions.CheckOverException;
import http.HttpTaskServer;
import interfaces.TaskManager;
import models.Task;

import java.io.IOException;
import java.util.Optional;

import static http.helpers.BaseHttpHandler.*;

public class TaskHandler implements HttpHandler {
    private final TaskManager manager;
    private final Gson gson;

    public TaskHandler(TaskManager manager, Gson gson) {
        this.manager = manager;
        this.gson = gson;
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange);
        switch (endpoint) {
            case GET_ALL:
                handleGetAllTasks(exchange);
                break;
            case GET_BY_ID:
                handleGetTaskById(exchange);
                break;
            case POST_CREATE:
                handlePostCreateNewTask(exchange);
                break;
            case DELETE:
                handleDeleteTask(exchange);
                break;
            default:
                HttpTaskServer.writeResponse(exchange, "Неизвестный энподинт", 404);
                break;
        }
    }

    private void handleGetAllTasks(HttpExchange exchange) throws IOException {
        HttpTaskServer.writeResponse(exchange, gson.toJson(manager.getAllTasks()), 200);
    }

    private void handleGetTaskById(HttpExchange exchange) throws IOException {
        int taskId = getTaskId(exchange);
        if (manager.getTaskById(taskId) != null) {
            HttpTaskServer.writeResponse(exchange, gson.toJson(manager.getTaskById(taskId)), 200);
        } else {
            HttpTaskServer.writeResponse(exchange, "Задачи с id: " + taskId + " не найдена", 404);
        }
    }

    private void handlePostCreateNewTask(HttpExchange exchange) throws IOException {
        Optional<Task> parsedTask = parseTask(Task.class, exchange.getRequestBody(), gson);
        if (parsedTask.isEmpty()) {
            HttpTaskServer.writeResponse(exchange, "Не удалось распарсить задачу", 404);
        }

        try {
            Task task = parsedTask.get();
            if (task.getId() == 0) {
                manager.createNewTasks(task);
                HttpTaskServer.writeResponse(exchange, "Создана задача с id: " + task.getId(), 201);
            } else {
                manager.updateTask(task);
                HttpTaskServer.writeResponse(exchange, "Задача с id: " + task.getId() + " обновлена", 201);
            }
        } catch (CheckOverException e) {
            HttpTaskServer.writeResponse(exchange, "Не удалось добавить задачу, задачи пересекаются", 406);
        }

        HttpTaskServer.writeResponse(exchange, gson.toJson(parsedTask), 200);
    }

    private void handleDeleteTask(HttpExchange exchange) throws IOException {
        int taskId = getTaskId(exchange);
        if (manager.getTaskById(taskId) != null) {
            manager.deleteTaskById(taskId);
            HttpTaskServer.writeResponse(exchange, "Задача с id: " + taskId + " удалена", 200);
        } else {
            HttpTaskServer.writeResponse(exchange, "Задачи с id: " + taskId + " не найдена", 404);
        }
    }
}
