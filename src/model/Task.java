package model;

import service.*;

import java.time.Duration;
import java.time.LocalDateTime;

public class Task implements Comparable<Task> {

    private String title;
    private String description;
    private int id;
    private Status status;
    private TaskType type;

    protected Duration duration; //полное время
    protected LocalDateTime startTime; //время начала
    protected LocalDateTime endTime; // время окончания

    public Task(String title, String description, Status status, Duration duration, LocalDateTime startTime) {
        this.title = title;
        this.description = description;
        this.id = InMemoryTaskManager.counter++;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
        this.endTime = startTime.plus(duration);
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


    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    @Override
    public int compareTo(Task other) {
        if (this.startTime == null && other.startTime == null) {
            return 0;
        }
        if (this.startTime == null) {
            return 1;
        }
        if (other.startTime == null) {
            return -1;
        }
        return this.startTime.compareTo(other.startTime);
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


}
