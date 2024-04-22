package test.managers;

import enums.Status;
import enums.TaskType;
import exceptions.CheckOverException;
import interfaces.TaskManager;
import models.Epic;
import models.Subtask;
import models.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

public class TaskManagerTest<T extends TaskManager> {

    protected T manager;

    @Test
    public void getAllTasks() {
        manager.createNewTasks(new Task("Task", "task_description", TaskType.TASK, 60, LocalDateTime.now()));
        manager.createNewTasks(new Task("Task", "task_description", TaskType.TASK, 60, LocalDateTime.now().plusWeeks(1)));
        assertNotNull(manager.getAllTasks(), "Задачи не добавились в getAllTasks()");
        assertEquals(2, manager.getAllTasks().size(), "Ожидалось что в списке 2 задачи");
    }

    @Test
    public void deleteAllTasks() {
        manager.createNewTasks(new Task("Task", "task_description", TaskType.TASK, 60, LocalDateTime.now()));
        manager.createNewTasks(new Task("Task", "task_description", TaskType.TASK, 60, LocalDateTime.now().plusWeeks(1)));
        manager.deleteAllTasks();
        assertEquals(Collections.EMPTY_LIST, manager.getAllTasks(), "Не удалились все задачи");
    }

    @Test
    public void getTaskById() {
        Task task = new Task("Task", "task_description", TaskType.TASK, 60, LocalDateTime.now());
        manager.createNewTasks(task);
        assertEquals(task, manager.getTaskById(task.getId()), "Вернулась не та задача");
    }

    @Test
    public void updateTask() {
        Task task = new Task("Task", "task_description", TaskType.TASK, 60, LocalDateTime.now());
        manager.createNewTasks(task);
        Task newTask = new Task(task.getId(), "Task_upd", "task_description_upd", Status.IN_PROGRESS, TaskType.TASK, 60, LocalDateTime.now());
        manager.updateTask(newTask);
        assertEquals(task.getTitle(), newTask.getTitle(), "Титл не обновился");
        assertEquals(task.getDescription(), newTask.getDescription(), "Описание не обновилось");
        assertEquals(task.getStatus(), newTask.getStatus(), "Статус не обновился");
    }

    @Test
    public void createNewTasks() {
        Task task1 = new Task("Task", "task_description", TaskType.TASK, 60, LocalDateTime.now());
        manager.createNewTasks(task1);
        assertNotNull(task1, "Задача не создана");
        assertEquals(manager.getAllTasks().size(), 1, "Добавлено больше чем 1 задача");
    }

    @Test
    public void deleteTaskById() {
        Task task = new Task("Task", "task_description", TaskType.TASK, 60, LocalDateTime.now());
        manager.createNewTasks(task);
        manager.getTaskById(task.getId());
        manager.deleteTaskById(task.getId());
        assertEquals(0, manager.getHistory().size());
        assertEquals(Collections.EMPTY_LIST, manager.getAllTasks());
        assertEquals(Collections.EMPTY_SET, manager.getPrioritizedTasks());
    }

    @Test
    public void getALlEpics() {
        Epic epic1 = new Epic("epic_title1", "epic_description1", TaskType.EPIC);
        Epic epic2 = new Epic("epic_title2", "epic_description2", TaskType.EPIC);
        manager.createEpic(epic1);
        manager.createEpic(epic2);
        assertEquals(2, manager.getALlEpics().size(), "Не все эпики добавлены");
    }

    @Test
    public void deleteAllEpics() {
        Epic epic1 = new Epic("epic_title1", "epic_description1", TaskType.EPIC);
        Epic epic2 = new Epic("epic_title2", "epic_description2", TaskType.EPIC);
        manager.createEpic(epic1);
        manager.createEpic(epic2);
        manager.deleteAllEpics();
        assertEquals(0, manager.getALlEpics().size(), "Не все эпики удалились");
        assertNull(manager.getEpicById(epic1.getId()));
        assertNull(manager.getEpicById(epic2.getId()));
    }

    @Test
    public void getEpicById() {
        Epic epic1 = new Epic("epic_title1", "epic_description1", TaskType.EPIC);
        manager.createEpic(epic1);
        assertEquals(epic1, manager.getEpicById(epic1.getId()));
    }

    @Test
    public void createEpic() {
        Epic epic1 = new Epic("epic_title1", "epic_description1", TaskType.EPIC);
        manager.createEpic(epic1);
        assertEquals(epic1, manager.getEpicById(epic1.getId()));
        assertEquals(1, manager.getALlEpics().size(), "Эпик не добавился");
    }

    @Test
    public void updateEpic() {
        Epic epic1 = new Epic("epic_title1", "epic_description1", TaskType.EPIC);
        manager.createEpic(epic1);
        Epic epic2 = new Epic("epic_title1_new", "epic_description1_new", TaskType.EPIC);
        epic2.setId(epic1.getId());
        epic2.setStatus(Status.IN_PROGRESS);
        manager.updateEpic(epic2);
        assertEquals(epic1.getTitle(), epic2.getTitle(), "Титлы не совпадают");
        assertEquals(epic1.getDescription(), epic2.getDescription(), "Описание не совпадают");
        assertEquals(epic1.getStatus(), epic2.getStatus(), "Статусы не совпадают");
    }

    @Test
    public void deleteEpicById() {
        Epic epic1 = new Epic("epic_title1", "epic_description1", TaskType.EPIC);
        Epic epic2 = new Epic("epic_title2", "epic_description2", TaskType.EPIC);
        manager.createEpic(epic1);
        manager.createEpic(epic2);
        manager.deleteEpicById(epic1.getId());
        assertEquals(1, manager.getALlEpics().size(), "Не удалились все эпики");
        assertNull(manager.getEpicById(epic1.getId()), "Эпик 1 не удалилися");
    }

    @Test
    public void createNewSubTask() {
        Epic epic1 = new Epic("epic_title1", "epic_description1", TaskType.EPIC);
        Subtask subtask1 = new Subtask(1, "subtask", "subtask_description", Status.NEW, TaskType.SUBTASK, 1, 60, LocalDateTime.now());
        manager.createEpic(epic1);
        manager.createNewSubTask(subtask1);
        assertEquals(1, manager.getAllSubTasks().size(), "Подзадача не добавлена");
    }

    @Test
    public void getAllSubTasks() {
        Epic epic1 = new Epic("epic_title1", "epic_description1", TaskType.EPIC);
        epic1.setId(1);
        Subtask subtask1 = new Subtask("subtask1", "subtask_description1", epic1.getId(), TaskType.SUBTASK, 60, LocalDateTime.now().plusWeeks(1));
        Subtask subtask2 = new Subtask("subtask2", "subtask_description2", epic1.getId(), TaskType.SUBTASK, 60, LocalDateTime.now().plusWeeks(2));
        manager.createEpic(epic1);
        manager.createNewSubTask(subtask1);
        manager.createNewSubTask(subtask2);
        assertEquals(2, manager.getAllSubTasks().size(), "Подзадачи не добавлены");
    }

    @Test
    public void deleteAllSubTasks() {
        Epic epic1 = new Epic("epic_title1", "epic_description1", TaskType.EPIC);
        epic1.setId(1);
        Subtask subtask1 = new Subtask("subtask1", "subtask_description1", epic1.getId(), TaskType.SUBTASK, 60, LocalDateTime.now().plusWeeks(1));
        manager.createEpic(epic1);
        manager.createNewSubTask(subtask1);
        manager.deleteAllSubTasks();
        assertEquals(0, manager.getAllSubTasks().size(), "не получилось удалить все подзадачи");
        assertNull(manager.getSubTasksById(subtask1.getId()));
    }

    @Test
    public void getSubTasksById() {
        Epic epic1 = new Epic("epic_title1", "epic_description1", TaskType.EPIC);
        manager.createEpic(epic1);
        Subtask subtask1 = new Subtask("subtask1", "subtask_description1", epic1.getId(), TaskType.SUBTASK, 60, LocalDateTime.now().plusWeeks(1));
        manager.createNewSubTask(subtask1);
        assertEquals(subtask1, manager.getSubTasksById(subtask1.getId()), "вернулась не та подзадача");
    }

    @Test
    public void updateSubTasks() {
        Epic epic1 = new Epic("epic_title1", "epic_description1", TaskType.EPIC);
        manager.createEpic(epic1);
        Subtask subtask1 = new Subtask("subtask1", "subtask_description1", epic1.getId(), TaskType.SUBTASK, 60, LocalDateTime.now().plusWeeks(1));
        manager.createNewSubTask(subtask1);
        Subtask subtaskNew = new Subtask("subtask_new", "subtask_description_new", epic1.getId(), TaskType.SUBTASK, 60, LocalDateTime.now().plusWeeks(1));
        subtaskNew.setStatus(Status.DONE);
        subtaskNew.setId(subtask1.getId());
        manager.updateSubTasks(subtaskNew);
        assertEquals(subtaskNew.getTitle(), subtask1.getTitle(), "титл не совпал");
        assertEquals(subtaskNew.getDescription(), subtask1.getDescription(), "описание не совпало");
        assertEquals(Status.DONE, subtask1.getStatus(), "статус не совпал");
        assertEquals(1, manager.getAllSubTasks().size(), "добавилась лишняя подзадача");
        assertEquals(Status.DONE, manager.getEpicById(epic1.getId()).getStatus(), "не обновился статус у эпика при редактировании подзадачи");
    }

    @Test
    public void deleteSubTasksById() {
        Epic epic1 = new Epic("epic_title1", "epic_description1", TaskType.EPIC);
        manager.createEpic(epic1);
        Subtask subtask1 = new Subtask("subtask1", "subtask_description1", epic1.getId(), TaskType.SUBTASK, 60, LocalDateTime.now().plusWeeks(1));
        manager.createNewSubTask(subtask1);
        manager.deleteSubTasksById(subtask1.getId());
        assertEquals(0, manager.getAllSubTasks().size(), "количество тасок после удаления не равно 0");
        assertNull(manager.getSubTasksById(subtask1.getId()), "подзадача не удалилась");
    }

    @Test
    public void getAllSubTasksByEpicId() {
        Epic epic1 = new Epic("epic_title1", "epic_description1", TaskType.EPIC);
        manager.createEpic(epic1);
        Subtask subtask1 = new Subtask("subtask1", "subtask_description1", epic1.getId(), TaskType.SUBTASK, 60, LocalDateTime.now().plusWeeks(1));
        Subtask subtask2 = new Subtask("subtask2", "subtask_description2", epic1.getId(), TaskType.SUBTASK, 60, LocalDateTime.now().plusWeeks(2));
        Subtask subtask3 = new Subtask("subtask3", "subtask_description13", epic1.getId(), TaskType.SUBTASK, 60, LocalDateTime.now().plusWeeks(3));
        manager.createNewSubTask(subtask1);
        manager.createNewSubTask(subtask2);
        manager.createNewSubTask(subtask3);
        assertEquals(3, manager.getAllSubTasksByEpicId(epic1.getId()).size(), "не все подзадачи добавились в эпик");
    }

    @Test
    @DisplayName("Задачи в prioritizedTasks выстраиваются в правильном порядке")
    public void checkPrioritizedTask() {
        manager.createNewTasks(new Task("Task", "task_description", TaskType.TASK, 60, LocalDateTime.now()));
        manager.createNewTasks(new Task("Task", "task_description", TaskType.TASK, 60, LocalDateTime.now().plusDays(1)));
        manager.createNewTasks(new Task("Task", "task_description", TaskType.TASK, 60, LocalDateTime.now().plusDays(2)));
        TreeSet<Task> tasks = (TreeSet<Task>) manager.getPrioritizedTasks();
        assertEquals(1, tasks.first().getId(), "первый элемент не стоит на своем месте");
        assertEquals(3, tasks.last().getId(), "последний элемент не последний");
    }

    @Test
    @DisplayName("При добавлении задачи возвращается исключение о пересечении")
    public void checkOverTimeException() {
        Task task1 = new Task("Task", "task_description", TaskType.TASK, 60, LocalDateTime.now());
        Task task2 = new Task("Task", "task_description", TaskType.TASK, 60, LocalDateTime.now().plusMinutes(10));
        manager.createNewTasks(task1);
        assertThrows(CheckOverException.class, () -> {
            manager.createNewTasks(task2);
        }, "При добавлении задачи не вернулось исключение о пересечении");
        assertEquals(1, manager.getAllTasks().size(), "Количичество задач должно быть равно 1");
    }
}
