package todolist.service;

import todolist.exceptions.Validator;
import todolist.model.Task;
import todolist.model.enums.TaskStatus;
import todolist.repository.TaskRepository;
import todolist.util.TaskColumn;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class TaskService {
    public static void saveTask(Task task) {
        Validator.validateNotPastDate(task.getDate());
        TaskRepository.insert(task);
    }

    public static List<Task> findAll() {
        return TaskRepository.findAll();
    }

    public static <T>List<Task> findByCriteria(TaskColumn criteria, T param) {
        if (param instanceof LocalDate localDate) {
            Validator.validateDateToString(localDate);
            Validator.validateNotPastDate(localDate);
        }

        String selectedParam = paramFormatter(param);

        return TaskRepository.findByCriteria(criteria, selectedParam);
    }

    public static List<Task> findByDataAsc() {
        return TaskRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Task::getDate))
                .toList();
    }

    public static void updateStatus(Task task, TaskStatus newStatus) {
        TaskRepository.updateStatus(task, newStatus);
    }

    public static void update(Task task) {
        TaskRepository.update(task);
    }

    public static void delete(int id) {
        TaskRepository.delete(id);
    }

    public static void deleteAll() {
        TaskRepository.deleteAll();
    }

    public static Optional<Task> getTaskById(int id){
        return TaskRepository.findByCriteria(TaskColumn.ID, String.valueOf(id)).stream()
                .filter(t -> t.getId().equals(id))
                .findFirst();
    }

    private static <T> String paramFormatter(T param){
        return switch (param) {
            case LocalDate d -> d.toString();
            case TaskStatus ts -> ts.getPortugueseStatusName();
            case Integer i -> i.toString();
            default -> param.toString();
        };
    }
}
