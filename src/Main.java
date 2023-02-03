import exception.ValidateException;
import manager.*;
import manager.interfaces.TaskManager;
import tasks.*;
import tasks.enums.StatusTypeEnum;
import tasks.enums.type;

import java.time.Instant;

public class Main {

    public static void main(String[] args) throws ValidateException {
        TaskManager inMemoryTaskManager = Managers.getDefault();

        Task task = new Task("Ноутбук", type.TASK, "Купить новый ноутбук", StatusTypeEnum.NEW,
                Instant.ofEpochSecond(1), 5);
        Task task1 = new Task("Квартира", type.TASK,"Купить новую квартиру", StatusTypeEnum.NEW,
                Instant.ofEpochSecond(4), 3);
        Epic epic = new Epic("Продукты", type.EPIC, "Купить продукты", StatusTypeEnum.NEW,
                Instant.ofEpochSecond(0), 0);
        Epic epic1 = new Epic("Проект", type.EPIC, "Сделать проект", StatusTypeEnum.NEW,
                Instant.ofEpochSecond(0), 0);
        SubTask subTask = new SubTask("Составить список",
                type.SUB_TASK, "Добавить в список все продукты", StatusTypeEnum.NEW, 2,
                Instant.ofEpochSecond(15), 4);
        SubTask subTask1 = new SubTask("Изучить теорию",
                type.SUB_TASK,"Подготовить проект к 1 проверке", StatusTypeEnum.NEW, 1,
                Instant.ofEpochSecond(20), 2);
        SubTask subTask2 = new SubTask("Пойти в магазин",
                type.SUB_TASK, "Купить все в магазине", StatusTypeEnum.NEW, 4,
                Instant.ofEpochSecond(40), 100);

        inMemoryTaskManager.createTask(task);
        inMemoryTaskManager.createTask(task1);
        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.createEpic(epic1);
        inMemoryTaskManager.createSubTask(subTask);
        inMemoryTaskManager.createSubTask(subTask1);
        inMemoryTaskManager.createSubTask(subTask2);




        System.out.println("1 проверка:" + "\n" + inMemoryTaskManager.getAllTasks() + "\n" + inMemoryTaskManager.getAllEpics()
                + "\n" + inMemoryTaskManager.getAllSubTasks() + "\n");

//        subTask.setTaskStatus(StatusTypeEnum.DONE);
//        inMemoryTaskManager.updateSubTask(subTask);

//        System.out.println("2 проверка:" + "\n" + inMemoryTaskManager.getAllTasks() + "\n" + inMemoryTaskManager.getAllEpics()
//                + "\n" + inMemoryTaskManager.getAllSubTasks() + "\n");
//
//        subTask2.setTaskStatus(StatusTypeEnum.DONE);
//        inMemoryTaskManager.updateSubTask(subTask);
//        subTask1.setTaskStatus(StatusTypeEnum.DONE);
//        inMemoryTaskManager.updateSubTask(subTask1);
//
//        System.out.println("3 проверка:" + "\n" + inMemoryTaskManager.getAllTasks() + "\n" + inMemoryTaskManager.getAllEpics()
//                + "\n" + inMemoryTaskManager.getAllSubTasks()  + "\n");

//        inMemoryTaskManager.deleteAllTasks();
//        inMemoryTaskManager.deleteAllEpics();
//        inMemoryTaskManager.deleteAllSubTasks();
//        inMemoryTaskManager.deleteTaskById(1);
//        inMemoryTaskManager.deleteEpicById(4);
//        inMemoryTaskManager.deleteSubTaskById(6);
//
//        System.out.println("4 проверка: " + inMemoryTaskManager.getAllTasks() + "\n" + inMemoryTaskManager.getAllEpics()
//                + "\n" + inMemoryTaskManager.getAllSubTasks()+ "\n");
//        System.out.println(inMemoryTaskManager.getAllSubTasksInEpic(epic) + "\n");

//        inMemoryTaskManager.getTaskById(1);//1
//        inMemoryTaskManager.getTaskById(2);//2
//        inMemoryTaskManager.getEpicById(3);//3
//        inMemoryTaskManager.getEpicById(4);//4
//        inMemoryTaskManager.getSubTaskById(5);//5
//
//        System.out.println("1 Проверка вывода истории:" + "\n" + inMemoryTaskManager.getHistory() + "\n");
//
//        inMemoryTaskManager.getTaskById(1);//1
//        inMemoryTaskManager.getTaskById(2);//2
//        inMemoryTaskManager.getEpicById(3);//3
//        inMemoryTaskManager.getEpicById(4);//4
//        inMemoryTaskManager.getSubTaskById(5);//5
//        inMemoryTaskManager.getSubTaskById(6);//5
//        inMemoryTaskManager.getSubTaskById(7);//5
//
//        System.out.println("2 Проверка вывода истории:" + "\n" + inMemoryTaskManager.getHistory() + "\n");
//
//        inMemoryTaskManager.deleteTaskById(1);
//        inMemoryTaskManager.deleteTaskById(2);
//        inMemoryTaskManager.deleteSubTaskById(5);
//
//        System.out.println("3 Проверка вывода истории:" + "\n" + inMemoryTaskManager.getHistory() + "\n");
//
//        inMemoryTaskManager.deleteEpicById(3);
//
//        System.out.println("4 Проверка вывода истории:" + "\n" + inMemoryTaskManager.getHistory());

        System.out.println("1 проверка временного вывода: " + inMemoryTaskManager.getPrioritizedTasks());
    }
}
