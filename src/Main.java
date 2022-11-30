import Manager.TaskManagerMemory;
import Tasks.Epic;
import Tasks.Status;
import Tasks.SubTask;
import Tasks.Task;


public class Main {

    public static void main(String[] args) {
        TaskManagerMemory taskManagerMemory = new TaskManagerMemory();
        Task task = new Task("Ноутбук", "Купить новый ноутбук", Status.NEW);
        Task task1 = new Task("Квартира", "Купить новую квартиру", Status.NEW);
        Epic epic = new Epic("Продукты", "Купить продукты", Status.NEW);
        Epic epic1 = new Epic("Проект", "Сделать проект", Status.NEW);
        SubTask subTask = new SubTask("Составить список"
                , "Добавить в список все продукты", Status.NEW);
        SubTask subTask1 = new SubTask("Изучить теорию"
                , "Подготовить проект к 1 проверке", Status.NEW);
        SubTask subTask2 = new SubTask("Пойти в магазин", "Купить все в магазине", Status.NEW);

        taskManagerMemory.createTask(task);
        taskManagerMemory.createTask(task1);
        taskManagerMemory.createEpic(epic);
        taskManagerMemory.createEpic(epic1);
        taskManagerMemory.createSubTusk(subTask, epic.getId());
        taskManagerMemory.createSubTusk(subTask1, epic1.getId());
        taskManagerMemory.createSubTusk(subTask2, epic.getId());
        subTask.setTaskStatus(Status.DONE);
        taskManagerMemory.updateSubTusk(subTask);
//        taskManagerMemory.deleteAllTasks();
//        taskManagerMemory.deleteAllEpics();
//        taskManagerMemory.deleteAllSubTusks();
//        taskManagerMemory.deleteTaskById(1);
//        taskManagerMemory.deleteEpicById(4);
//        taskManagerMemory.deleteSubTaskById(6);

        System.out.println(taskManagerMemory.getAllTasks() + "\n" + taskManagerMemory.getAllEpics()
                + "\n" + taskManagerMemory.getAllSubTusks());
        System.out.println(taskManagerMemory.getAllSubTasksInEpic(epic1));
    }
}
