package model;

import java.util.ArrayList;

import service.TaskManager;

public class Epic extends Task {

    TaskManager manager;

    private final ArrayList<Integer> subtaskIDs;

    public Epic(String title, String description, Status status) {
        super(title, description, status);
        this.subtaskIDs = new ArrayList<>();
    }

    public ArrayList<Integer> getSubtaskIDs() {
        return subtaskIDs;
    }


    public boolean isDone(Integer id) {
        return manager.getSubtaskById(id).getStatus() == Status.DONE;
    }

    @Override
    public String toString() {
        return "model.Epic{" +
                "title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", ID=" + getID() +
                ", status=" + getStatus() +
                ", subtasksCount=" + subtaskIDs.size() +
                '}';
    }

}
