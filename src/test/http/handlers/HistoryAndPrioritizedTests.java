package test.http.handlers;

import com.google.gson.reflect.TypeToken;
import enums.TaskType;
import http.HttpTaskServer;
import implementation.InMemoryTaskManager;
import interfaces.TaskManager;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static test.http.HttpTaskServerTests.createBaseGetRequest;

public class HistoryAndPrioritizedTests {

    private static final String HISTORY_URL = "http://localhost:8080/history/";

    static TaskManager manager = new InMemoryTaskManager();
    private HttpTaskServer server = new HttpTaskServer(manager);
    HttpClient httpClient = HttpClient.newHttpClient();
    static Task task, task2, task3;

    public HistoryAndPrioritizedTests() throws IOException {
    }

    @BeforeAll
    public static void setUp() {
        createTaskForTest();
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
    public void shouldHistoryEndpointReturnTasksInHistory() throws IOException, InterruptedException {
        manager.getTaskById(task.getId());
        manager.getTaskById(task2.getId());

        HttpRequest httpRequest = createBaseGetRequest(HISTORY_URL);
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Статус код не равен 201");

        List<Task> tasks = server.getGson().fromJson(response.body(), new TypeToken<ArrayList<Task>>() {
        }.getType());

        assertEquals(tasks.size(), manager.getHistory().size(), "вернулось больше задач в иcтории");
    }

    @Test
    public void shouldTasksSortInPrioritizedList() throws IOException, InterruptedException {
        manager.getTaskById(task.getId());
        manager.getTaskById(task2.getId());
        manager.getTaskById(task3.getId());
        HttpRequest httpRequest = createBaseGetRequest(HISTORY_URL);
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Статус код не равен 201");

        List<Task> tasks = server.getGson().fromJson(response.body(), new TypeToken<ArrayList<Task>>() {
        }.getType());

        assertEquals(task, tasks.get(0), "первая таска не совпала в prioritized");
        assertEquals(task3, tasks.get(2), "последняя таска не совпала в prioritized");
    }

    private static void createTaskForTest() {
        task = new Task("title_task", "title_description", TaskType.TASK, 60, LocalDateTime.now());
        task2 = new Task("title_task2", "title_description", TaskType.TASK, 60, LocalDateTime.now().plusDays(1));
        task3 = new Task("title_task3", "title_description", TaskType.TASK, 60, LocalDateTime.now().plusDays(3));

        manager.createNewTasks(task);
        manager.createNewTasks(task2);
        manager.createNewTasks(task3);
    }
}
