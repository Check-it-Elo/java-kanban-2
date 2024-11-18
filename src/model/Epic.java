package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import service.TaskManager;
import service.TaskType;

public class Epic extends Task {

    TaskManager manager;

    private final List<Integer> subtaskids;

    public Epic(String title, String description) {
        super(title, description, Status.NEW, Duration.ZERO, LocalDateTime.now());
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

    @Override
    public TaskType getType() {
        return TaskType.EPIC;
    }


    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }


}
