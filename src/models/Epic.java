package models;

import enums.Status;
import enums.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subTasksId;
    private LocalDateTime endTime;

    public Epic(String title, String description, TaskType taskType) {
        // из конструктора Task LocalDateTime обязателен, в ТЗ ничего не сказано - проставляю момент создания
        // так же из ТЗ параметр Duration и LocalDateTime долны высчитываться, не понятный момент :(
        super(title, description, taskType, Duration.ZERO.toMinutes(), LocalDateTime.now());
        subTasksId = new ArrayList<>();
    }

    public Epic(int id, String title, String description, Status status, TaskType taskType, LocalDateTime startTime) {
        super(id, title, description, status, taskType, Duration.ZERO.toMinutes(), startTime);
        subTasksId = new ArrayList<>();
    }

    public List<Integer> getSubTasksId() {
        return subTasksId;
    }

    public void setSubTasksId(List<Integer> subTasksId) {
        this.subTasksId = subTasksId;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
