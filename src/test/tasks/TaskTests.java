package test.tasks;

import enums.Status;
import enums.TaskType;
import models.Epic;
import models.Subtask;
import models.Task;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskTests {

    @Test
    public void checkTaskEqual() {
        Task task1 = new Task(1, "Task", "task_description", Status.NEW, TaskType.TASK, 60, LocalDateTime.of(2000, 1, 1, 10, 10));
        Task task2 = new Task(1, "Task", "task_description", Status.NEW, TaskType.TASK, 60, LocalDateTime.of(2000, 1, 1, 10, 10));
        assertEquals(task1, task2, "Задачи не совпадают");
    }

    @Test
    public void checkSubtaskEqual() {
        Subtask subtask1 = new Subtask(1, "subtask", "subtask_description", Status.NEW, TaskType.SUBTASK, 1, 60, LocalDateTime.of(2000, 1, 1, 10, 10));
        Subtask subtask2 = new Subtask(1, "subtask", "subtask_description", Status.NEW, TaskType.SUBTASK, 1, 60, LocalDateTime.of(2000, 1, 1, 10, 10));
        assertEquals(subtask1, subtask2, "Подзадачи не совпадают");
    }

    @Test
    public void checkEpicEqual() {
        Epic epic1 = new Epic(1, "epic", "epic_description", Status.IN_PROGRESS, TaskType.EPIC, LocalDateTime.of(2000, 1, 1, 10, 10));
        Epic epic2 = new Epic(1, "epic", "epic_description", Status.IN_PROGRESS, TaskType.EPIC, LocalDateTime.of(2000, 1, 1, 10, 10));
        assertEquals(epic1, epic2, "Эпики не совпадают");
    }
}
