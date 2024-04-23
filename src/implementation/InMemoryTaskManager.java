package implementation;

import enums.Status;
import enums.TaskType;
import exceptions.CheckOverException;
import interfaces.HistoryManager;
import interfaces.TaskManager;
import manager.Managers;
import models.Epic;
import models.Subtask;
import models.Task;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    protected final Map<Integer, Task> tasks;
    protected final Map<Integer, Subtask> subtasks;
    protected final Map<Integer, Epic> epics;
    protected final HistoryManager historyManager;

    protected final TreeSet<Task> prioritizedTasks;
    private int taskId = 0;

    public InMemoryTaskManager() {
        tasks = new HashMap<>();
        subtasks = new HashMap<>();
        epics = new HashMap<>();
        prioritizedTasks = new TreeSet<>(Comparator.comparing(
                Task::getStartTime,
                Comparator.nullsLast(Comparator.naturalOrder())
        ));
        historyManager = Managers.getDefaultHistory();
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void deleteAllTasks() {
        tasks.values().forEach(t -> {
            historyManager.remove(t.getId());
            prioritizedTasks.remove(t);
        });
        tasks.clear();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.add(task);
        }
        return tasks.get(id);
    }

    @Override
    public void createNewTasks(Task task) {
        if (checkOver(task)) {
            throw new CheckOverException("Задача " + task.getTitle() + " пересекается с другой");
        }

        if (task.getId() == 0) {
            task.setId(createIdForTask());
        }
        tasks.put(task.getId(), task);
        prioritizedTasks.add(task);
    }


    @Override
    public void updateTask(Task newTask) {
        if (tasks.get(newTask.getId()) != null) {
            Task updatedTask = tasks.get(newTask.getId());
            updatedTask.setTitle(newTask.getTitle());
            updatedTask.setDescription(newTask.getDescription());
            updatedTask.setStatus(newTask.getStatus());
        }
    }

    @Override
    public void deleteTaskById(int id) {
        prioritizedTasks.remove(getTaskById(id));
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public List<Epic> getALlEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void deleteAllEpics() {
        deleteAllSubTasks();
        epics.values().forEach(e -> {
            historyManager.remove(e.getId());
        });
        epics.clear();
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.add(epic);
        }
        return epics.get(id);
    }

    @Override
    public void createEpic(Epic epic) {
        if (epic.getId() == 0) {
            epic.setId(createIdForTask());
        }
        epics.put(epic.getId(), epic);
    }

    @Override
    public void updateEpic(Epic newEpic) {
        if (epics.get(newEpic.getId()) != null) {
            Epic updatedEpic = epics.get(newEpic.getId());
            updatedEpic.setTitle(newEpic.getTitle());
            updatedEpic.setDescription(newEpic.getDescription());
            updatedEpic.setStatus(newEpic.getStatus());
        }
    }

    @Override
    public void deleteEpicById(int id) {
        getAllSubTasksByEpicId(id).forEach(s -> deleteSubTasksById(s.getId()));
        historyManager.remove(id);
        epics.remove(id);
    }

    @Override
    public void createNewSubTask(Subtask subtask) {
        if (checkOver(subtask)) {
            throw new CheckOverException("Задача " + subtask.getTitle() + " пересекается с другой");
        }

        if (subtask.getId() == 0) {
            subtask.setId(createIdForTask());
        }

        int epicId = subtask.getEpicId();
        Epic epic = epics.get(epicId);
        epic.getSubTasksId().add(subtask.getId());
        subtasks.put(subtask.getId(), subtask);
        prioritizedTasks.add(subtask);
        updateEpicStatus(subtask.getEpicId());

        epic.setStartTime(
                prioritizedTasks.
                        stream()
                        .filter(sub -> sub.getTaskType() == TaskType.SUBTASK)
                        .findFirst()
                        .get().getStartTime()
        );

        epic.setEndTime(
                prioritizedTasks.
                        stream()
                        .filter(sub -> sub.getTaskType() == TaskType.SUBTASK)
                        .reduce((first, second) -> second)
                        .get().getEndTime()
        );


        epic.setDuration(epic.getDuration().plusMinutes(subtask.getDuration().toMinutes()));
    }

    @Override
    public List<Subtask> getAllSubTasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void deleteAllSubTasks() {
        subtasks.values().forEach(s -> {
            historyManager.remove(s.getId());
            prioritizedTasks.remove(s);
        });
        subtasks.clear();
        epics.values().forEach(e -> {
            e.setStatus(Status.NEW);
            e.setSubTasksId(new ArrayList<>());
        });
    }

    @Override
    public Subtask getSubTasksById(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.add(subtask);
        }
        return subtasks.get(id);
    }

    @Override
    public void updateSubTasks(Subtask sub) {
        if (subtasks.get(sub.getId()) != null) {
            Subtask subtask = subtasks.get(sub.getId());
            subtask.setTitle(sub.getTitle());
            subtask.setDescription(sub.getDescription());
            subtask.setStatus(sub.getStatus());
            subtasks.put(subtask.getId(), subtask);
            updateEpicStatus(subtask.getEpicId());
        }
    }

    @Override
    public void deleteSubTasksById(int id) {
        int epicId = subtasks.get(id).getEpicId();
        epics.get(epicId).getSubTasksId().remove(Integer.valueOf(id));
        updateEpicStatus(epicId);
        historyManager.remove(id);
        prioritizedTasks.remove(getSubTasksById(id));
        subtasks.remove(id);
    }

    @Override
    public List<Subtask> getAllSubTasksByEpicId(int epicId) {
        Epic epic = epics.get(epicId);
        ArrayList<Subtask> found = new ArrayList<>();
        for (int id : epic.getSubTasksId()) {
            found.add(subtasks.get(id));
        }
        return found;
    }

    @Override
    public void updateEpicStatus(int epicId) {
        List<Subtask> subtasksList = getAllSubTasksByEpicId(epicId);
        Epic epic = epics.get(epicId);
        int countDone = 0;
        int countNew = 0;

        for (Subtask sub : subtasksList) {
            if (sub.getStatus() == Status.DONE) {
                countDone++;
            }

            if (sub.getStatus() == Status.NEW) {
                countNew++;
            }
        }

        if (subtasksList.size() == 0 || countNew == subtasksList.size()) {
            epic.setStatus(Status.NEW);
        } else if (countDone == subtasksList.size()) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }

    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    private int createIdForTask() {
        return ++taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    private boolean checkOver(Task task) {
        boolean b = false;
        for (Task t : getPrioritizedTasks()) {
            b = t.getEndTime().isAfter(task.getStartTime());
        }
        return b;
    }
}
