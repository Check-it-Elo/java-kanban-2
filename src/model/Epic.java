package model;

import java.util.ArrayList;
import java.util.List;

import service.TaskManager;
import service.TaskType;

public class Epic extends Task {

    TaskManager manager;

    private final List<Integer> subtaskids;

    public Epic(String title, String description) {
        super(title, description, Status.NEW);
        this.subtaskids = new ArrayList<>();
    }

    public List<Integer> getSubtaskids() {
        return subtaskids;
    }


    public boolean isDone(Integer id) {
        return manager.getSubtaskById(id).getStatus() == Status.DONE;
    }

//    @Override
//    public String toString() {
//        return "model.Epic{" +
//                "title='" + getTitle() + '\'' +
//                ", description='" + getDescription() + '\'' +
//                ", id=" + getId() +
//                ", status=" + getStatus() +
//                ", subtasksCount=" + subtaskids.size() +
//                '}';
//    }


//    @Override
//    public String toString() {
//        return String.format("%s,%d,%s,%s,%s",
//                getType(), getId(), getTitle(), getDescription(), getStatus());
//    }


    @Override
    public TaskType getType() {
        return TaskType.EPIC;
    }


}
