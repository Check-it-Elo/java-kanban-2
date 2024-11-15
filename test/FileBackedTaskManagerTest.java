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


}
