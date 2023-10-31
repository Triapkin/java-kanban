package implementation;

import interfaces.HistoryManager;
import models.Task;
import utils.CustomLinkedList;

import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private CustomLinkedList history;

    public InMemoryHistoryManager() {
        history = new CustomLinkedList();
    }

    @Override
    public void add(Task task) {
        if (task != null) {
            remove(task.getId());
            history.linkLast(task);
        }
    }

    @Override
    public void remove(int id) {
        history.remove(id);
    }

    @Override
    public List<Task> getHistory() {
        return history.getTasks();
    }
}
