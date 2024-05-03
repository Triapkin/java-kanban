package test.http.handlers;

import com.google.gson.reflect.TypeToken;
import enums.TaskType;
import http.HttpTaskServer;
import implementation.InMemoryTaskManager;
import interfaces.TaskManager;
import models.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static test.http.HttpTaskServerTests.*;

public class TaskHandlerTests {

    private static final String TASKS_URL = "http://localhost:8080/tasks/";

    private TaskManager manager = new InMemoryTaskManager();
    private HttpTaskServer server = new HttpTaskServer(manager);
    private HttpClient httpClient = HttpClient.newHttpClient();

    public TaskHandlerTests() throws IOException {
    }

    @BeforeEach
    public void setUpBeforeClass() {
        server.start();
    }

    @AfterEach
    public void tearDownAfterClass() {
        server.stop();
    }

    @Test
    public void createNewTaskWithPost() throws IOException, InterruptedException {
        HttpRequest httpRequest = createBasePostRequest(TASKS_URL, createTaskAndConvertToJson());

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "Статус код не равен 201");

        List<Task> tasks = manager.getAllTasks();
        assertNotNull(tasks, "Не вернулись задачи");
        assertEquals(1, tasks.size(), "Создалось больше одной задачи");
        assertEquals("title_check", tasks.get(0).getTitle(), "Некорректное имя задачи");
    }

    @Test
    public void updateNewTaskWithPost() throws IOException, InterruptedException {
        Task task = new Task("title_check", "description", TaskType.TASK, 60, LocalDateTime.now());
        manager.createNewTasks(task);
        Task updatedTask = new Task("new_title_check", "new_description", TaskType.TASK, 60, LocalDateTime.now());
        updatedTask.setId(task.getId());

        HttpRequest httpRequest = createBasePostRequest(TASKS_URL, server.getGson().toJson(updatedTask));

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode(), "Статус код не равен 201");
    }

    @Test
    public void getTaskById() throws IOException, InterruptedException {
        Task task = new Task("title_check", "description", TaskType.TASK, 60, LocalDateTime.now());
        manager.createNewTasks(task);
        HttpRequest httpRequest = createBaseGetRequest(TASKS_URL + task.getId());

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Статус код не равен 201");

        Task taskFromResponse = server.getGson().fromJson(response.body(), new TypeToken<Task>() {
        }.getType());
        assertEquals(task.getId(), taskFromResponse.getId(), "Вернулся не тот айди запроса по id");
        assertEquals(task.getTitle(), taskFromResponse.getTitle(), "Вернулся непраивльный титл");
    }

    @Test
    public void deleteTaskById() throws IOException, InterruptedException {
        Task task = new Task("title_check", "description", TaskType.TASK, 60, LocalDateTime.now());
        manager.createNewTasks(task);
        HttpRequest httpRequest = createBaseDeleteRequest(TASKS_URL + task.getId());

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Статус код не равен 201");

        assertNull(manager.getTaskById(task.getId()), "задача не удалилась");
    }

    @Test
    public void getTaskWithWrongId() throws IOException, InterruptedException {
        HttpRequest httpRequest = createBaseDeleteRequest(TASKS_URL + "666");

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode(), "Статус код не равен 404");

        assertEquals("Задачи с id: 666 не найдена", response.body(), "Вернулось неправильное сообщение");
    }

    @Test
    public void getAllTasks() throws IOException, InterruptedException {
        manager.createNewTasks(new Task("title_1", "description", TaskType.TASK, 60, LocalDateTime.now().plusDays(1)));
        manager.createNewTasks(new Task("title_2", "description", TaskType.TASK, 60, LocalDateTime.now().plusDays(2)));
        manager.createNewTasks(new Task("title_3", "description", TaskType.TASK, 60, LocalDateTime.now().plusDays(3)));

        HttpRequest httpRequest = createBaseGetRequest(TASKS_URL);
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Статус код не равен 200");
        List<Task> tasks = server.getGson().fromJson(response.body(), new TypeToken<ArrayList<Task>>() {
        }.getType());
        assertEquals(3, tasks.size(), "Получилось больше трех задач");
    }

    private String createTaskAndConvertToJson() {
        return server.getGson().toJson(new Task("title_check", "description", TaskType.TASK, 60, LocalDateTime.now()));
    }
}
