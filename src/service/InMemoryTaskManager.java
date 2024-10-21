package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {

    private Map<Integer, Task> tasks;
    protected Map<Integer, Subtask> subtasks;
    private Map<Integer, Epic> epics;

    private HistoryManager historyManager;

    public static int counter = 0;

    public InMemoryTaskManager() {
        this.tasks = new HashMap<>();
        this.subtasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.historyManager = Managers.getDefaultHistory();
    }


    //Добавление
    @Override
    public void addTask(Task task) {
        task.setId(counter++);
        tasks.put(task.getId(), task);
    }

    @Override
    public void addSubtask(Subtask subtask) {
        Integer epicId = subtask.getLinkEpic();
        if (epicId != null) {
            subtask.setId(counter++);
            subtasks.put(subtask.getId(), subtask);
            updateEpicStatus(epicId);
        } else {
            System.out.println("Подзадача не ссылается ни на один эпик");
        }
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setId(counter++);
        epics.put(epic.getId(), epic);
    }

    //Получение
    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    //Удаление
    @Override
    public void deleteAllTasks() {
        List<Integer> deletedTaskIds = new ArrayList<>(tasks.keySet());

        tasks.clear();

        for (Integer taskId : deletedTaskIds) {
            tasks.remove(taskId);
            historyManager.remove(taskId);
        }
    }

    @Override
    public void deleteAllSubtasks() {
        List<Integer> deletedSubtaskIds = new ArrayList<>(subtasks.keySet());

        for (Epic epic : epics.values()) {
            List<Integer> subtaskids = epic.getSubtaskids();

            for (Integer id : deletedSubtaskIds) {
                if (subtaskids.contains(id)) {
                    subtaskids.remove(id);
                    historyManager.remove(id);
                }
            }
            updateEpicStatus(epic.getId());
        }

        for (Integer id : deletedSubtaskIds) {
            subtasks.remove(id);
        }
    }

    @Override
    public void deleteAllEpics() {
        List<Integer> deletedEpicIds = new ArrayList<>(epics.keySet());

        for (Integer epicId : deletedEpicIds) {
            Epic epic = epics.get(epicId);

            for (Integer subtaskId : epic.getSubtaskids()) {
                subtasks.remove(subtaskId);
                historyManager.remove(subtaskId);
            }

            historyManager.remove(epicId);
            epics.remove(epicId);
        }
    }

    //Получение по id
    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.add(subtask);
        }
        return subtask;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.add(epic);
        }
        return epic;
    }

    //Обновление
    @Override
    public void updateTask(Task task) {
        if (epics.containsValue(task)) {
            System.err.println("Ошибка");
        } else {
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        Integer subtaskId = subtask.getLinkEpic();

        if (subtaskId != null) {
            ArrayList<Subtask> epicSubtasks = new ArrayList<>(subtasks.values());
            epicSubtasks.remove(subtask);
            epicSubtasks.add(subtask);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            Epic existingEpic = epics.get(epic.getId());

            existingEpic.setTitle(epic.getTitle());
            existingEpic.setDescription(epic.getDescription());
        }
    }

    //Удаление по id
    @Override
    public void deleteTaskById(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
            historyManager.remove(id);
        }
    }

    @Override
    public void deleteSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            int epicId = subtask.getLinkEpic();
            Epic epic = epics.get(epicId);

            if (epic != null) {
                List<Integer> epicSubtaskIds = epic.getSubtaskids();
                if (epicSubtaskIds.remove((Integer) id)) {
                    updateEpicStatus(epicId);
                }
            }

            subtasks.remove(id);
            historyManager.remove(id);
        }
    }

    @Override
    public void deleteEpicById(int id) {
        Epic epic = getEpicById(id);
        if (epic != null) {
            for (Integer subtaskId : epic.getSubtaskids()) {
                deleteSubtaskById(subtaskId);
            }
            epics.remove(id);
            historyManager.remove(id);
        }
    }


    //ДОПОЛНИТЕЛЬНЫЕ МЕТОДЫ

    // Получение списка всех подзадач определённого эпика
    @Override
    public List<Integer> getSubtaskidsByEpicId(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            return epic.getSubtaskids();
        }
        return List.of();
    }

    @Override
    public void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic != null) {
            List<Integer> epicSubtasks = epic.getSubtaskids();
            if (epicSubtasks.isEmpty()) {
                epic.setStatus(Status.NEW);
            } else {
                boolean allDone = true;
                boolean anyInProgress = false;

                for (Integer subtask : epicSubtasks) {
                    if (subtasks.get(subtask).getStatus() == Status.NEW) {
                        allDone = false;
                    } else if (subtasks.get(subtask).getStatus() == Status.IN_PROGRESS) {
                        anyInProgress = true;
                    }
                }

                if (allDone) {
                    epic.setStatus(Status.DONE);
                } else if (anyInProgress) {
                    epic.setStatus(Status.IN_PROGRESS);
                } else {
                    epic.setStatus(Status.NEW);
                }
            }
        }
    }

    @Override
    public Task getTask(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.add(subtask);
        }
        return subtask;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.add(epic);
        }
        return epic;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }


}
