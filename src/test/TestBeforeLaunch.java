package test;

import enums.Status;
import implementation.InMemoryHistoryManager;
import implementation.InMemoryTaskManager;
import interfaces.HistoryManager;
import interfaces.TaskManager;
import manager.Managers;
import models.Epic;
import models.Subtask;
import models.Task;

public class TestBeforeLaunch {

    TaskManager manager = Managers.getDefault();
    HistoryManager historyManager = Managers.getDefaultHistory();

    public void create_test() {
        System.out.println("Создаем 2 задачи");
        Task taskOne = new Task("задача 1", "описание задачи 1");
        Task taskTwo = new Task("задача 2", "описание задачи 2");
        manager.createNewTasks(taskOne);
        manager.createNewTasks(taskTwo);

        System.out.println("Создаем 2 эпика");
        Epic epicOne = new Epic("Эпик 1", "описание для эпика 1");
        Epic epicTwo = new Epic("Эпик 2", "описание для эпика 2");
        manager.createEpic(epicOne);
        manager.createEpic(epicTwo);

        System.out.println("Создаем 2 подзадачи на 1 эпик, и одну подзадачу на 2 эпик");
        Subtask subtaskOne = new Subtask("подзадача 1", "подзадача для эпика 1", epicOne.getId());
        Subtask subtaskTwo = new Subtask("подзадача 2", "подзадача для эпика 1", epicOne.getId());
        Subtask subtaskThree = new Subtask("подзадача 1", "подзадача для эпика 2", epicTwo.getId());
        manager.createNewSubTask(subtaskOne);
        manager.createNewSubTask(subtaskTwo);
        manager.createNewSubTask(subtaskThree);

        System.out.println("Список эпиков:");
        manager.getALlEpics().forEach(e -> {
            System.out.println("ID: " + e.getId() + " | Название: " + e.getTitle() + " | Описание: " + e.getDescription() + " | Статус: " + e.getStatus().toString());
        });

        System.out.println("Список задач:");
        manager.getAllTasks().forEach(t -> {
            System.out.println("ID: " + t.getId() + " | Название: " + t.getTitle() + " | Описание: " + t.getDescription() + " | Статус: " + t.getStatus().toString());
        });

        System.out.println("Список подзадач:");
        manager.getAllSubTasks().forEach(s -> {
            System.out.println("ID: " + s.getId() + " | Название: " + s.getTitle() + " | Описание: " + s.getDescription() + " | Статус: " + s.getStatus().toString() + " | Принадлежит эпику: " + manager.getEpicById(s.getEpicId()).getTitle());
        });

        System.out.println("Удаляем задачу 1: ");
        manager.deleteTaskById(taskOne.getId());
        System.out.println("Список задач послу удаления");
        manager.getAllTasks().forEach(t -> {
            System.out.println("ID: " + t.getId() + " | Название: " + t.getTitle() + " | Описание: " + t.getDescription() + " | Статус: " + t.getStatus().toString());
        });

        System.out.println("Переводим все подзадачи в эпике 1 в статус DONE");
        manager.getAllSubTasksByEpicId(epicOne.getId()).forEach(e -> {
            e.setStatus(Status.DONE);
            manager.updateSubTasks(e);
        });
        System.out.println("Статус эпика 1, после перевода всех задач в статус DONE: " + manager.getEpicById(epicOne.getId()).getStatus().toString());


        System.out.println("Удаляем эпик 1: ");
        manager.deleteEpicById(epicOne.getId());
        System.out.println("Список эпиков после удаления:");
        manager.getALlEpics().forEach(e -> {
            System.out.println("ID: " + e.getId() + " | Название: " + e.getTitle() + " | Описание: " + e.getDescription() + " | Статус: " + e.getStatus().toString());
        });

        System.out.println("История просмотров: ");
        historyManager.getHistory().forEach(h -> {
            System.out.println("ID: " + h.getId() + " | Название: " + h.getTitle() + " | Описание: " + h.getDescription());
        });
    }

}
