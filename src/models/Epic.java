package models;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subTasksId;

    public Epic(String title, String description) {
        super(title, description);
        subTasksId = new ArrayList<>();
    }

    public List<Integer> getSubTasksId() {
        return subTasksId;
    }

    public void setSubTasksId(List<Integer> subTasksId) {
        this.subTasksId = subTasksId;
    }
}
