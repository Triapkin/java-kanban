package models;

import enums.Status;
import enums.TaskType;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subTasksId;

    public Epic(String title, String description, TaskType taskType) {
        super(title, description, taskType);
        subTasksId = new ArrayList<>();
    }

    public Epic(int id, String title, String description, Status status, TaskType taskType) {
        super(id, title, description, status, taskType);
        subTasksId = new ArrayList<>();
    }

    public List<Integer> getSubTasksId() {
        return subTasksId;
    }

    public void setSubTasksId(List<Integer> subTasksId) {
        this.subTasksId = subTasksId;
    }
}
