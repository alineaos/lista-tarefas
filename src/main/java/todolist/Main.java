package todolist;

import lombok.extern.log4j.Log4j2;
import todolist.controller.CategoryController;
import todolist.controller.TaskController;
import todolist.exceptions.BusinessException;
import todolist.exceptions.DatabaseException;
import todolist.repository.CategoryRepository;
import todolist.repository.TaskRepository;
import todolist.service.CategoryService;
import todolist.service.TaskService;
import todolist.ui.CategoryMenu;
import todolist.ui.Menu;
import todolist.ui.TaskMenu;

import java.util.Scanner;

@Log4j2
public class Main {

    public static void main(String[] args) {
        log.info("Sistema iniciado.");

        CategoryRepository categoryRepository = new CategoryRepository();
        TaskRepository taskRepository = new TaskRepository();

        CategoryService categoryService = new CategoryService(categoryRepository);
        TaskService taskService = new TaskService(taskRepository, categoryService);

        CategoryController categoryController = new CategoryController(categoryService);
        TaskController taskController = new TaskController(taskService);

        Scanner scanner = new Scanner(System.in);
        TaskMenu taskMenu = new TaskMenu(taskController, scanner);
        CategoryMenu categoryMenu = new CategoryMenu(categoryController, scanner);

        Menu mainMenu = new Menu(taskMenu, categoryMenu, scanner);

        try {
            mainMenu.runApplication();
        } catch (DatabaseException e) {
            log.error("Erro no banco de dados: {}", e.getMessage(), e);
            System.out.println("Erro no banco de dados: " + e.getMessage());
        } finally {
            scanner.close();
            System.out.println("Sistema encerrado.");
        }
    }
}
