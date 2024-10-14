package service;

import model.Task;
import model.Subtask;
import model.Epic;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    // Методы для управления задачами
    public void addTask(Task task);
    public void deleteTaskById(int id);
    public Task getTaskById(int id);
    public ArrayList<Task> getAllTasks();
    public void deleteAllTasks();
    public void updateTask(Task task);

    // Методы для управления подзадачами
    public void addSubtask(Subtask subtask);
    public void deleteSubtaskById(int id);
    public Subtask getSubtaskById(int id);
    public ArrayList<Subtask> getAllSubtasks();
    public void deleteAllSubtasks();
    public void updateSubtask(Subtask subtask);

    // Методы для управления эпиками
    public void addEpic(Epic epic);
    public void deleteEpicById(int id);
    public Epic getEpicById(int id);
    public ArrayList<Epic> getAllEpics();
    public void deleteAllEpics();
    public void updateEpic(Epic epic);

    // Методы для обновления статусов
    public void updateEpicStatus(int epicId);
    public ArrayList<Integer> getSubtaskIDsByEpicId(int id);

    Task getTask(int id);
    Subtask getSubtask(int id);
    Epic getEpic(int id);

    List<Task> getHistory();
}
