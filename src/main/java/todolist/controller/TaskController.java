package todolist.controller;

import todolist.model.Category;
import todolist.model.Task;
import todolist.model.enums.TaskStatus;
import todolist.service.TaskService;
import todolist.util.TaskColumn;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    public String save(String description, TaskStatus status, LocalDate date, Category category) {
        Task taskToSave = Task.builder()
                .description(description)
                .status(status)
                .date(date)
                .category(category)
                .build();

        taskService.save(taskToSave);
        return "Tarefa salva com sucesso.";
    }

    public List<Task> findAll() {
        return taskService.findAll();
    }

    public List<Task> findByDataAsc() {
        return taskService.findByDataAsc();
    }

    public <T> List<Task> findByCriteria(TaskColumn criteria, T param) {
        return taskService.findByCriteria(criteria, param);
    }

    public String updateStatus(Task task, TaskStatus newStatus) {
        taskService.updateStatus(task, newStatus);
        return "Status atualizado com sucesso.";
    }


    public String update(int taskId, String description, LocalDate date, int categoryId) {
        Category updatedCategory = Category.builder()
                .id(categoryId)
                .build();


        Task taskToUpdate = Task.builder()
                .id(taskId)
                .description(description)
                .date(date)
                .category(updatedCategory)
                .build();

        taskService.update(taskToUpdate);
        return "Tarefa atualizada com sucesso.";
    }

    public String delete(int id) {
        taskService.delete(id);
        return "A tarefa foi excluída com sucesso.";
    }

    public String deleteAll() {
       taskService.deleteAll();
        return "Todas as tarefas do banco de dados foram excluídas com sucesso.";
    }

    public Optional<Task> getTaskById(int id) {
        return taskService.getTaskById(id);
    }

    public Category getCategoryById(int categoryId) {
        return taskService.getCategoryById(categoryId);
    }

    public List<Category> getCategoriesList() {
        return taskService.getCategoriesList();
    }
}
