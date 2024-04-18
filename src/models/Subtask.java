package models;

import enums.Status;
import enums.TaskType;

import java.time.LocalDateTime;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String title, String description, int epicId, TaskType taskType, long durationInMinutes, LocalDateTime startTime) {
        super(title, description, taskType, durationInMinutes, startTime);
        this.epicId = epicId;
    }

    public Subtask(int id, String title, String description, Status status, TaskType taskType, int epicId, long durationInMinutes, LocalDateTime startTime) {
        super(id, title, description, status, taskType, durationInMinutes, startTime);
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
        return getId() + separator + getTaskType() + separator + getTitle() + separator + getStatus() + separator + getDescription() + separator + getStartTime() + separator + getEndTime() + separator + getDuration().toMinutes() + separator + getEpicId();
    }
}
