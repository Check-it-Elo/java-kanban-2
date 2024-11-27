import http.DurationAdapter;
import http.LocalDateTimeAdapter;
import model.Epic;
import model.Status;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.http.HttpRequest;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

import service.TaskManager;
import service.InMemoryTaskManager;
import model.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import http.HttpTaskServer;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskManagerTest {

    private TaskManager manager;
    private HttpTaskServer taskServer;
    private Gson gson;

    @BeforeEach
    public void setUp() {
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
        manager = new InMemoryTaskManager();
        taskServer = new HttpTaskServer(manager);
        taskServer.start();
    }

    @AfterEach
    public void tearDown() {
        taskServer.stop();
    }


    @Test
    public void testAddTask() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks");

        Task task = new Task("Task to add", "Add me", model.Status.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        manager.addTask(task);
        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(20))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.statusCode());

        assertEquals(200, response.statusCode());

    }


    @Test
    public void testGetTasks() throws IOException, InterruptedException {

        manager.addTask(new Task("Task 1", "Description 1", model.Status.NEW, Duration.ofMinutes(30), LocalDateTime.now()));
        manager.addTask(new Task("Task 2", "Description 2", model.Status.NEW, Duration.ofMinutes(45), LocalDateTime.now()));

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("Task 1"));
        assertTrue(response.body().contains("Task 2"));
    }


    @Test
    public void testDeleteTask() throws IOException, InterruptedException {
        Task task = new Task("Task to delete", "Delete me", model.Status.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        manager.addTask(task);

        URI url = URI.create("http://localhost:8080/tasks/" + task.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertNull(manager.getTaskById(task.getId()), "Задача не была удалена");
    }


    @Test
    public void testGetTaskHistory() throws IOException, InterruptedException {
        // Создание и добавление задач
        Task task1 = new Task("Task 1", "Description of Task 1", Status.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        Task task2 = new Task("Task 2", "Description of Task 2", Status.NEW, Duration.ofMinutes(45), LocalDateTime.now());
        manager.addTask(task1);
        manager.addTask(task2);

        // Имитируем просмотр задач
        manager.getTaskById(task1.getId());
        manager.getTaskById(task2.getId());

        // Запрашиваем историю
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/history");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Проверка статуса и содержимого ответа
        assertEquals(200, response.statusCode(), "Статус код должен быть 200");
        assertTrue(response.body().contains("Task 1"), "Ответ должен содержать 'Task 1'");
        assertTrue(response.body().contains("Task 2"), "Ответ должен содержать 'Task 2'");
    }


    @Test
    public void testGetEpicById() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic 1", "This is epic 1");
        manager.addEpic(epic);

        URI url = URI.create("http://localhost:8080/epics/" + epic.getId());
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("Epic 1"), "The response should contain the epic title");
    }

    @Test
    public void testDeleteEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic to delete", "Delete this epic");
        manager.addEpic(epic);

        URI url = URI.create("http://localhost:8080/epics/" + epic.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertNull(manager.getEpicById(epic.getId()), "Epic should have been deleted");
    }


    @Test
    public void testGetPrioritizedTasks() throws IOException, InterruptedException {
        Task task1 = new Task("Task A", "Description A", Status.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        Task task2 = new Task("Task B", "Description B", Status.NEW, Duration.ofMinutes(15), LocalDateTime.now());
        manager.addTask(task1);
        manager.addTask(task2);

        URI url = URI.create("http://localhost:8080/prioritized");
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("Task A"), "Response should contain Task A");
        assertTrue(response.body().contains("Task B"), "Response should contain Task B");
    }


}