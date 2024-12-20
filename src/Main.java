
import model.Epic;
import model.Subtask;
import model.Task;
import service.TaskManager;

import java.util.ArrayList;
import java.util.List;


public class Main {
    public static void main(String[] args) {

    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        ArrayList<Task> tasks = manager.getAllTasks();
        if (tasks.isEmpty()) {
            System.out.println("Нет доступных задач.");
        } else {
            for (Task task : tasks) {
                System.out.println(task);
            }
        }

        System.out.println("\nЭпики:");
        ArrayList<Epic> epics = manager.getAllEpics();
        if (epics.isEmpty()) {
            System.out.println("Нет доступных эпиков.");
        } else {
            for (Epic epic : epics) {
                System.out.println(epic);
                List<Integer> subtaskIds = manager.getSubtaskidsByEpicId(epic.getId());
                for (int subtaskId : subtaskIds) {
                    Subtask subtask = manager.getSubtask(subtaskId);
                    if (subtask != null) {
                        System.out.println("--> " + subtask);
                    }
                }
            }
        }

        System.out.println("\nПодзадачи:");
        ArrayList<Subtask> subtasks = manager.getAllSubtasks();
        if (subtasks.isEmpty()) {
            System.out.println("Нет доступных подзадач.");
        } else {
            for (Subtask subtask : subtasks) {
                System.out.println(subtask);
            }
        }

        System.out.println("\nИстория задач:");
        List<Task> history = manager.getHistory();
        if (history.isEmpty()) {
            System.out.println("Нет доступной истории.");
        } else {
            for (Task task : history) {
                System.out.println(task);
            }
        }
    }


}