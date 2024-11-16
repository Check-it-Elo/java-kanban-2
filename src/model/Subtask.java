package model;

import service.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {

    private Integer epicid;

    public Subtask(String title, String description, Status status, Integer epicid, Duration duration,
                   LocalDateTime startTime) {
        super(title, description, status, duration, startTime);
        this.epicid = epicid;
    }

    public Integer getLinkEpic() {
        return epicid;
    }

//    @Override
//    public String toString() {
//        return "model.Subtask{" +
//                "title='" + getTitle() + '\'' +
//                ", description='" + getDescription() + '\'' +
//                ", id=" + getId() +
//                ", status=" + getStatus() +
//                '}';
//    }


    @Override
    public TaskType getType() {
        return TaskType.SUBTASK;
    }


}