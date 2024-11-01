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
            throw new ManagerSaveException("РћС€РёР±РєР° РїСЂРё СЃРѕС…СЂР°РЅРµРЅРёРё РґР°РЅРЅС‹С… РІ С„Р°Р№Р»", e);
        }
    }


    private String toString(Task task) {
        if (task instanceof Subtask) {
            return String.format("SUBTASK,%d,%s,%s,%s", task.getId(), task.getTitle(), task.getDescription(), task.getStatus());
        } else if (task instanceof Epic) {
            return String.format("EPIC,%d,%s,%s,%s", task.getId(), task.getTitle(), task.getDescription(), task.getStatus());
        } else {
            return String.format("TASK,%d,%s,%s,%s", task.getId(), task.getTitle(), task.getDescription(), task.getStatus());
        }
    }


    private Task fromString(String value) {
        String[] parts = value.split(",");
        if (parts.length < 5) {
            throw new IllegalArgumentException("РЎС‚СЂРѕРєР° РёРјРµРµС‚ РЅРµРґРѕСЃС‚Р°С‚РѕС‡РЅРѕ С‡Р°СЃС‚РµР№: " + value);
        }

        int id = Integer.parseInt(parts[1]);
        String title = parts[2];
        String description = parts[3];
        Status status = Status.valueOf(parts[4]);

        switch (parts[0]) {
            case "SUBTASK":
                if (parts.length < 6) {
                    throw new IllegalArgumentException("SUBTASK РЅРµРґРѕСЃС‚Р°С‚РѕС‡РЅРѕ С‡Р°СЃС‚РµР№: " + value);
                }
                int epicId = Integer.parseInt(parts[5]);
                return new Subtask(title, description, status, epicId);
            case "EPIC":
                return new Epic(title, description);
            case "TASK":
            default:
                return new Task(title, description, status);
        }
    }


    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        try {
            List<String> lines = Files.readAllLines(file.toPath());
            for (String line : lines) {
                Task task = manager.fromString(line);
                if (task instanceof Subtask) {
                    manager.addSubtask((Subtask) task);
                } else if (task instanceof Epic) {
                    manager.addEpic((Epic) task);
                } else {
                    manager.addTask(task);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("РћС€РёР±РєР° РїСЂРё Р·Р°РіСЂСѓР·РєРµ РґР°РЅРЅС‹С… РёР· С„Р°Р№Р»Р°", e);
        }
        return manager;
    }


    //Р”РѕР±Р°РІР»РµРЅРёРµ
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


    //РЈРґР°Р»РµРЅРёРµ
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


    //РћР±РЅРѕРІР»РµРЅРёРµ
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


    //РЈРґР°Р»РµРЅРёРµ РїРѕ id
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
