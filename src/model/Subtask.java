package model;

import service.TaskType;

public class Subtask extends Task {

    private Integer epicid;

    public Subtask(String title, String description, Status status, Integer epicid) {
        super(title, description, status);
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

//    @Override
//    public String toString() {
//        return String.format("%s,%d,%s,%s,%s",
//                getType(), getId(), getTitle(), getDescription(), getStatus());
//    }


}