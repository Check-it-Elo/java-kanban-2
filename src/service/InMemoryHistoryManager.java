package service;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    //private List<Task> history;

    private Node head;
    private Node tail;
    private HashMap<Integer, Node> taskMap;


    public InMemoryHistoryManager() {
        //this.history = new ArrayList<>();
        this.taskMap = new HashMap<>();
    }

    // Добавление узла в конец списка
    private void linkLast(Node newNode) {
        if (tail == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
    }

    // Удаление узла
    private void removeNode(Node node) {
        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            head = node.next;
        }
        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev;
        }
        taskMap.remove(node.task.getID());
    }

    // Получение всех задач в ArrayList
    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        Node current = head;
        while (current != null) {
            tasks.add(current.task);
            current = current.next;
        }
        return tasks;
    }

    // Добавление задачи
    @Override
    public void add(Task task) {
        Node newNode = new Node(task);

        if (taskMap.containsKey(task.getID())) {
            Node existingNode = taskMap.get(task.getID());
            removeNode(existingNode);
        }

        linkLast(newNode);
        taskMap.put(task.getID(), newNode);
    }

    @Override
    public void remove(int id) {
        if (taskMap.containsKey(id)) {
            Node nodeToRemove = taskMap.get(id);
            removeNode(nodeToRemove);
        }
    }

    @Override
    public List<Task> getHistory() {
        List<Task> tasks = new ArrayList<>();
        Node current = head;
        while (current != null) {
            tasks.add(current.task);
            current = current.next;
        }
        return tasks;
    }

}