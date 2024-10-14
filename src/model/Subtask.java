package model;

public class Subtask extends Task {

    private Integer epicid;

    public Subtask(String title, String description, Status status, Integer epicid) {
        super(title, description, status);
        this.epicid = epicid;
    }

    public Integer getLinkEpic() {
        return epicid;
    }


    @Override
    public String toString() {
        return "model.Subtask{" +
                "title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getid() +
                ", status=" + getStatus() +
                '}';
    }

}