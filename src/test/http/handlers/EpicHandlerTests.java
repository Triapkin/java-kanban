package test.http.handlers;

import com.google.gson.reflect.TypeToken;
import enums.TaskType;
import http.HttpTaskServer;
import implementation.InMemoryTaskManager;
import interfaces.TaskManager;
import models.Epic;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static test.http.HttpTaskServerTests.createBaseGetRequest;
import static test.http.HttpTaskServerTests.createBasePostRequest;

public class EpicHandlerTests {

    private static final String EPIC_URL = "http://localhost:8080/epics/";

    private TaskManager manager = new InMemoryTaskManager();
    HttpTaskServer server = new HttpTaskServer(manager);
    HttpClient httpClient = HttpClient.newHttpClient();

    public EpicHandlerTests() throws IOException {
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
    public void createNewEpicWithPost() throws IOException, InterruptedException {
        HttpRequest httpRequest = createBasePostRequest(EPIC_URL, createEpicAndConvertToJson());

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "Статус код не равен 201");

        List<Epic> tasks = manager.getALlEpics();
        assertNotNull(tasks, "Не вернулись задачи");
        assertEquals(1, tasks.size(), "Создалось больше одной задачи");
        assertEquals("title_epic", tasks.get(0).getTitle(), "Некорректное имя задачи");
    }

    @Test
    public void getEpicById() throws IOException, InterruptedException {
        Epic epic = new Epic("title_check", "description", TaskType.EPIC);
        manager.createEpic(epic);
        HttpRequest httpRequest = createBaseGetRequest(EPIC_URL + epic.getId());

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Статус код не равен 200");

        Epic epicFromResponse = server.getGson().fromJson(response.body(), new TypeToken<Epic>() {
        }.getType());
        assertEquals(epic.getId(), epicFromResponse.getId(), "Вернулся не тот айди запроса по id");
        assertEquals(epic.getTitle(), epicFromResponse.getTitle(), "Вернулся неправильный титл");
    }

    private String createEpicAndConvertToJson() {
        return server.getGson().toJson(new Epic("title_epic", "title_description", TaskType.EPIC));
    }
}
