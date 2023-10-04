package manager;

import enums.Status;
import models.Epic;
import models.Subtask;
import models.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Subtask> subtasks;
    private HashMap<Integer, Epic> epics;
    int taskId = 0;

    public Manager() {
        tasks = new HashMap<>();
        subtasks = new HashMap<>();
        epics = new HashMap<>();
    }

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public void createNewTasks(Task task) {
        task.setId(createIdForTask());
        tasks.put(task.getId(), task);
    }

    public void updateTask(Task newTask) {
        if (tasks.get(newTask.getId()) != null) {
            Task updatedTask = tasks.get(newTask.getId());
            updatedTask.setTitle(newTask.getTitle());
            updatedTask.setDescription(newTask.getDescription());
            updatedTask.setStatus(newTask.getStatus());
        }
    }

    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    public ArrayList<Epic> getALlEpics() {
        return new ArrayList<>(epics.values());
    }

    public void deleteAllEpics() {
        subtasks.clear();
        epics.clear();
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public void createEpic(Epic epic) {
        epic.setId(createIdForTask());
        epics.put(epic.getId(), epic);
    }

    public void updateEpic(Epic newEpic) {
        if (epics.get(newEpic.getId()) != null) {
            Epic updatedEpic = epics.get(newEpic.getId());
            updatedEpic.setTitle(newEpic.getTitle());
            updatedEpic.setDescription(newEpic.getDescription());
            updatedEpic.setStatus(newEpic.getStatus());
        }
    }

    public void deleteEpicById(int id) {
        getAllSubTasksByEpicId(id).forEach(s -> deleteSubTasksById(s.getId()));
        epics.remove(id);
    }

    public void createNewSubTask(Subtask subtask) {
        subtask.setId(createIdForTask());
        int epicId = subtask.getEpicId();
        Epic epic = epics.get(epicId);
        epic.getSubTasksId().add(subtask.getId());
        subtasks.put(subtask.getId(), subtask);
        updateEpicStatus(subtask.getEpicId());
    }

    public ArrayList<Subtask> getAllSubTasks() {
        return new ArrayList<>(subtasks.values());
    }

    public void deleteAllSubTasks() {
        subtasks.clear();
        epics.values().forEach(e -> {
            e.setStatus(Status.NEW);
            e.setSubTasksId(new ArrayList<>());
        });
    }

    public Subtask getSubTasksById(int id) {
        return subtasks.get(id);
    }

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

    public void deleteSubTasksById(int id) {
        subtasks.remove(id);
    }

    public ArrayList<Subtask> getAllSubTasksByEpicId(int epicId) {
        Epic epic = epics.get(epicId);
        ArrayList<Subtask> found = new ArrayList<>();
        for (int id : epic.getSubTasksId()) {
            found.add(subtasks.get(id));
        }
        return found;
    }

    public void updateEpicStatus(int epicId) {
        ArrayList<Subtask> subtasksList = getAllSubTasksByEpicId(epicId);
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

    private int createIdForTask() {
        return ++taskId;
    }
}
