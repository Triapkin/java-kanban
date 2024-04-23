package test.managers;

import enums.TaskType;
import implementation.FileBackedTasksManager;
import implementation.InMemoryTaskManager;
import models.Epic;
import models.Subtask;
import models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.CsvUtils;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTasksManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    private File file;

    @BeforeEach
    public void setUp() {
        file = CsvUtils.createIfFileNotExist();
        manager = new FileBackedTasksManager();
    }


    @Test
    public void correctlySaveAndLoadTasks() {
        Epic epic1 = new Epic("epic_title1", "epic_description1", TaskType.EPIC);
        manager.createEpic(epic1);
        Task task1 = new Task("Task", "task_description", TaskType.TASK, 60, LocalDateTime.now());
        Subtask subtask1 = new Subtask("subtask1", "subtask_description1", epic1.getId(), TaskType.SUBTASK, 60, LocalDateTime.now().plusWeeks(1));
        manager.createNewTasks(task1);
        manager.createNewSubTask(subtask1);

        manager.getTaskById(task1.getId());
        manager.getEpicById(epic1.getId());
        manager.getSubTasksById(subtask1.getId());

        manager = FileBackedTasksManager.loadFromFile(file);
        assertEquals(List.of(task1), manager.getAllTasks());
        assertEquals(List.of(subtask1), manager.getAllSubTasks());
        assertEquals(List.of(epic1), manager.getALlEpics());
    }
}
