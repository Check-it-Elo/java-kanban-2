
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import service.TaskManager;
import java.util.ArrayList;
import java.util.List;
import service.InMemoryTaskManager;

public class Main {
    public static void main(String[] args) {

        InMemoryTaskManager manager = new InMemoryTaskManager();

        manager.addTask(new Task("Задача 1", "Описание задачи 1", Status.NEW));
        manager.addTask(new Task("Задача 2", "Описание задачи 2", Status.IN_PROGRESS));

        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1", Status.NEW);
        manager.addEpic(epic1);
        manager.addSubtask(new Subtask("Подзадача 1", "Описание подзадачи 1", Status.NEW, epic1.getID()));

        printAllTasks(manager);

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
                ArrayList<Integer> subtaskIds = manager.getSubtaskIDsByEpicId(epic.getID());
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