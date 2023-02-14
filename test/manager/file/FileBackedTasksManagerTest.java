package manager.file;

import exception.ManagerSaveException;
import manager.TaskManagerTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.io.File;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    File file;

    @BeforeEach
    void beforeEach() {
        file = new File("resources/testFiles/fileWithTest.csv");
        manager = new FileBackedTasksManager();
        tasksCreation();
    }

    @Test
    void saveAndLoadFromFile() {
        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(file);
        List<Task> list = fileBackedTasksManager.getAllTasks();
        manager.getTaskById(1);
        manager.getEpicById(2);

        assertNotNull(list);
        Assertions.assertEquals(task, list.get(0));
        Assertions.assertEquals(List.of(task, epic), manager.getHistory());
    }

    @Test
    void saveAndLoadFromFileWithEmptyTasks() {
        manager.deleteAllTasks();
        manager.deleteAllEpics();
        manager.deleteAllSubTasks();
        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(file);
        List<Task> list = fileBackedTasksManager.getAllTasks();

        assertEquals(Collections.emptyList(), list);
    }

    @Test
    void saveAndLoadFromFileWithEmptyHistory() {
        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(file);
        List<Task> list = fileBackedTasksManager.getAllTasks();

        assertNotNull(list);
        Assertions.assertEquals(Collections.emptyList(), manager.getHistory());
    }

    @Test
    void saveAndLoadFromFileWithoutSubTasks() {
        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(file);
        List<Task> list = fileBackedTasksManager.getAllTasks();
        manager.deleteSubTaskById(3);
        manager.getTaskById(1);
        manager.getEpicById(2);

        assertEquals(1, list.size());
        Assertions.assertEquals(Collections.emptyList(), epic.getSubtaskIds());
        Assertions.assertEquals(List.of(task, epic), manager.getHistory());
    }

    @Test
    void saveAndLoadFromFileWithWrongData() {
        manager.deleteAllTasks();
        manager.deleteAllEpics();
        manager.deleteAllSubTasks();
        final ManagerSaveException exception = assertThrows(ManagerSaveException.class,
                () -> {
                    FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager();
                    fileBackedTasksManager.save();
                });
        assertEquals(" (Невозможно создать файл, так как он уже существует)", exception.getMessage());
    }
}