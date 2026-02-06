package todolist.controller;

import todolist.exceptions.Validator;
import todolist.model.Task;
import todolist.model.enums.TaskStatus;
import todolist.service.TaskService;
import todolist.ui.Menu;
import todolist.util.TaskColumn;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Function;

public class TaskController {
    private static final Scanner SCANNER = new Scanner(System.in);

    public static void save() {
        System.out.println("Descreva a tarefa");
        String description = SCANNER.nextLine();
        System.out.println("Selecione o status da tarefa");
        int option;
        do {
            Menu.taskStatusMenu();
            option = Validator.parseInteger(SCANNER.nextLine());
        } while (option < 1 || option > 3);
        TaskStatus status = Menu.processingTaskStatusMenu(option);
        System.out.println("Digite a data limite no formato DD/MM/AAAA");
        LocalDate date = Validator.parseDate(SCANNER.nextLine());
        System.out.println("Digite a categoria da tarefa");
        String category = SCANNER.nextLine();

        Task taskToSave = Task.builder()
                .description(description)
                .status(status)
                .date(date)
                .category(category)
                .build();

        TaskService.save(taskToSave);
        System.out.println("Tarefa salva com sucesso.");
    }

    public static void findAll() {
        List<Task> tasks = TaskService.findAll();

        printTaskTable(tasks);
    }

    public static void findByCriteria() {
        Menu.showCriteriasMenu();
        int option = Validator.parseInteger(SCANNER.nextLine());

        TaskColumn selectedCriteria = Menu.processingCriteriasMenuOption(option);
        System.out.println("Digite o parâmetro de busca");

        Object param = switch (selectedCriteria.getEnglishColumnName()) {
            case "Status" -> {
                Menu.taskStatusMenu();
                option = Validator.parseInteger(SCANNER.nextLine());
                yield Menu.processingTaskStatusMenu(option);
            }
            case "Date" -> {
                System.out.println("A data precisa estar no formato DD/MM/YYYY");
                yield  Validator.parseDate(SCANNER.nextLine());
            }
            default -> SCANNER.nextLine();
        };

        List<Task> tasks = TaskService.findByCriteria(selectedCriteria, param);
        printTaskTable(tasks);
    }

    public static void findByDataAsc(){
        List<Task> tasks = TaskService.findByDataAsc();
        printTaskTable(tasks);
    }

    public static void updateStatus(){
        Optional<Task> taskOptional = getTaskById();
        if (taskOptional.isEmpty()) return;
        Task taskFromDb = taskOptional.get();

        System.out.println("Tarefa: " + taskFromDb.getDescription());
        System.out.println("Status: " + taskFromDb.getStatus().getPortugueseStatusName());
        System.out.println("Digite o novo status");

        Menu.taskStatusMenu();
        int option = Validator.parseInteger(SCANNER.nextLine());
        TaskStatus newStatus = Menu.processingTaskStatusMenu(option);

        TaskService.updateStatus(taskFromDb, newStatus);
        System.out.println("Status atualizado com sucesso.");
    }

    public static void update() {
        Optional<Task> taskOptional = getTaskById();
        if (taskOptional.isEmpty()) return;
        Task taskFromDb = taskOptional.get();

        String updatedDescription = fieldToUpdate(TaskColumn.DESCRIPTION.getPortugueseColumnName(), taskFromDb.getDescription());
        LocalDate updatedDate = fieldToUpdate(TaskColumn.DATE.getPortugueseColumnName(), taskFromDb.getDate(),
                Validator::parseDate,
                Validator::formatDate);
        String updatedCategory = fieldToUpdate(TaskColumn.CATEGORY.getPortugueseColumnName(), taskFromDb.getCategory());


        Task taskToUpdate = Task.builder()
                .id(taskFromDb.getId())
                .description(updatedDescription)
                .date(updatedDate)
                .category(updatedCategory)
                .build();

        TaskService.update(taskToUpdate);
        System.out.println("Tarefa atualizada com sucesso.");
    }

    public static void delete(){
        Optional<Task> taskOptional = getTaskById();
        if (taskOptional.isEmpty()) return;
        Task taskToDelete = taskOptional.get();

        System.out.println("Tarefa selecionada: ");
        printTaskTable(List.of(taskToDelete));
        if (confirmeAction("Essa ação é irreversível, você tem certeza?")) {
            TaskService.delete(taskToDelete.getId());
            System.out.println("A tarefa foi excluída com sucesso.");
        }
    }

    public static void deleteAll() {
        if (confirmeAction("Você tem certeza que deseja deletar TODAS as tarefas do banco de dados? Essa ação é irreversível.")) {
            TaskService.deleteAll();
            System.out.println("Todas as tarefas do banco de dados foram excluídas com sucesso. ");
        }
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
                Validator.formatDate(f.getDate()),
                f.getCategory()));
    }

    private static Optional<Task> getTaskById() {
        System.out.println("Digite o id da tarefa ou 0 para retornar ao menu anterior");
        int id = Validator.parseInteger(SCANNER.nextLine());
        if (id == 0) return Optional.empty();
        Optional<Task> taskOptional = TaskService.getTaskById(id);

        if (taskOptional.isEmpty()) {
            System.out.println("Tarefa não encontrada.");
        }

        return taskOptional;
    }

    private static String fieldToUpdate(String field, String currentValue) {
        System.out.printf("%s: %s%n", field, currentValue);
        System.out.printf("Digite a nova %s. Em branco mantém a atual.%n", field);
        String input = SCANNER.nextLine();
        return input.isBlank() ? currentValue : input;

    }

    private static <T> T fieldToUpdate(String field, T currentValue, Function<String, T> converter, Function<T, String> formatter) {
        System.out.printf("%s: %s%n", field, formatter.apply(currentValue));
        System.out.printf("Digite a nova %s. Em branco mantém a atual.%n", field);
        String input = SCANNER.nextLine();

        if (input.isBlank()) {
            return currentValue;
        }
        return converter.apply(input);

    }

    private static boolean confirmeAction (String message) {
        while (true) {
            System.out.printf("%s (S/N)%n", message);
            String input = SCANNER.nextLine().toUpperCase();
            if (input.equals("S")) return true;
            if (input.equals("N")) {
                System.out.println("Ação cancelada.");
                return false;
            }
        }
    }
}
