package manager;

import implementation.InMemoryHistoryManager;
import implementation.InMemoryTaskManager;
import interfaces.HistoryManager;
import interfaces.TaskManager;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
