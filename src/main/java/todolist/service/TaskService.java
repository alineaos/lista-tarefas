package todolist.service;

import todolist.exceptions.Validator;
import todolist.model.Category;
import todolist.model.Task;
import todolist.model.enums.TaskStatus;
import todolist.repository.TaskRepository;
import todolist.util.TaskColumn;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class TaskService {
    private final TaskRepository taskRepository;
    private final CategoryService categoryService;

    public TaskService(TaskRepository taskRepository, CategoryService categoryService) {
        this.taskRepository = taskRepository;
        this.categoryService = categoryService;
    }

    public void save(Task task) {
        Validator.notPastDate(task.getDate());
        taskRepository.save(task);
    }

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public <T> List<Task> findByCriteria(TaskColumn criteria, T param) {
        if (param instanceof LocalDate localDate) {
            Validator.formatDate(localDate);
            Validator.notPastDate(localDate);
        }

        String selectedParam = paramFormatter(param);

        return taskRepository.findByCriteria(criteria, selectedParam);
    }

    public List<Task> findByDataAsc() {
        return taskRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Task::getDate))
                .toList();
    }

    public void updateStatus(Task task, TaskStatus newStatus) {
        taskRepository.updateStatus(task, newStatus);
    }

    public void update(Task task) {
        taskRepository.update(task);
    }

    public void delete(int id) {
        taskRepository.delete(id);
    }

    public void deleteAll() {
        taskRepository.deleteAll();
    }

    public Optional<Task> getTaskById(int id) {
        return taskRepository.findByCriteria(TaskColumn.ID, String.valueOf(id)).stream()
                .filter(t -> t.getId().equals(id))
                .findFirst();
    }

    private <T> String paramFormatter(T param) {
        return switch (param) {
            case LocalDate d -> d.toString();
            case TaskStatus ts -> ts.getPortugueseStatusName();
            case Integer i -> i.toString();
            default -> param.toString();
        };
    }

    public Category getCategoryById(int id) {
        return categoryService.getCategoryById(id);
    }

    public List<Category> getCategoriesList() {
        return categoryService.findAll();
    }
}
