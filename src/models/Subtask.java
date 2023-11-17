package models;

import enums.Status;
import enums.TaskType;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String title, String description, int epicId, TaskType taskType) {
        super(title, description, taskType);
        this.epicId = epicId;
    }

    public Subtask(int id, String title, String description, Status status, TaskType taskType, int epicId) {
        super(id, title, description, status, taskType);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        String separator = ",";
        return getId() + separator + getTaskType() + separator + getTitle() + separator + getStatus() + separator + getDescription() + separator + getEpicId();
    }
}
