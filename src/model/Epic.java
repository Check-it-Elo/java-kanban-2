package model;

import java.util.ArrayList;

import service.TaskManager;

public class Epic extends Task {

    TaskManager manager;

    private final ArrayList<Integer> subtaskids;

    public Epic(String title, String description, Status status) {
        super(title, description, status);
        this.subtaskids = new ArrayList<>();
    }

    public ArrayList<Integer> getSubtaskids() {
        return subtaskids;
    }


    public boolean isDone(Integer id) {
        return manager.getSubtaskById(id).getStatus() == Status.DONE;
    }

    @Override
    public String toString() {
        return "model.Epic{" +
                "title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getid() +
                ", status=" + getStatus() +
                ", subtasksCount=" + subtaskids.size() +
                '}';
    }

}
