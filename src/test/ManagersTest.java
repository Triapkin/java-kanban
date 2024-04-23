package test;

import interfaces.HistoryManager;
import interfaces.TaskManager;
import manager.Managers;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ManagersTest {
    @Test
    public void getDefault() {
        TaskManager taskManager = Managers.getDefault();
        assertNotNull(taskManager, "default task manager is null");
        assertEquals(0, taskManager.getAllTasks().size(), "Неверное количество задач.");
        assertEquals(0, taskManager.getAllSubTasks().size(), "Неверное количество подзадач.");
        assertEquals(0, taskManager.getALlEpics().size(), "Неверное количество эпиков.");
        assertEquals(0, taskManager.getHistory().size(), "Неверное количество задач в истории.");
    }

    @Test
    public void getDefaultHistory() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        assertNotNull(historyManager, "default task manager is null");
        assertEquals(0, historyManager.getHistory().size(), "Неверное количество задач в истории.");
    }
}
