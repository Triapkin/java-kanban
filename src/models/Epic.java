package models;

import java.util.ArrayList;

public class Epic extends Task {
    public ArrayList<Integer> subTasksId;

    public Epic(String title, String description) {
        super(title, description);
        subTasksId = new ArrayList<>();
    }

    public ArrayList<Integer> getSubTasksId() {
        return subTasksId;
    }

    public void setSubTasksId(ArrayList<Integer> subTasksId) {
        this.subTasksId = subTasksId;
    }
}
