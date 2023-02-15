import manager.*;
import server.HttpTaskManager;
import server.KVServer;
import tasks.*;
import tasks.enums.StatusTypeEnum;
import tasks.enums.Type;

import java.io.IOException;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) throws IOException {
        KVServer kvServer = Managers.getKVserver();
        kvServer.start();
        HttpTaskManager httpTaskManager = Managers.getDefault();

        Task task = new Task("Ноутбук", Type.TASK, "Купить новый ноутбук", StatusTypeEnum.NEW,
                LocalDateTime.of(2023,2,3,10, 0), 5);
        httpTaskManager.createTask(task);

        httpTaskManager.saveFile();
//        httpTaskManager.loadFile();

//        Task task1 = new Task("Квартира", Type.TASK,"Купить новую квартиру", StatusTypeEnum.NEW,
//                LocalDateTime.of(2023,2,3,10, 0), 3);
//        Epic epic = new Epic("Продукты", Type.EPIC, "Купить продукты", StatusTypeEnum.NEW,
//                LocalDateTime.of(2023,2,3,10, 0), 0);
//        Epic epic1 = new Epic("Проект", Type.EPIC, "Сделать проект", StatusTypeEnum.NEW,
//                LocalDateTime.of(2023,2,3,10, 0), 0);
//        SubTask subTask = new SubTask("Составить список",
//                Type.SUB_TASK, "Добавить в список все продукты", StatusTypeEnum.NEW, 2,
//                LocalDateTime.of(2023,2,3,10, 0), 4);
//        SubTask subTask1 = new SubTask("Изучить теорию",
//                Type.SUB_TASK,"Подготовить проект к 1 проверке", StatusTypeEnum.NEW, 1,
//                LocalDateTime.of(2023,2,3,10, 0), 2);
//        SubTask subTask2 = new SubTask("Пойти в магазин",
//                Type.SUB_TASK, "Купить все в магазине", StatusTypeEnum.NEW, 4,
//                LocalDateTime.of(2023,2,3,10, 0), 100);

//        inMemoryTaskManager.createTask(task);
//        inMemoryTaskManager.createTask(task1);
//        inMemoryTaskManager.createEpic(epic);
//        inMemoryTaskManager.createEpic(epic1);
//        inMemoryTaskManager.createSubTask(subTask);
//        inMemoryTaskManager.createSubTask(subTask1);
//        inMemoryTaskManager.createSubTask(subTask2);
//
//
//
//
//        System.out.println("1 проверка:" + "\n" + inMemoryTaskManager.getAllTasks() + "\n" + inMemoryTaskManager.getAllEpics()
//                + "\n" + inMemoryTaskManager.getAllSubTasks() + "\n");

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
//
//        System.out.println("1 проверка временного вывода: " + inMemoryTaskManager.getprioritizedTaskss());
    }
}
