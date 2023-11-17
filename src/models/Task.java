package models;

import enums.Status;
import enums.TaskType;

public class Task {
    private int id;
    private String title;
    private String description;
    private Status status;
    private TaskType taskType;

    public Task(String title, String description, TaskType taskType) {
        this.title = title;
        this.description = description;
        this.taskType = taskType;
        status = Status.NEW;
    }

    public Task(int id, String title, String description, Status status, TaskType taskType) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.taskType = taskType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    @Override
    public String toString() {
        String separator = ",";
        return getId() + separator + getTaskType() + separator + getTitle() + separator + getStatus() + separator + getDescription();
    }
}
