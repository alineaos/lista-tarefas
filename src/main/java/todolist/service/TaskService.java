package todolist.service;

import todolist.exceptions.Validator;
import todolist.model.Task;
import todolist.model.enums.TaskStatus;
import todolist.repository.TaskRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class TaskService {
    private static final Scanner SCANNER = new Scanner(System.in);

    public static void saveTask() {
        System.out.println("Descreva a tarefa");
        String description = SCANNER.nextLine();
        System.out.println("Selecione o status da tarefa");
        TaskStatus status = taskStatus();
        System.out.println("Digite a data limite no formato DD/MM/AAAA");
        LocalDate date = Validator.validateStringToDate(SCANNER.nextLine());
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

    private static TaskStatus taskStatus() {
        int option;
        do {
            System.out.println("[1] Não iniciada");
            System.out.println("[2] Em progresso");
            System.out.println("[3] Concluída");
            option = Validator.validateNumber(SCANNER.nextLine());
        } while (option < 1 || option > 3);

        return TaskStatus.selectByStatusId(option);
    }

    public static void findAll() {
        List<Task> tasks = TaskRepository.findAll();
        int maxDescriptionLength = descriptionLargestLength(tasks);
        displayHeader(maxDescriptionLength);
        tasks.forEach(f -> System.out.printf("[%-2d] %-"+maxDescriptionLength+"s | %-12s | %-10s | %s%n", f.getId(), f.getDescription(),
                        f.getStatus().getPORTUGUESE_STATUS_NAME(), Validator.validateDateToString(f.getDate()), f.getCategory()));
    }

    private static void displayHeader(int maxDescriptionLength) {
        //String headerPattern = "[ID] Tarefa - Status - Data - Categoria"
        String headerPattern = "[%-2s] %-"+maxDescriptionLength+"s | %-12s | %-10s | %s%n";
        System.out.printf(headerPattern, "ID","Tarefa", "Status", "Data", "Categoria");
    }

    private static int descriptionLargestLength(List<Task> tasks) {
        int max = tasks.stream()
                .map(Task::getDescription)
                .mapToInt(String::length)
                .max()
                .orElse(10);

        return Math.max(max, 6);
    }
}
