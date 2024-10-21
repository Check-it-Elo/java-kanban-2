package service;

import model.Task;

public class Node {
    private Task task;
    private Node prev;
    private Node next;

    Node(Task task) {
        this.task = task;
    }

    //ГЕТТЕРЫ
    public Task getTask() {
        return task;
    }

    public Node getPrev() {
        return prev;
    }

    public Node getNext() {
        return next;
    }

    //СЕТТЕРЫ
    public void setTask(Task task) {
        this.task = task;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }

    public void setNext(Node next) {
        this.next = next;
    }

}


