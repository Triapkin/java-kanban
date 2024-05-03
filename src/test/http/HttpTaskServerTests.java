package test.http;

import http.HttpTaskServer;
import implementation.InMemoryTaskManager;
import interfaces.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class HttpTaskServerTests {

    private TaskManager manager = new InMemoryTaskManager();
    private HttpTaskServer server = new HttpTaskServer(manager);

    public HttpTaskServerTests() throws IOException {
    }


    @BeforeEach
    public void setUpBeforeClass() {
        server.start();
    }

    @AfterEach
    public void tearDownAfterClass() {
        server.stop();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "http://localhost:8080/tasks/",
            "http://localhost:8080/subtasks/",
            "http://localhost:8080/epics/",
            "http://localhost:8080/history/",
            "http://localhost:8080/prioritized/"
    })
    public void serverShouldStart(String url) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = createBaseGetRequest(url);

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Статус код не равен 200");
    }

    public static HttpRequest createBaseGetRequest(String url) {
        return HttpRequest
                .newBuilder(URI.create(url))
                .header("Accept", "application/json")
                .GET().build();
    }

    public static HttpRequest createBasePostRequest(String url, String body) {
        return HttpRequest
                .newBuilder(URI.create(url))
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
    }

    public static HttpRequest createBaseDeleteRequest(String url) {
        return HttpRequest
                .newBuilder(URI.create(url))
                .header("Accept", "application/json")
                .DELETE()
                .build();
    }
}
