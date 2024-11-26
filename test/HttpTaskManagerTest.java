import http.DurationAdapter;
import http.LocalDateTimeAdapter;
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

        // Сериализация задачи
        String taskJson = gson.toJson(new Task(
                "Новая задача",
                "Описание задачи",
                Status.NEW,
                Duration.ofMinutes(90),
                LocalDateTime.of(2024, 8, 15, 10, 0)
        ));

        System.out.println("Отправляемый JSON: " + taskJson);

        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(20))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Статус код должен быть 200");
        assertTrue(response.body().contains("Новая задача"), "Ответ должен содержать заголовок задачи");

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


//    @Test
//    public void testGetTasks() throws IOException, InterruptedException {
//        // Настройка объектов задач
//        Task task1 = new Task("Task 1", "Description 1", Status.NEW, Duration.ofHours(1), LocalDateTime.now());
//        Task task2 = new Task("Task 2", "Description 2", Status.NEW, Duration.ofHours(2), LocalDateTime.now().plusDays(1));
//
//        // Сериализация задач
//        Gson gson = new GsonBuilder()
//                .registerTypeAdapter(Duration.class, new DurationAdapter())
//                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
//                .create();
//
//        String task1Json = gson.toJson(task1);
//        String task2Json = gson.toJson(task2);
//
//        // Отправка POST-запросов на добавление задач
//        URI taskUri = URI.create("http://localhost:8080/tasks");
//
//        HttpClient client = HttpClient.newHttpClient();
//
//        try {
//            HttpRequest task1HttpRequest = HttpRequest.newBuilder()
//                    .uri(taskUri)
//                    .POST(HttpRequest.BodyPublishers.ofString(task1Json))
//                    .header("Content-Type", "application/json")
//                    .build();
//
//            HttpResponse<String> response1 = client.send(task1HttpRequest, HttpResponse.BodyHandlers.ofString());
//
//            HttpRequest task2HttpRequest = HttpRequest.newBuilder()
//                    .uri(taskUri)
//                    .POST(HttpRequest.BodyPublishers.ofString(task2Json))
//                    .header("Content-Type", "application/json")
//                    .build();
//
//            HttpResponse<String> response2 = client.send(task2HttpRequest, HttpResponse.BodyHandlers.ofString());
//
//            // Проверяем статус для первого запроса
//            if (response1.statusCode() != 200) {
//                System.err.println("Ошибка при добавлении задачи 1: " + response1.body());
//            }
//
//            // Проверяем статус для второго запроса
//            if (response2.statusCode() != 200) {
//                System.err.println("Ошибка при добавлении задачи 2: " + response2.body());
//            }
//
//        } catch (IOException e) {
//            System.out.println("HTTP: " + e.getMessage());
//            return;
//        }
//
//        // Получение задач
//        URI url = URI.create("http://localhost:8080/tasks");
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(url)
//                .GET()
//                .build();
//
//        HttpResponse<String> response;
//
//        try {
//            response = client.send(request, HttpResponse.BodyHandlers.ofString());
//
//            // Проверка результатов
//            assertEquals(200, response.statusCode());
//            assertTrue(response.body().contains("Task 1"));
//            assertTrue(response.body().contains("Task 2"));
//        } catch (IOException e) {
//            System.err.println("Ошибка при получении задач: " + e.getMessage());
//        }
//    }


}