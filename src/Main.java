import manager.*;
import tasks.*;

public class Main {

    public static void main(String[] args) {
        TaskManager inMemoryTaskManager = Managers.getDefault();

        Task task = new Task("Ноутбук", "Купить новый ноутбук", Status.NEW);
        Task task1 = new Task("Квартира", "Купить новую квартиру", Status.NEW);
        Epic epic = new Epic("Продукты", "Купить продукты", Status.NEW);
        Epic epic1 = new Epic("Проект", "Сделать проект", Status.NEW);
        SubTask subTask = new SubTask("Составить список"
                , "Добавить в список все продукты", Status.NEW, 3);
        SubTask subTask1 = new SubTask("Изучить теорию"
                , "Подготовить проект к 1 проверке", Status.NEW, 3);
        SubTask subTask2 = new SubTask("Пойти в магазин", "Купить все в магазине", Status.NEW, 3);

        inMemoryTaskManager.createTask(task);
        inMemoryTaskManager.createTask(task1);
        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.createEpic(epic1);
        inMemoryTaskManager.createSubTask(subTask);
        inMemoryTaskManager.createSubTask(subTask1);
        inMemoryTaskManager.createSubTask(subTask2);

        System.out.println("1 проверка:" + "\n" + inMemoryTaskManager.getAllTasks() + "\n" + inMemoryTaskManager.getAllEpics()
                + "\n" + inMemoryTaskManager.getAllSubTasks() + "\n");

        subTask.setTaskStatus(Status.DONE);
        inMemoryTaskManager.updateSubTask(subTask);

        System.out.println("2 проверка:" + "\n" + inMemoryTaskManager.getAllTasks() + "\n" + inMemoryTaskManager.getAllEpics()
                + "\n" + inMemoryTaskManager.getAllSubTasks() + "\n");

        subTask2.setTaskStatus(Status.DONE);
        inMemoryTaskManager.updateSubTask(subTask);
        subTask1.setTaskStatus(Status.DONE);
        inMemoryTaskManager.updateSubTask(subTask1);

        System.out.println("3 проверка:" + "\n" + inMemoryTaskManager.getAllTasks() + "\n" + inMemoryTaskManager.getAllEpics()
                + "\n" + inMemoryTaskManager.getAllSubTasks()  + "\n");

/*        inMemoryTaskManager.deleteAllTasks();
        inMemoryTaskManager.deleteAllEpics();
        inMemoryTaskManager.deleteAllSubTasks();
        inMemoryTaskManager.deleteTaskById(1);
        inMemoryTaskManager.deleteEpicById(4);
        inMemoryTaskManager.deleteSubTaskById(7);

        System.out.println("4 проверка: " + inMemoryTaskManager.getAllTasks() + "\n" + inMemoryTaskManager.getAllEpics()
                + "\n" + inMemoryTaskManager.getAllSubTasks()+ "\n");
        System.out.println(inMemoryTaskManager.getAllSubTasksInEpic(epic) + "\n");*/

        inMemoryTaskManager.getTaskById(1);//1
        inMemoryTaskManager.getTaskById(2);//2
        inMemoryTaskManager.getEpicById(3);//3
        inMemoryTaskManager.getEpicById(4);//4
        inMemoryTaskManager.getSubTaskById(5);//5

        System.out.println("1 Проверка вывода истории:" + "\n" + inMemoryTaskManager.getHistory() + "\n");

        inMemoryTaskManager.getTaskById(1);//1
        inMemoryTaskManager.getTaskById(2);//2
        inMemoryTaskManager.getEpicById(3);//3
        inMemoryTaskManager.getEpicById(4);//4
        inMemoryTaskManager.getSubTaskById(5);//5
        inMemoryTaskManager.getSubTaskById(6);//5
        inMemoryTaskManager.getSubTaskById(7);//5

        System.out.println("2 Проверка вывода истории:" + "\n" + inMemoryTaskManager.getHistory() + "\n");

        inMemoryTaskManager.deleteTaskById(1);
        inMemoryTaskManager.deleteTaskById(2);
        inMemoryTaskManager.deleteSubTaskById(5);

        System.out.println("3 Проверка вывода истории:" + "\n" + inMemoryTaskManager.getHistory() + "\n");

        inMemoryTaskManager.deleteEpicById(3);

        System.out.println("4 Проверка вывода истории:" + "\n" + inMemoryTaskManager.getHistory());


    }
}
