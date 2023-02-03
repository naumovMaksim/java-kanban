import exception.ValidateException;
import manager.inMemory.InMemoryHistoryManager;
import manager.inMemory.InMemoryTaskManager;
import manager.interfaces.HistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.enums.StatusTypeEnum;
import tasks.enums.Type;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {
    HistoryManager historyManager;
    InMemoryTaskManager inMemoryTaskManager;
    protected Task task;
    protected Epic epic;
    protected SubTask subTask;

    @BeforeEach
    void beforeEach() throws ValidateException {
        historyManager = new InMemoryHistoryManager();
        inMemoryTaskManager = new InMemoryTaskManager();

        task = new Task("Task", Type.TASK, "description", StatusTypeEnum.NEW,
                LocalDateTime.of(2023,2,3,10, 0), 1);
        epic = new Epic("Epic", Type.EPIC, "description", StatusTypeEnum.NEW,
                LocalDateTime.of(2023,2,3,11, 0), 1);
        subTask = new SubTask("SubTask", Type.SUB_TASK, "description", StatusTypeEnum.NEW, 2,
                LocalDateTime.of(2023,2,3,12, 0), 1);

        inMemoryTaskManager.createTask(task);
        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.createSubTask(subTask);
    }

    @Test
    void add() {
        historyManager.add(task);
        List<Task> list = historyManager.getHistory();
        assertEquals(task, list.get(0));
    }

    @Test
    void addTwoEqualTasksAndMustBeOneInHistory() {
        historyManager.add(task);
        historyManager.add(task);
        List<Task> list = historyManager.getHistory();
        assertEquals(1, list.size());
    }

    @Test
    void addWithWrongData() {
        historyManager.add(null);
        List<Task> list = historyManager.getHistory();
        assertEquals(0, list.size());
    }

    @Test
    void removeFirst() {
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subTask);
        List<Task> list = historyManager.getHistory();
        assertEquals(3, list.size());

        historyManager.remove(task.getId());
        list = historyManager.getHistory();
        assertEquals(2, list.size());
        assertEquals(epic, list.get(0));
        assertEquals(subTask, list.get(1));
    }

    @Test
    void removeSecond() {
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subTask);
        List<Task> list = historyManager.getHistory();
        assertEquals(3, list.size());

        historyManager.remove(epic.getId());
        list = historyManager.getHistory();
        assertEquals(2, list.size());
        assertEquals(task, list.get(0));
        assertEquals(subTask, list.get(1));
    }

    @Test
    void removeThird() {
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subTask);
        List<Task> list = historyManager.getHistory();
        assertEquals(3, list.size());

        historyManager.remove(subTask.getId());
        list = historyManager.getHistory();
        assertEquals(2, list.size());
        assertEquals(task, list.get(0));
        assertEquals(epic, list.get(1));
    }

    @Test
    void removeWithWrongData() {
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subTask);
        List<Task> list = historyManager.getHistory();
        assertEquals(3, list.size());

        historyManager.remove(0);
        list = historyManager.getHistory();
        assertEquals(3, list.size());
        assertEquals(task, list.get(0));
        assertEquals(epic, list.get(1));
        assertEquals(subTask, list.get(2));
    }

    @Test
    void getHistory() {
        historyManager.add(task);
        List<Task> list = historyManager.getHistory();
        assertNotNull(list);
    }

    @Test
    void getHistoryWithWrongData() {
        historyManager.add(null);
        List<Task> list = historyManager.getHistory();
        assertEquals(0, list.size());
    }
}