package todolist.ui;

import lombok.extern.log4j.Log4j2;
import todolist.exceptions.BusinessException;
import todolist.exceptions.Validator;

import java.util.Scanner;

@Log4j2
public class Menu {
    private final TaskMenu taskMenu;
    private final CategoryMenu categoryMenu;
    private final Scanner scanner;

    public Menu(TaskMenu taskMenu, CategoryMenu categoryMenu, Scanner scanner) {
        this.taskMenu = taskMenu;
        this.categoryMenu = categoryMenu;
        this.scanner = scanner;
    }

    public void runApplication() {
        while (true) {
            try {
                System.out.println("\n********************");
                System.out.println("* LISTA DE TAREFAS *");
                System.out.println("*  MENU PRINCIPAL  *");
                System.out.println("********************\n");

                System.out.println("Selecione o Menu desejado: ");
                System.out.println("[1] Menu de Tarefas");
                System.out.println("[2] Menu de Categorias");
                System.out.println("[0] Encerrar o programa");

                int option = Validator.parseInteger(scanner.nextLine());

                if (option == 0) return;

                switch (option) {
                    case 1 -> taskMenu.runTaskMenu();
                    case 2 -> categoryMenu.runCategoryMenu();
                    default -> System.out.println("Opção inválida.");
                }
            } catch (IllegalArgumentException | BusinessException e) {
                log.warn("Aviso: {}", e.getMessage(), e);
                System.out.println("Aviso: " + e.getMessage());
            }
        }
    }
}
