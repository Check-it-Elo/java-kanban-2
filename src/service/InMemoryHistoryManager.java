package service;

import model.Task;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private Node head;
    private Node tail;
    private Map<Integer, Node> taskMap;


    public InMemoryHistoryManager() {
        this.taskMap = new HashMap<>();
    }


    // Добавление задачи
    @Override
    public void add(Task task) {
        Node newNode = new Node(task);
        removeNode(taskMap.get(task.getId()));
        linkLast(newNode);
        taskMap.put(task.getId(), newNode);
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
        List<Task> tasks = new LinkedList<>();
        Node current = head;
        while (current != null) {
            tasks.add(current.getTask());
            current = current.getNext();
        }
        return tasks;
    }

    @Override
    public void clearHistory() {
        head = null;
        tail = null;
        taskMap.clear();
    }

    // Добавление узла в конец списка
    private void linkLast(Node newNode) {
        if (tail == null) {
            head = newNode;
        } else {
            tail.setNext(newNode);
            newNode.setPrev(tail);
        }
        tail = newNode;
    }

    // Удаление узла
    private void removeNode(Node node) {
        if (node == null) {
            return;
        }

        Node prevNode = node.getPrev();
        Node nextNode = node.getNext();

        if (prevNode != null) {
            prevNode.setNext(nextNode);
        } else {
            head = nextNode;
        }

        if (nextNode != null) {
            nextNode.setPrev(prevNode);
        } else {
            tail = prevNode;
        }

        taskMap.remove(node.getTask().getId());
    }

}