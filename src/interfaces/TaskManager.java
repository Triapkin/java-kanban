package interfaces;

import models.Epic;
import models.Subtask;
import models.Task;

import java.util.List;

public interface TaskManager {

    List<Task> getAllTasks();

    void deleteAllTasks();

    Task getTaskById(int id);

    void createNewTasks(Task task);

    void updateTask(Task newTask);

    void deleteTaskById(int id);

    List<Epic> getALlEpics();

    void deleteAllEpics();

    Epic getEpicById(int id);

    void createEpic(Epic epic);

    void updateEpic(Epic newEpic);

    void deleteEpicById(int id);

    void createNewSubTask(Subtask subtask);

    List<Subtask> getAllSubTasks();

    void deleteAllSubTasks();

    Subtask getSubTasksById(int id);

    void updateSubTasks(Subtask sub);

    void deleteSubTasksById(int id);

    List<Subtask> getAllSubTasksByEpicId(int epicId);

    void updateEpicStatus(int epicId);

    List<Task> getHistory();
}
