import org.junit.jupiter.api.*;
import service.FileBackedTaskManager;

import java.io.File;


import static org.junit.jupiter.api.Assertions.assertEquals;


public class FileBackedTaskManagerTest {

    private FileBackedTaskManager manager;
    private File tempFile;

    @BeforeEach
    public void setUp() throws Exception {
        tempFile = File.createTempFile("task_manager", ".txt");
        manager = new FileBackedTaskManager(tempFile);
    }

    @AfterEach
    public void tearDown() {
        tempFile.delete();
    }


    @Test
    public void testSaveAndLoadEmptyFile() {
        manager.save();
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        assertEquals(0, loadedManager.getAllTasks().size());
        assertEquals(0, loadedManager.getAllSubtasks().size());
        assertEquals(0, loadedManager.getAllEpics().size());
    }


//    @Test
//    public void testSaveAndLoadTaskWithDurationAndStartTime() {
//        Task task = new Task("Task 1", "Description 1", Status.NEW, Duration.ofHours(2), LocalDateTime.now());
//        manager.addTask(task);
//        manager.save();
//
//        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);
//        Task loadedTask = loadedManager.getTask(task.getId());
//
//        assertEquals(task.getId(), loadedTask.getId());
//        assertEquals(task.getTitle(), loadedTask.getTitle());
//        assertEquals(task.getDescription(), loadedTask.getDescription());
//        assertEquals(task.getStatus(), loadedTask.getStatus());
//        assertEquals(task.getDuration(), loadedTask.getDuration());
//        assertEquals(task.getStartTime(), loadedTask.getStartTime());
//    }


}
