package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.io.*;
import java.nio.file.Files;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;

    public FileBackedTaskManager(File file) {
        super();
        this.file = file;
    }


    public void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Task task : getAllTasks()) {
                writer.write(toString(task));
                writer.newLine();
            }
            for (Subtask subtask : getAllSubtasks()) {
                writer.write(toString(subtask));
                writer.newLine();
            }
            for (Epic epic : getAllEpics()) {
                writer.write(toString(epic));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении данных в файл»", e);
        }
    }


//    private String toString(Task task) {
//        if (task instanceof Subtask) {
//            return String.format("SUBTASK,%d,%s,%s,%s", task.getId(), task.getTitle(), task.getDescription(), task.getStatus());
//        } else if (task instanceof Epic) {
//            return String.format("EPIC,%d,%s,%s,%s", task.getId(), task.getTitle(), task.getDescription(), task.getStatus());
//        } else {
//            return String.format("TASK,%d,%s,%s,%s", task.getId(), task.getTitle(), task.getDescription(), task.getStatus());
//        }
//    }

    public String toString(Task task) {
        return task.toString();
    }


    private Task fromString(String value) {
        String[] parts = value.split(",");
        if (parts.length < 5) {
            throw new IllegalArgumentException("Недостаточно данных: " + value);
        }

        int id = Integer.parseInt(parts[1]);
        String title = parts[2];
        String description = parts[3];
        Status status = Status.valueOf(parts[4]);

        switch (parts[0]) {
            case "SUBTASK":
                if (parts.length < 6) {
                    throw new IllegalArgumentException("Некорректный идентификатор задачи: " + value);
                }
                int epicId = Integer.parseInt(parts[5]);
                Subtask subtask = new Subtask(title, description, status, epicId);
                subtask.setId(id);
                return subtask;
            case "EPIC":
                Epic epic = new Epic(title, description);
                epic.setId(id);
                return epic;
            case "TASK":
            default:
                Task task = new Task(title, description, status);
                task.setId(id);
                return task;
        }
    }


    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        try {
            List<String> lines = Files.readAllLines(file.toPath());
            int maxId = 0;
            for (String line : lines) {
                Task task = manager.fromString(line);
                if (task instanceof Subtask) {
                    manager.addSubtask((Subtask) task);
                } else if (task instanceof Epic) {
                    manager.addEpic((Epic) task);
                } else {
                    manager.addTask(task);
                }

                if (task.getId() > maxId) {
                    maxId = task.getId();
                }
            }
            InMemoryTaskManager.counter = maxId + 1;

        } catch (IOException e) {
            throw new RuntimeException("Ошибка при загрузке данных из файла: ", e);
        }
        return manager;
    }


    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }


    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }


    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        updateSubtask(subtask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        updateEpic(epic);
        save();
    }


    @Override
    public void deleteTaskById(int id) {
        deleteTaskById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        deleteSubtaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        deleteEpicById(id);
        save();
    }


}
