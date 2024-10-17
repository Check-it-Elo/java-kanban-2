import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.InMemoryTaskManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ExtendedInMemoryTaskManagerTest {

    private InMemoryTaskManager taskManager;
    private Task task1;
    private Task task2;
    private Epic epic1;
    private Subtask subtask1;

    @BeforeEach
    public void setUp() {
        taskManager = new InMemoryTaskManager();
        task1 = new Task("Task 1", "Test task 1", Status.NEW);
        task2 = new Task("Task 2", "Test task 2", Status.NEW);
        epic1 = new Epic("Epic 1", "Test epic 1");
        subtask1 = new Subtask("Subtask 1", "Test subtask 1", Status.NEW, epic1.getId());
        taskManager.addTask(task1);
        taskManager.addEpic(epic1);
    }

    // Проверка, что задача добавляется в историю
    @Test
    void testAddToHistory() {
        Task fetchedTask = taskManager.getTask(task1.getId());
        assertNotNull(fetchedTask);
        assertEquals(1, taskManager.getHistory().size());
    }

    // Проверка, что задача удаляется из истории при удалении
    @Test
    void testRemoveTaskUpdatesHistory() {
        taskManager.getTask(task1.getId());
        taskManager.deleteTaskById(task1.getId());
        assertEquals(0, taskManager.getHistory().size());
    }

    // Проверка истории версий
    @Test
    void testHistoryMaintainsOrder() {
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.getTask(task1.getId());
        taskManager.getTask(task2.getId());

        List<Task> history = taskManager.getHistory();
        assertEquals(2, history.size());
        assertEquals(task1.getId(), history.get(0).getId());
        assertEquals(task2.getId(), history.get(1).getId());
    }

}
