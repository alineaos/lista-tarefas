package todolist.service;

import todolist.exceptions.BusinessException;
import todolist.exceptions.Validator;
import todolist.model.Task;
import todolist.model.enums.TaskStatus;
import todolist.repository.TaskRepository;
import todolist.util.ColumnsEnum;

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

    public static List<Task> findByCriteria(ColumnsEnum criteria, Object param) {
        if (criteria.getEnglishColumnName().equalsIgnoreCase("Date") && param instanceof LocalDate localDate) {
            Validator.validateDateToString(localDate);
            Validator.validateNotPastDate(localDate);
        }

        String objectToString = switch (param) {
            case LocalDate d -> d.toString();
            case TaskStatus ts -> ts.getPortugueseStatusName();
            case Integer i -> i.toString();
            case String s -> s;
            case null -> throw new BusinessException("O parâmetro não pode ser nulo.");
            default -> param.toString();
        };

        return TaskRepository.findByCriteria(criteria, objectToString);
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
        return TaskRepository.findByCriteria(ColumnsEnum.ID, String.valueOf(id)).stream()
                .filter(t -> t.getId().equals(id))
                .findFirst();
    }
}
