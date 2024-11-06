package model;

import service.*;

public class Task {

    private String title;
    private String description;
    private int id;
    private Status status;
    private TaskType type;


    public Task(String title, String description, Status status) {
        this.title = title;
        this.description = description;
        this.id = InMemoryTaskManager.counter++;
        this.status = status;
    }


    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }


    public void changeTaskToInProgress() {
        status = Status.IN_PROGRESS;
    }

    public void changeTaskToDone() {
        status = Status.DONE;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Task task = (Task) obj;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

//    @Override
//    public String toString() {
//        return "model.Task{" +
//                "title='" + title + '\'' +
//                ", description='" + description + '\'' +
//                ", id=" + id +
//                ", status=" + status +
//                '}';
//    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public TaskType getType() {
        return TaskType.TASK;
    }


//    @Override
//    public String toString() {
//        return String.format("%s,%d,%s,%s,%s",
//                getType(), getId(), getTitle(), getDescription(), getStatus());
//    }


}
