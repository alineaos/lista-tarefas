package todolist.service;

import todolist.exceptions.Validator;
import todolist.model.Task;
import todolist.repository.TaskRepository;

import java.time.LocalDate;
import java.util.Scanner;

public class TaskService {
    private static final Scanner SCANNER = new Scanner(System.in);
    public static void saveTask() {
        System.out.println("Descreva a tarefa");
        String description = SCANNER.nextLine();
        System.out.println("Selecione o status da tarefa");
        String status = taskStatus();
        System.out.println("Digite a data limite no formato DD/MM/AAAA");
        LocalDate date = Validator.validateDate(SCANNER.nextLine());
        System.out.println("Digite a categoria da tarefa");
        String category = SCANNER.nextLine();

        Task task = Task.builder()
                .description(description)
                .status(status)
                .date(date)
                .category(category)
                .build();
        TaskRepository.saveTask(task);
    }

    private static String taskStatus() {
        int option;
        do {
            System.out.println("[1] Não iniciada");
            System.out.println("[2] Em progresso");
            System.out.println("[3] Concluída");
        option = Validator.validateNumber(SCANNER.nextLine());
        } while (option < 1 || option > 3);

        return switch (option) {
            case 1 -> "Não iniciada";
            case 2 -> "Em progresso";
            case 3 -> "Concluída";
            default -> throw new IllegalArgumentException("Valor inválido");
        };
    }
}
