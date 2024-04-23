package test.tasks;

import enums.Status;
import enums.TaskType;
import interfaces.TaskManager;
import manager.Managers;
import models.Epic;
import models.Subtask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

public class EpicTests {
    private TaskManager manager;
    private Epic epic = new Epic("epic_title", "epic_description", TaskType.EPIC);
    private Subtask subtask1 = new Subtask("subtask1_title", "subtask1_description", 1, TaskType.SUBTASK, 30, LocalDateTime.now());
    private Subtask subtask2 = new Subtask("subtask2_title", "subtask2_description", 1, TaskType.SUBTASK, 30, LocalDateTime.now().plusDays(1));

    @BeforeEach
    void setUp() {
        manager = Managers.getDefault();
        manager.createEpic(epic);
        epic.setId(1);
    }

    @Test
    public void epicStatusEqualNewThenAllSubtaskStatusIsNew() {
        manager.createNewSubTask(subtask1);
        manager.createNewSubTask(subtask2);
        assertEquals(epic.getStatus(), Status.NEW, "Статус эпика не NEW");
    }

    @Test
    public void epicStatusEqualDoneThenAllSubtaskStatusIsDone() {
        subtask1.setStatus(Status.DONE);
        subtask2.setStatus(Status.DONE);
        manager.createNewSubTask(subtask1);
        manager.createNewSubTask(subtask2);
        assertEquals(epic.getStatus(), Status.DONE, "Статус эпика не DONE");
    }

    @Test
    public void epicStatusEqualInProgressThenSubtaskStatusIsDoneAndNew() {
        subtask1.setStatus(Status.NEW);
        subtask2.setStatus(Status.DONE);
        manager.createNewSubTask(subtask1);
        manager.createNewSubTask(subtask2);
        assertEquals(epic.getStatus(), Status.IN_PROGRESS, "Статус эпика не IN_PROGRESS");
    }

    @Test
    public void epicStatusEqualInProgressThenSubtaskStatusIsInProgress() {
        subtask1.setStatus(Status.IN_PROGRESS);
        subtask2.setStatus(Status.IN_PROGRESS);
        manager.createNewSubTask(subtask1);
        manager.createNewSubTask(subtask2);
        assertEquals(epic.getStatus(), Status.IN_PROGRESS, "Статус эпика не IN_PROGRESS");
    }
}