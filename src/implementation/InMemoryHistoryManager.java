package implementation;

import interfaces.HistoryManager;
import models.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private List<Task> history;
    private final int MAX_HISTORY_COUNTS = 10;

    public InMemoryHistoryManager() {
        this.history = new ArrayList<>();
    }

    @Override
    public void addToHistory(Task task) {
        history.add(task);
        if (history.size() > MAX_HISTORY_COUNTS) {
            history.remove(0);
        }
    }

    @Override
    public List<Task> getHistory() {
        return List.copyOf(history);
    }
}
