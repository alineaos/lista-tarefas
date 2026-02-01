package todolist.service;

import todolist.exceptions.Validator;
import todolist.model.Task;
import todolist.model.enums.TaskStatus;
import todolist.repository.TaskRepository;
import todolist.ui.Menu;
import todolist.util.ColumnsEnum;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class TaskService {
    private static final Scanner SCANNER = new Scanner(System.in);

    public static void saveTask() {
        System.out.println("Descreva a tarefa");
        String description = SCANNER.nextLine();
        System.out.println("Selecione o status da tarefa");
        int option;
        do {
            Menu.taskStatusMenu();
            option = Validator.validateNumber(SCANNER.nextLine());
        } while (option < 1 || option > 3);
        TaskStatus status = Menu.processingTaskStatusMenu(option);
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

    public static void findAll() {
        printTaskTable(TaskRepository.findAll());
    }

    public static void findByCriteria() {
        Menu.showCriteriasMenu();
        int option = Validator.validateNumber(SCANNER.nextLine());
        String param;
        ColumnsEnum selectedCriteria = Menu.processingCriteriasMenuOption(option);
        System.out.println("Digite o parâmetro de busca");
        switch (selectedCriteria.getEnglishColumnName()) {
            case "Status":
                Menu.taskStatusMenu();
                TaskStatus status = Menu.processingTaskStatusMenu(option);
                param = status.getPortugueseStatusName();
                break;
            case "Date":
                System.out.println("A data precisa estar no formato DD/MM/YYYY");
                param = String.valueOf(Validator.validateStringToDate(SCANNER.nextLine()));
                break;
            default:
                param = SCANNER.nextLine();
        }

        printTaskTable(TaskRepository.findByCriteria(selectedCriteria, param));
    }

    private static int descriptionLargestLength(List<Task> tasks) {
        int max = tasks.stream()
                .map(Task::getDescription)
                .mapToInt(String::length)
                .max()
                .orElse(10);

        return Math.max(max, 6);
    }

    private static void printTaskTable(List<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println("Lista vazia. Nenhuma tarefa para exibir.");
            return;
        }

        int maxDescriptionLength = descriptionLargestLength(tasks);
        String tablePattern = "[%-2s] %-" + maxDescriptionLength + "s | %-12s | %-10s | %s%n";
        System.out.printf(tablePattern, "ID", "Tarefa", "Status", "Data", "Categoria");
        tasks.forEach(f -> System.out.printf(tablePattern,
                f.getId(),
                f.getDescription(),
                f.getStatus().getPortugueseStatusName(),
                Validator.validateDateToString(f.getDate()),
                f.getCategory()));
    }
}
