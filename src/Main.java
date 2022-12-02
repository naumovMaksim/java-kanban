import manager.TaskManagerMemory;
import tasks.*;

public class Main {

    public static void main(String[] args) {
        TaskManagerMemory taskManagerMemory = new TaskManagerMemory();
        Task task = new Task("Ноутбук", "Купить новый ноутбук", Status.NEW);
        Task task1 = new Task("Квартира", "Купить новую квартиру", Status.NEW);
        Epic epic = new Epic("Продукты", "Купить продукты", Status.NEW);
        Epic epic1 = new Epic("Проект", "Сделать проект", Status.NEW);
        SubTask subTask = new SubTask("Составить список"
                , "Добавить в список все продукты", Status.NEW, 3);
        SubTask subTask1 = new SubTask("Изучить теорию"
                , "Подготовить проект к 1 проверке", Status.NEW, 4);
        SubTask subTask2 = new SubTask("Пойти в магазин", "Купить все в магазине", Status.NEW, 3);

        taskManagerMemory.createTask(task);
        taskManagerMemory.createTask(task1);
        taskManagerMemory.createEpic(epic);
        taskManagerMemory.createEpic(epic1);
        taskManagerMemory.createSubTask(subTask);
        taskManagerMemory.createSubTask(subTask1);
        taskManagerMemory.createSubTask(subTask2);

        System.out.println("1 проверка: " + taskManagerMemory.getAllTasks() + "\n" + taskManagerMemory.getAllEpics()
                + "\n" + taskManagerMemory.getAllSubTasks() + "\n");

        subTask.setTaskStatus(Status.DONE);
        taskManagerMemory.updateSubTask(subTask);

        System.out.println("2 проверка: " + taskManagerMemory.getAllTasks() + "\n" + taskManagerMemory.getAllEpics()
                + "\n" + taskManagerMemory.getAllSubTasks() + "\n");

        subTask2.setTaskStatus(Status.DONE);
        taskManagerMemory.updateSubTask(subTask);
        subTask1.setTaskStatus(Status.DONE);
        taskManagerMemory.updateSubTask(subTask1);

        System.out.println("3 проверка: " + taskManagerMemory.getAllTasks() + "\n" + taskManagerMemory.getAllEpics()
                + "\n" + taskManagerMemory.getAllSubTasks()  + "\n");

//        taskManagerMemory.deleteAllTasks();
//        taskManagerMemory.deleteAllEpics();
//        taskManagerMemory.deleteAllSubTasks();
       taskManagerMemory.deleteTaskById(1);
       taskManagerMemory.deleteEpicById(4);
       taskManagerMemory.deleteSubTaskById(7);

        System.out.println("4 проверка: " + taskManagerMemory.getAllTasks() + "\n" + taskManagerMemory.getAllEpics()
                + "\n" + taskManagerMemory.getAllSubTasks()+ "\n");
        System.out.println(taskManagerMemory.getAllSubTasksInEpic(epic));
    }
}
