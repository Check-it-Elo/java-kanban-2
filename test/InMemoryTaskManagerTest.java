import static org.junit.jupiter.api.Assertions.*;

import model.Status;
import model.Task;
import model.Epic;
import model.Subtask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.InMemoryTaskManager;

class InMemoryTaskManagerTest {

    private InMemoryTaskManager taskManager;
    private Task originalTask;

    @BeforeEach
    public void setUp() {
        taskManager = new InMemoryTaskManager();
        originalTask = new Task("Test Task", "This is a test task.", Status.NEW);
    }

    //проверяется неизменность задачи (по всем полям) при добавлении задачи в менеджер
    @Test
    public void testTaskImmutabilityAfterAdd() {
        taskManager.addTask(originalTask);
        Task fetchedTask = taskManager.getTask(originalTask.getId());

        assertEquals(originalTask.getId(), fetchedTask.getId());
        assertEquals(originalTask.getTitle(), fetchedTask.getTitle());
        assertEquals(originalTask.getDescription(), fetchedTask.getDescription());
    }


    //проверка, что InMemoryTaskManager действительно добавляет задачи разного типа и может найти их по id;
    @Test
    void testAddAndFindTaskById() {
        Task task = new Task("Task 1", "Description 1", Status.NEW);
        taskManager.addTask(task);

        Task foundTask = taskManager.getTaskById(task.getId());
        assertNotNull(foundTask, "Task should not be null");
        assertEquals(task.getId(), foundTask.getId(), "Task id should match");
        assertEquals(task.getTitle(), foundTask.getTitle(), "Task names should match");
        assertEquals(task.getDescription(), foundTask.getDescription(), "Task descriptions should match");
        assertEquals(task.getStatus(), foundTask.getStatus(), "Task statuses should match");
    }

    @Test
    void testAddAndFindEpicById() {
        Epic epic = new Epic("Epic 1", "Description for epic");
        taskManager.addEpic(epic);

        Epic foundEpic = taskManager.getEpicById(epic.getId());
        assertNotNull(foundEpic, "Epic should not be null");
        assertEquals(epic.getId(), foundEpic.getId(), "Epic id should match");
        assertEquals(epic.getTitle(), foundEpic.getTitle(), "Epic names should match");
        assertEquals(epic.getDescription(), foundEpic.getDescription(), "Epic descriptions should match");
    }

    @Test
    void testAddAndFindSubtaskById() {
        Epic epic = new Epic("Epic 1", "Description for epic");
        taskManager.addEpic(epic);

        Subtask subtask = new Subtask("Subtask 1", "Description for subtask", Status.NEW, epic.getId());
        taskManager.addSubtask(subtask);

        Subtask foundSubtask = taskManager.getSubtaskById(subtask.getId());
        assertNotNull(foundSubtask, "Subtask should not be null");
        assertEquals(subtask.getId(), foundSubtask.getId(), "Subtask id should match");
        assertEquals(subtask.getTitle(), foundSubtask.getTitle(), "Subtask names should match");
        assertEquals(subtask.getDescription(), foundSubtask.getDescription(), "Subtask descriptions should match");
        assertEquals(subtask.getStatus(), foundSubtask.getStatus(), "Subtask statuses should match");
    }


}