import exception.ManagerSaveException;
import exception.ValidateException;
import manager.file.FileBackedTasksManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.io.File;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager>{
    File file;

    @BeforeEach
    void beforeEach() throws ValidateException {
        file = new File("resources/testFiles/fileWithTest.csv");
        manager = new FileBackedTasksManager(file);
        tasksCreation();
    }

    @Test
    void saveAndLoadFromFile() {
        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(file);
        List<Task> list = fileBackedTasksManager.getAllTasks();
        manager.getTaskById(1);
        manager.getEpicById(2);

        assertNotNull(list);
        assertEquals(task.getId(), list.get(0).getId());
        assertEquals(task.getType(), list.get(0).getType());
        assertEquals(task.getName(), list.get(0).getName());
        assertEquals(task.getDescription(), list.get(0).getDescription());
        assertEquals(task.getAllTasksStatus(), list.get(0).getAllTasksStatus());
        assertEquals(task.getStartTime(), list.get(0).getStartTime());
        assertEquals(task.getDuration(), list.get(0).getDuration());
        assertEquals(task.getEndTime(), list.get(0).getEndTime());
        assertEquals(List.of(task, epic), manager.getHistory());
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
        assertEquals(Collections.emptyList(), manager.getHistory());
    }

    @Test
    void saveAndLoadFromFileWithoutSubTasks() {
        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(file);
        List<Task> list = fileBackedTasksManager.getAllTasks();
        manager.deleteSubTaskById(3);
        manager.getTaskById(1);
        manager.getEpicById(2);

        assertEquals(1, list.size());
        assertEquals(Collections.emptyList(), epic.getSubtaskIds());
        assertEquals(List.of(task, epic), manager.getHistory());
    }

    @Test
    void saveAndLoadFromFileWithWrongData() {
        manager.deleteAllTasks();
        manager.deleteAllEpics();
        manager.deleteAllSubTasks();
        final ManagerSaveException exception = assertThrows(ManagerSaveException.class,
            () -> {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(Path.of("").toFile());
        fileBackedTasksManager.save();
            });
        assertEquals(" (Невозможно создать файл, так как он уже существует)", exception.getMessage());
    }
}