package manager;

import enums.Status;
import models.Epic;
import models.Subtask;
import models.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    public HashMap<Integer, Task> tasks = new HashMap<>();
    public HashMap<Integer, Subtask> subtasks = new HashMap<>();
    public HashMap<Integer, Epic> epics = new HashMap<>();
    int taskId = 1;
    int subTaskId = 1;
    int epicId = 1;

    public Manager() {
    }

    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> list = new ArrayList<>();
        tasks.forEach((k, v) -> list.add(v));
        return list;
    }

    public void deleteALlTasks() {
        tasks.clear();
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public void createNewTasks(Task task) {
        task.setId(taskId++);
        tasks.put(task.getId(), task);
    }

    public void updateTask(Task newTask) {
        Task updatedTask = tasks.get(newTask.getId());
        updatedTask.setTitle(newTask.getTitle());
        updatedTask.setDescription(newTask.getDescription());
        updatedTask.setStatus(newTask.getStatus());
        tasks.put(updatedTask.getId(), updatedTask);
    }

    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    public ArrayList<Epic> getALlEpics() {
        ArrayList<Epic> list = new ArrayList<>();
        epics.forEach((k, v) -> list.add(v));
        return list;
    }

    public void deleteAllEpics() {
        epics.clear();
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public void createEpic(Epic epic) {
        epic.setId(epicId++);
        epics.put(epic.getId(), epic);
    }

    public void updateEpic(Epic newEpic) {
        Epic updatedEpic = epics.get(newEpic.getId());
        updatedEpic.setTitle(newEpic.getTitle());
        updatedEpic.setDescription(newEpic.getDescription());
        updatedEpic.setStatus(newEpic.getStatus());
        epics.put(updatedEpic.getId(), updatedEpic);
    }

    public void deleteEpicById(int id) {
        getAllSubTasksByEpicId(id).clear();
        epics.remove(id);
    }

    public void createNewSubTask(Subtask subtask) {
        subtask.setId(subTaskId++);
        int epicId = subtask.getEpicId();
        Epic epic = epics.get(epicId);
        epic.subTasksId.add(subtask.getId());
        subtasks.put(subtask.getId(), subtask);
        updateEpicStatus(subtask.getEpicId());
    }

    public ArrayList<Subtask> getAllSubTasks() {
        ArrayList<Subtask> list = new ArrayList<>();
        subtasks.forEach((k, v) -> list.add(v));
        return list;
    }

    public void deleteAllSubTasks() {
        subtasks.clear();
    }

    public Subtask getSubTasksById(int id) {
        return subtasks.get(id);
    }

    public void updateSubTasks(Subtask sub) {
        Subtask subtask = subtasks.get(sub.getId());
        subtask.setTitle(sub.getTitle());
        subtask.setDescription(sub.getDescription());
        subtask.setStatus(sub.getStatus());
        subtasks.put(subtask.getId(), subtask);
        updateEpicStatus(subtask.getEpicId());
    }

    public void deleteSubTasksById(int id) {
        subtasks.remove(id);
    }

    public ArrayList<Subtask> getAllSubTasksByEpicId(int epicId) {
        Epic epic = epics.get(epicId);
        ArrayList<Integer> subTaskIds = epic.getSubTasksId();
        ArrayList<Subtask> founded = new ArrayList<>();
        for (Subtask sub : subtasks.values()) {
            for (int id : subTaskIds) {
                if (sub.getId() == id) {
                    founded.add(sub);
                }
            }
        }
        return founded;
    }

    public void updateEpicStatus(int epicId) {
        ArrayList<Subtask> list = getAllSubTasksByEpicId(epicId);
        Epic epic = epics.get(epicId);
        int countDone = 0;
        int countNew = 0;

        for (Subtask sub : list) {
            if (sub.getStatus() == Status.DONE) {
                countDone++;
            }

            if (sub.getStatus() == Status.NEW) {
                countNew++;
            }
        }

        if (list.size() == 0 || countNew == list.size()) {
            System.out.println();
            epic.setStatus(Status.NEW);
        } else if (countDone == list.size()) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }

    }
}
