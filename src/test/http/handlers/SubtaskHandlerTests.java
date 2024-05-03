package test.http.handlers;

import com.google.gson.reflect.TypeToken;
import enums.TaskType;
import http.HttpTaskServer;
import implementation.InMemoryTaskManager;
import interfaces.TaskManager;
import models.Epic;
import models.Subtask;
import models.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
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

public class SubtaskHandlerTests {

    private static final String SUBTASK_URL = "http://localhost:8080/subtasks/";

    static TaskManager manager = new InMemoryTaskManager();
    private HttpTaskServer server = new HttpTaskServer(manager);
    private HttpClient httpClient = HttpClient.newHttpClient();
    static Epic epic = new Epic("epic_title", "epic_description", TaskType.EPIC);


    public SubtaskHandlerTests() throws IOException {
    }

    @BeforeAll
    public static void setUpClass() {
        manager.createEpic(epic);
    }

    @BeforeEach
    public void setUpBeforeClass() {
        manager.deleteAllSubTasks();
        server.start();
    }

    @AfterEach
    public void tearDownAfterClass() {
        server.stop();
    }

    @Test
    public void createNewSubtaskWithPost() throws IOException, InterruptedException {
        HttpRequest httpRequest = createBasePostRequest(SUBTASK_URL, createSubtaskAndConvertToJson());
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "Статус код не равен 201");
        List<Subtask> subtasks = manager.getAllSubTasks();
        assertNotNull(subtasks, "Не вернулись задачи");
        assertEquals(1, subtasks.size(), "Создалось больше одной задачи");
        assertEquals("subtask_title", subtasks.get(0).getTitle(), "Некорректное имя задачи");
    }


    @Test
    public void updateSubtaskWithPost() throws IOException, InterruptedException {
        Subtask subtask = new Subtask("title_sub", "description_sub", epic.getId(), TaskType.SUBTASK, 60, LocalDateTime.now().plusDays(1));
        manager.createNewSubTask(subtask);
        Subtask updatedSubtask = new Subtask("title_sub_new", "description_sub_new", epic.getId(), TaskType.SUBTASK, 60, LocalDateTime.now().plusDays(2));
        updatedSubtask.setId(subtask.getId());
        HttpRequest httpRequest = createBasePostRequest(SUBTASK_URL, server.getGson().toJson(updatedSubtask));
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "Статус код не равен 201");
        assertEquals("Подздача с id: " + subtask.getId() + " обновлена", response.body(), "сообщение не сошлось");
    }

    @Test
    public void getSubtaskById() throws IOException, InterruptedException {
        Subtask subtask = new Subtask("title_sub", "description_sub", epic.getId(), TaskType.SUBTASK, 60, LocalDateTime.now().plusDays(3));
        manager.createNewSubTask(subtask);
        HttpRequest httpRequest = createBaseGetRequest(SUBTASK_URL + subtask.getId());

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Статус код не равен 201");

        Task taskFromResponse = server.getGson().fromJson(response.body(), new TypeToken<Subtask>() {
        }.getType());
        assertEquals(subtask.getId(), taskFromResponse.getId(), "Вернулся не тот айди запроса по id");
        assertEquals(subtask.getTitle(), taskFromResponse.getTitle(), "Вернулся непраивльный титл");
    }

    @Test
    public void deleteSubtaskById() throws IOException, InterruptedException {
        Subtask subtask = new Subtask("title_sub", "description_sub", epic.getId(), TaskType.SUBTASK, 60, LocalDateTime.now().plusDays(5));
        manager.createNewSubTask(subtask);
        HttpRequest httpRequest = createBaseDeleteRequest(SUBTASK_URL + subtask.getId());

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Статус код не равен 201");

        assertNull(manager.getSubTasksById(subtask.getId()), "задача не удалилась");
    }


    @Test
    public void getAllSubtasks() throws IOException, InterruptedException {
        manager.createNewSubTask(new Subtask("title_sub", "description_sub", epic.getId(), TaskType.SUBTASK, 60, LocalDateTime.now().plusMonths(1)));
        manager.createNewSubTask(new Subtask("title_sub2", "description_sub2", epic.getId(), TaskType.SUBTASK, 60, LocalDateTime.now().plusMonths(2)));
        manager.createNewSubTask(new Subtask("title_sub3", "description_sub3", epic.getId(), TaskType.SUBTASK, 60, LocalDateTime.now().plusMonths(3)));

        HttpRequest httpRequest = createBaseGetRequest(SUBTASK_URL);
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Статус код не равен 200");
        List<Subtask> tasks = server.getGson().fromJson(response.body(), new TypeToken<ArrayList<Subtask>>() {
        }.getType());
        assertEquals(3, tasks.size(), "Получилось больше трех задач");
    }

    private String createSubtaskAndConvertToJson() {
        return server.getGson().toJson(new Subtask("subtask_title", "subtask_description", epic.getId(), TaskType.SUBTASK, 60, LocalDateTime.now()));
    }
}
