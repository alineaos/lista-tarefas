package todolist.ui;

import todolist.exceptions.Validator;

import java.util.Scanner;

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
            System.out.println("********************");
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
        }
    }
}
