package implementation;

import enums.TaskType;
import exceptions.ManagerSaveException;
import interfaces.HistoryManager;
import models.Epic;
import models.Subtask;
import models.Task;
import utils.CsvUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private final File defaultFile;
    private final String newLine = System.lineSeparator();

    public FileBackedTasksManager() {
        this.defaultFile = CsvUtils.createIfFileNotExist();
    }

    public FileBackedTasksManager(File defaultFile) {
        this.defaultFile = defaultFile;
    }

    public void save() {
        try (FileWriter fileWriter = new FileWriter(defaultFile)) {
            fileWriter.write("id,type,name,status,description,epic\n");
            for (Task task : getAllTasks()) {
                fileWriter.write(task.toString());
                fileWriter.write(newLine);
            }

            for (Epic epic : getALlEpics()) {
                fileWriter.write(epic.toString());
                fileWriter.write(newLine);
            }

            for (Subtask subtask : getAllSubTasks()) {
                fileWriter.write(subtask.toString());
                fileWriter.write(newLine);
            }

            fileWriter.write(newLine);
            fileWriter.write(historyToString(historyManager));
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось сохранить");
        }
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public void createNewTasks(Task task) {
        super.createNewTasks(task);
        save();
    }

    @Override
    public void updateTask(Task newTask) {
        super.updateTask(newTask);
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void updateEpic(Epic newEpic) {
        super.updateEpic(newEpic);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void createNewSubTask(Subtask subtask) {
        super.createNewSubTask(subtask);
        save();
    }

    @Override
    public void deleteAllSubTasks() {
        super.deleteAllSubTasks();
        save();
    }

    @Override
    public Subtask getSubTasksById(int id) {
        Subtask subtask = super.getSubTasksById(id);
        save();
        return subtask;
    }

    @Override
    public void updateSubTasks(Subtask sub) {
        super.updateSubTasks(sub);
        save();
    }

    @Override
    public void deleteSubTasksById(int id) {
        super.deleteSubTasksById(id);
        save();
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        List<String> lines = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            while (bufferedReader.ready()) {
                lines.add(bufferedReader.readLine());
            }
        } catch (IOException ex) {
            throw new ManagerSaveException("Не удалось прочитать из файла.");
        }


        for (int i = 1; i < lines.size() - 2; i++) {
            Task task = CsvUtils.fromString(lines.get(i));
            if (task != null) {
                if (task.getTaskType() == TaskType.TASK) {
                    fileBackedTasksManager.createNewTasks(task);
                } else if (task.getTaskType() == TaskType.EPIC) {
                    fileBackedTasksManager.createEpic((Epic) task);
                } else {
                    fileBackedTasksManager.createNewSubTask((Subtask) task);
                }
            }
        }

        int maxId = getMaxId(lines.subList(1, lines.size() - 2));
        fileBackedTasksManager.setTaskId(maxId++);

        List<Integer> ids = historyFromString(lines.get(lines.size() - 1));
        for (Integer id : ids) {
            fileBackedTasksManager.historyManager.add(fileBackedTasksManager.getTask(id));
        }
        return fileBackedTasksManager;
    }

    private static String historyToString(HistoryManager manager) {
        List<Task> tasks = manager.getHistory();
        return tasks.stream().map(s -> String.valueOf(s.getId())).collect(Collectors.joining(","));
    }

    private static List<Integer> historyFromString(String value) {
        String[] lines = value.split(",");
        return Arrays.stream(lines).map(Integer::parseInt).collect(Collectors.toList());
    }

    private static int getMaxId(List<String> strings) {
        return strings.stream().map(s -> s.split(",")).mapToInt(m -> Integer.parseInt(m[0])).max().orElseThrow(NoSuchElementException::new);
    }

    private Task getTask(int id) {
        if (tasks.get(id) != null) {
            return tasks.get(id);
        } else if (epics.get(id) != null) {
            return epics.get(id);
        }
        return subtasks.get(id);
    }
}
