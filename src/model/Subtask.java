package model;

public class Subtask extends Task {

    private Integer epicID;

    public Subtask(String title, String description, Status status, Integer epicID) {
        super(title, description, status);
        this.epicID = epicID;
    }

    public Integer getLinkEpic() {
        return epicID;
    }


    @Override
    public String toString() {
        return "model.Subtask{" +
                "title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", ID=" + getID() +
                ", status=" + getStatus() +
                '}';
    }

}