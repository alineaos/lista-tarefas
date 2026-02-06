package todolist.ui;

import todolist.controller.CategoryController;
import todolist.controller.TaskController;
import todolist.exceptions.Validator;
import todolist.model.enums.TaskStatus;
import todolist.util.TaskColumn;

import java.util.Scanner;

public class Menu {
    public static void showTaskMenu() {
        System.out.println("Selecione a opção desejada:");
        System.out.println("[1] Criar tarefa");
        System.out.println("[2] Listar todas as tarefas");
        System.out.println("[3] Listar as tarefas pela data mais próxima");
        System.out.println("[4] Listar as tarefas por...");
        System.out.println("[5] Atualizar o Status da tarefa");
        System.out.println("[6] Atualizar tarefa (descrição, data e categoria)");
        System.out.println("[7] Deletar tarefa");
        System.out.println("[8] Deletar todas as tarefas");
        System.out.println("[9] Acessar o menu de categorias");
        System.out.println("[0] Sair do programa");
    }

    public static void processingTaskMenuOption(int option) {
        switch (option) {
            case 1 -> TaskController.saveTask();
            case 2 -> TaskController.findAll();
            case 3 -> TaskController.findByDataAsc();
            case 4 -> TaskController.findByCriteria();
            case 5 -> TaskController.updateStatus();
            case 6 -> TaskController.update();
            case 7 -> TaskController.delete();
            case 8 -> TaskController.deleteAll();
            default -> throw new IllegalArgumentException("Valor inválido");
        }
    }

    public static void showCriteriasMenu() {
        System.out.println("Selecione o critério:");
        for (TaskColumn columns : TaskColumn.values()) {
            System.out.printf("[%d] %s%n", columns.getColumnClassification(), columns.getPortugueseColumnName());
        }
    }

    public static TaskColumn processingCriteriasMenuOption(int option) {
        return TaskColumn.selectByColumnClassification(option);
    }

    public static void taskStatusMenu() {
        for (TaskStatus status : TaskStatus.values()) {
            System.out.printf("[%d] %s%n", status.getStatusClassification(), status.getPortugueseStatusName());
        }
    }

    public static TaskStatus processingTaskStatusMenu(int option) {
        return TaskStatus.selectByStatusId(option);
    }


    private static void showCategoryMenu() {
        System.out.println("**********************");
        System.out.println("* MENU DE CATEGORIAS *");
        System.out.println("**********************\n");

        System.out.println("Selecione a opção desejada:");
        System.out.println("[1] Criar categoria");
        System.out.println("[2] Listar todas as categorias");
        System.out.println("[3] Listar categoria por Id");
        System.out.println("[0] Voltar para o menu anterior");

    }

    private static void processingCategoryMenu(int option) {
        switch (option) {
            case 1 -> CategoryController.save();
            case 2 -> CategoryController.findAll();
            case 3 -> CategoryController.findById();
            default -> throw new IllegalArgumentException("Valor inválido");
        }
    }

    public static void runCategoryMenu(Scanner scanner) {
        int option;
        while (true) {
            showCategoryMenu();
            option = Validator.validateNumber(scanner.nextLine());
            if (option == 0) return;
            processingCategoryMenu(option);
        }
    }
}
