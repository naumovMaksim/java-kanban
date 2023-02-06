package manager.inmem;

import exception.ValidateException;
import manager.TaskManagerTest;
import manager.inMemory.InMemoryTaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.enums.StatusTypeEnum;
import tasks.enums.Type;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    void beforeEach() {
        manager = new InMemoryTaskManager();
        tasksCreation();
    }

    @Test
    void validateTaskPriority() {
        Task task1 = new Task("Task", Type.TASK, "description", StatusTypeEnum.NEW,
                LocalDateTime.of(2023, 2, 3, 15, 0), 1);
        manager.createTask(task1);
        List<Task> list = new ArrayList<>(manager.getprioritizedTaskss());
        Assertions.assertEquals(list.get(0), task);
    }

    @Test
    void validateTaskPriorityWithWrongData() {
        final ValidateException exception = assertThrows(ValidateException.class,
                () -> {
                    Task task1 = new Task("Task", Type.TASK, "description", StatusTypeEnum.NEW,
                            LocalDateTime.of(2023, 2, 3, 6, 0), 1);
                    manager.createTask(task1);
                });
        assertEquals("Задачи пересекаются с id=1 c 2023-02-03T06:00 по 2023-02-03T06:01", exception.getMessage());
    }

    @Test
    void updateEpicTime() {
        Epic epic1 = new Epic("Epic", Type.EPIC, "description", StatusTypeEnum.NEW,
                LocalDateTime.of(2023, 2, 3, 15, 0), 1);
        manager.createEpic(epic1);
        LocalDateTime firstStartTime = LocalDateTime.of(2023, 2, 3, 15, 0);
        assertEquals(firstStartTime, epic1.getStartTime());
        SubTask subTask1 = new SubTask("SubTask", Type.SUB_TASK, "description", StatusTypeEnum.NEW, 4,
                LocalDateTime.of(2023, 2, 3, 16, 0), 1);
        manager.createSubTask(subTask1);
        LocalDateTime secondStartTime = LocalDateTime.of(2023, 2, 3, 16, 0);
        assertEquals(secondStartTime, epic1.getStartTime());
    }

    @Test
    void updateEpicTimeWithWrongData() {
        Epic epic1 = new Epic("Epic", Type.EPIC, "description", StatusTypeEnum.NEW,
                LocalDateTime.of(2023, 2, 3, 18, 0), 1);
        manager.createEpic(epic1);
        LocalDateTime firstStartTime = LocalDateTime.of(2023, 2, 3, 18, 0);
        assertEquals(firstStartTime, epic1.getStartTime());
        SubTask subTask1 = new SubTask("SubTask", Type.SUB_TASK, "description", StatusTypeEnum.NEW, 0,
                LocalDateTime.of(2023, 2, 3, 19, 0), 1);
        manager.createSubTask(subTask1);
        assertEquals(firstStartTime, epic1.getStartTime());
    }
}