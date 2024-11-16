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


    //общая продолжительность всех подзадач
    @Override
    public Duration getDuration() {
        Duration totalDuration = Duration.ZERO;
        for (Integer subtaskId : subtaskids) {
            totalDuration = totalDuration.plus(manager.getSubtaskById(subtaskId).getDuration());
        }
        return totalDuration;
    }

    //самое раннее время начала среди всех подзадач
    @Override
    public LocalDateTime getStartTime() {
        LocalDateTime earliestStartTime = LocalDateTime.MAX;
        for (Integer subtaskId : subtaskids) {
            LocalDateTime subtaskStartTime = manager.getSubtaskById(subtaskId).getStartTime();
            if (subtaskStartTime.isBefore(earliestStartTime)) {
                earliestStartTime = subtaskStartTime;
            }
        }
        return earliestStartTime;
    }

    //время окончания задачи
    @Override
    public LocalDateTime getEndTime() {
        return getStartTime().plus(getDuration());
    }

}
