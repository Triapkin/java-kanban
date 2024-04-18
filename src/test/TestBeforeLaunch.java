package test;

import enums.Status;
import enums.TaskType;
import implementation.FileBackedTasksManager;
import interfaces.TaskManager;
import manager.Managers;
import models.Epic;
import models.Subtask;
import models.Task;
import utils.CsvUtils;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class TestBeforeLaunch {

    TaskManager manager = Managers.getDefault();
    TaskManager fileManager = new FileBackedTasksManager();


    public void testDate() {
        Task taskOne = new Task("задача 1", "описание задачи 1", TaskType.TASK, 480, LocalDateTime.now());
        Task taskTwo = new Task("задача 2", "описание задачи 2", TaskType.TASK, 480, LocalDateTime.now().plusWeeks(1));

        fileManager.createNewTasks(taskOne);
        fileManager.createNewTasks(taskTwo);

        Epic epicOne = new Epic("Эпик 1", "описание для эпика 1", TaskType.EPIC);

        fileManager.createEpic(epicOne);

        Subtask subtaskOne = new Subtask("подзадача 1", "подзадача для эпика 1", epicOne.getId(), TaskType.SUBTASK, 480, LocalDateTime.now().plusMonths(1));
        Subtask subtaskTwo = new Subtask("подзадача 2", "подзадача для эпика 1", epicOne.getId(), TaskType.SUBTASK, 480, LocalDateTime.now().plusWeeks(1));
        Subtask subtaskThree = new Subtask("подзадача 1", "подзадача для эпика 2", epicOne.getId(), TaskType.SUBTASK, 60, LocalDateTime.now().minusDays(2));

        fileManager.createNewSubTask(subtaskOne);
        fileManager.createNewSubTask(subtaskTwo);
        fileManager.createNewSubTask(subtaskThree);

        System.out.println("сабтаски: ");
        System.out.println(subtaskOne.getTitle() + " " + subtaskOne.getDescription() + " " + subtaskOne.getStartTime() + " " + subtaskOne.getEndTime() + " " + subtaskOne.getDuration().toMinutes());
        System.out.println(subtaskTwo.getTitle() + " " + subtaskTwo.getDescription() + " " + subtaskTwo.getStartTime() + " " + subtaskTwo.getEndTime() + " " + subtaskTwo.getDuration().toMinutes());
        System.out.println(subtaskThree.getTitle() + " " + subtaskThree.getDescription() + " " + subtaskThree.getStartTime() + " " + subtaskThree.getEndTime() + " " + subtaskThree.getDuration().toMinutes());


        System.out.println("эпики: ");
        System.out.println(epicOne.getTitle() + " " + epicOne.getDescription() + " " + epicOne.getStartTime() + " " + epicOne.getEndTime() + " " + epicOne.getDuration().toMinutes());
    }

    //    public void create_test() {
//        System.out.println("Создаем 2 задачи");
//        Task taskOne = new Task("задача 1", "описание задачи 1", TaskType.TASK, 480, LocalDateTime.now());
//        Task taskTwo = new Task("задача 2", "описание задачи 2", TaskType.TASK, 480, LocalDateTime.now().plusWeeks(1));
//        manager.createNewTasks(taskOne);
//        manager.createNewTasks(taskTwo);
//
//        System.out.println("Создаем 2 эпика");
//        Epic epicOne = new Epic("Эпик 1", "описание для эпика 1", TaskType.EPIC);
//        Epic epicTwo = new Epic("Эпик 2", "описание для эпика 2", TaskType.EPIC);
//        manager.createEpic(epicOne);
//        manager.createEpic(epicTwo);
//
//        System.out.println("Создаем 2 подзадачи на 1 эпик, и одну подзадачу на 2 эпик");
//        Subtask subtaskOne = new Subtask("подзадача 1", "подзадача для эпика 1", epicOne.getId(), TaskType.SUBTASK, 480, LocalDateTime.now().plusMonths(1));
//        Subtask subtaskTwo = new Subtask("подзадача 2", "подзадача для эпика 1", epicOne.getId(), TaskType.SUBTASK, 480, LocalDateTime.now().plusWeeks(1));
//        Subtask subtaskThree = new Subtask("подзадача 1", "подзадача для эпика 2", epicTwo.getId(), TaskType.SUBTASK, 60, LocalDateTime.now().minusDays(2));
//        manager.createNewSubTask(subtaskOne);
//        manager.createNewSubTask(subtaskTwo);
//        manager.createNewSubTask(subtaskThree);
//
//        System.out.println("Список эпиков:");
//        manager.getALlEpics().forEach(e -> {
//            System.out.println("ID: " + e.getId() + " | Название: " + e.getTitle() + " | Описание: " + e.getDescription() + " | Статус: " + e.getStatus().toString());
//        });
//
//        System.out.println("Список задач:");
//        manager.getAllTasks().forEach(t -> {
//            System.out.println("ID: " + t.getId() + " | Название: " + t.getTitle() + " | Описание: " + t.getDescription() + " | Статус: " + t.getStatus().toString());
//        });
//
//        System.out.println("Список подзадач:");
//        manager.getAllSubTasks().forEach(s -> {
//            System.out.println("ID: " + s.getId() + " | Название: " + s.getTitle() + " | Описание: " + s.getDescription() + " | Статус: " + s.getStatus().toString() + " | Принадлежит эпику: " + manager.getEpicById(s.getEpicId()).getTitle());
//        });
//
//        System.out.println("Удаляем задачу 1: ");
//        manager.deleteTaskById(taskOne.getId());
//        System.out.println("Список задач послу удаления");
//        manager.getAllTasks().forEach(t -> {
//            System.out.println("ID: " + t.getId() + " | Название: " + t.getTitle() + " | Описание: " + t.getDescription() + " | Статус: " + t.getStatus().toString());
//        });
//
//        System.out.println("Переводим все подзадачи в эпике 1 в статус DONE");
//        manager.getAllSubTasksByEpicId(epicOne.getId()).forEach(e -> {
//            e.setStatus(Status.DONE);
//            manager.updateSubTasks(e);
//        });
//        System.out.println("Статус эпика 1, после перевода всех задач в статус DONE: " + manager.getEpicById(epicOne.getId()).getStatus().toString());
//
//
//        System.out.println("Удаляем эпик 1: ");
//        manager.deleteEpicById(epicOne.getId());
//        System.out.println("Список эпиков после удаления:");
//        manager.getALlEpics().forEach(e -> {
//            System.out.println("ID: " + e.getId() + " | Название: " + e.getTitle() + " | Описание: " + e.getDescription() + " | Статус: " + e.getStatus().toString());
//        });
//
//        System.out.println("История просмотров: ");
//        manager.getHistory().forEach(h -> {
//            System.out.println("ID: " + h.getId() + " | Название: " + h.getTitle() + " | Описание: " + h.getDescription());
//        });
//    }
//
    public void test_custom_link() {
        Task taskOne = new Task("задача 1", "описание задачи 1", TaskType.TASK, 60, LocalDateTime.now());
        manager.createNewTasks(taskOne);
        Task taskTwo = new Task("задача 2", "описание задачи 2", TaskType.TASK, 120, LocalDateTime.now().plusWeeks(1));
        manager.createNewTasks(taskTwo);

        Epic epicWithSubTask = new Epic("Эпик 1", "описание для эпика 1", TaskType.EPIC);
        manager.createEpic(epicWithSubTask);

        Subtask subtaskOne = new Subtask("подзадача 1", "подзадача для эпика 1", epicWithSubTask.getId(), TaskType.SUBTASK, 60, LocalDateTime.now());
        Subtask subtaskTwo = new Subtask("подзадача 2", "подзадача для эпика 1", epicWithSubTask.getId(), TaskType.SUBTASK, 120, LocalDateTime.now().plusDays(1));
        Subtask subtaskThree = new Subtask("подзадача 3", "подзадача для эпика 1", epicWithSubTask.getId(), TaskType.SUBTASK, 180, LocalDateTime.now().plusMonths(1));

        manager.createNewSubTask(subtaskOne);
        manager.createNewSubTask(subtaskTwo);
        manager.createNewSubTask(subtaskThree);

        manager.getSubTasksById(subtaskOne.getId());
        manager.getSubTasksById(subtaskTwo.getId());
        manager.getSubTasksById(subtaskThree.getId());
        manager.getSubTasksById(subtaskThree.getId());
        manager.getSubTasksById(subtaskThree.getId());

        Epic epicWithoutSubTask = new Epic("Эпик 2", "описание для эпика 2", TaskType.EPIC);
        manager.createEpic(epicWithoutSubTask);

        manager.getTaskById(taskOne.getId());
        manager.getTaskById(taskTwo.getId());
        manager.getTaskById(taskOne.getId());
        manager.getTaskById(taskOne.getId());


        manager.getEpicById(epicWithSubTask.getId());
        manager.getEpicById(epicWithoutSubTask.getId());

        System.out.println("История просмотров до удаление задачи");
        manager.getHistory().forEach(h -> {
            System.out.println("ID: " + h.getId() + " | Название: " + h.getTitle() + " | Описание: " + h.getDescription());
        });

        manager.deleteTaskById(taskOne.getId());

        System.out.println("История просмотров после удаление задачи");
        manager.getHistory().forEach(h -> {
            System.out.println("ID: " + h.getId() + " | Название: " + h.getTitle() + " | Описание: " + h.getDescription());
        });

        System.out.println("История просмотров до удаление эпика");
        manager.getHistory().forEach(h -> {
            System.out.println("ID: " + h.getId() + " | Название: " + h.getTitle() + " | Описание: " + h.getDescription());
        });

        manager.deleteEpicById(epicWithSubTask.getId());

        System.out.println("История просмотров после удаление эпика");
        manager.getHistory().forEach(h -> {
            System.out.println("ID: " + h.getId() + " | Название: " + h.getTitle() + " | Описание: " + h.getDescription());
        });
    }

    public void test_file_utils() {
        Task taskOne = new Task("задача 1", "описание задачи 1", TaskType.TASK, 10, LocalDateTime.now());
        Task taskTwo = new Task("задача 2", "описание задачи 2", TaskType.TASK, 10, LocalDateTime.now().plusDays(10));
        Task taskThree = new Task("задача 3", "описание задачи 3", TaskType.TASK, 10, LocalDateTime.now().plusDays(20));
        fileManager.createNewTasks(taskOne);
        fileManager.createNewTasks(taskTwo);
        fileManager.createNewTasks(taskThree);

        Epic epicWithSubTask = new Epic("Эпик 1", "описание для эпика 1", TaskType.EPIC);
        fileManager.createEpic(epicWithSubTask);

        Subtask subtaskOne = new Subtask("подзадача 1", "подзадача для эпика 1", epicWithSubTask.getId(), TaskType.SUBTASK, 10, LocalDateTime.now().plusMonths(2));
        Subtask subtaskTwo = new Subtask("подзадача 2", "подзадача для эпика 1", epicWithSubTask.getId(), TaskType.SUBTASK, 10, LocalDateTime.now().plusMonths(3));

        fileManager.createNewSubTask(subtaskOne);
        fileManager.createNewSubTask(subtaskTwo);

        fileManager.getTaskById(taskTwo.getId());
        fileManager.getTaskById(taskThree.getId());
        fileManager.getEpicById(epicWithSubTask.getId());
        fileManager.getSubTasksById(subtaskOne.getId());

        Task taskTest = new Task("задача 4", "описание задачи 4", TaskType.TASK, 120, LocalDateTime.now().plusMonths(5));
        fileManager.createNewTasks(taskTest);
        fileManager.getTaskById(taskTest.getId());
    }

    public void test_backup_app_after_close() {
        File file = CsvUtils.createIfFileNotExist();
        fileManager = FileBackedTasksManager.loadFromFile(file);

        System.out.println("История после восстановления из файла");
        fileManager.getHistory().forEach(h -> {
            System.out.println("ID: " + h.getId() + " | Название: " + h.getTitle() + " | Описание: " + h.getDescription() + " | Дата начала: " + h.getStartTime() + " | Дата завершения: " + h.getEndTime() + " | Требуется времени: " + h.getDuration().toMinutes());
        });

        System.out.println("Таски после восстановления");
        System.out.println();
        manager.getAllTasks().forEach(t -> {
            System.out.println("ID: " + t.getId() + " | Название: " + t.getTitle() + " | Описание: " + t.getDescription() + " | Статус: " + t.getStatus().toString());
        });
    }

    public void checkOverTest() {
        Task taskOne = new Task("задача 1", "описание задачи 1", TaskType.TASK, 30, LocalDateTime.now());
        Task taskTwo = new Task("задача 2", "описание задачи 2", TaskType.TASK, 30, LocalDateTime.now().plusMinutes(100));
        Task taskTwo2 = new Task("задача 3", "описание задачи 3", TaskType.TASK, 30, LocalDateTime.now().plusMinutes(505));

        manager.createNewTasks(taskOne);
        manager.createNewTasks(taskTwo);
        manager.createNewTasks(taskTwo2);

        System.out.println(taskOne.getEndTime().isAfter(taskTwo.getStartTime()));
        manager.getAllTasks().forEach(t -> {
            System.out.println("ID: " + t.getId() + " | Название: " + t.getTitle() + " | Описание: " + t.getDescription() + " | Начало: " + t.getStartTime() + " | Конец: " + t.getEndTime());
        });

    }

}
