import exception.ValidateException;
import manager.inMemory.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.enums.StatusTypeEnum;
import tasks.enums.Type;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
    Epic epic;
    SubTask subTask1;
    SubTask subTask2;
    @BeforeEach
    public void beforeEach() throws ValidateException {
        epic = new Epic("Epic", Type.EPIC, "description", StatusTypeEnum.NEW,
                LocalDateTime.of(2023,2,3,10, 0), 1);
        subTask1 = new SubTask("SubTask1",
                Type.SUB_TASK, "description", StatusTypeEnum.NEW, 1,
                LocalDateTime.of(2023,2,3,11, 0), 1);
        subTask2 = new SubTask("SubTask2",
                Type.SUB_TASK, "description", StatusTypeEnum.NEW, 1,
                LocalDateTime.of(2023,2,3,12, 0), 1);
        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.createSubTask(subTask1);
        inMemoryTaskManager.createSubTask(subTask2);
    }

    @Test
    public void emptyEpic() {
        inMemoryTaskManager.deleteAllSubTasks();
        List<Integer> subTaskIds = inMemoryTaskManager.getEpicById(1).getSubtaskIds();

        assertTrue(subTaskIds.isEmpty());
    }

    @Test
    public void epicShouldBeNewWhenAllSubTasksInEpicNew() {
        subTask1.setTaskStatus(StatusTypeEnum.NEW);
        subTask2.setTaskStatus(StatusTypeEnum.NEW);

        assertEquals(StatusTypeEnum.NEW,epic.getAllTasksStatus());
    }

    @Test
    public void epicShouldBeDoneWhenAllSubTasksInEpicDone() {
        subTask1.setTaskStatus(StatusTypeEnum.DONE);
        subTask2.setTaskStatus(StatusTypeEnum.DONE);
        inMemoryTaskManager.updateEpic(epic);

        assertEquals(StatusTypeEnum.DONE, epic.getAllTasksStatus());
    }

    @Test
    public void epicShouldBeInProgressWhenSubTasksInEpicDoneAndNew() {
        subTask1.setTaskStatus(StatusTypeEnum.NEW);
        subTask2.setTaskStatus(StatusTypeEnum.DONE);
        inMemoryTaskManager.updateEpic(epic);

        assertEquals(StatusTypeEnum.IN_PROGRESS, epic.getAllTasksStatus());
    }

    @Test
    public void epicShouldBeInProgressWhenAllSubTasksInEpicInProgress() {
        subTask1.setTaskStatus(StatusTypeEnum.IN_PROGRESS);
        subTask2.setTaskStatus(StatusTypeEnum.IN_PROGRESS);
        inMemoryTaskManager.updateEpic(epic);

        assertEquals(StatusTypeEnum.IN_PROGRESS, epic.getAllTasksStatus());
    }
}