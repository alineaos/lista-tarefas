package todolist.service;

import todolist.exceptions.Validator;
import todolist.model.Task;
import todolist.model.enums.TaskStatus;
import todolist.repository.TaskRepository;
import todolist.ui.Menu;
import todolist.util.ColumnsEnum;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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
                option = Validator.validateNumber(SCANNER.nextLine());
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

    public static void findByDataAsc() {
        List<Task> tasks = TaskRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Task::getDate))
                .toList();

        printTaskTable(tasks);
    }

    public static void updateStatus(){
        System.out.println("Digite o id da tarefa ou 0 para retornar ao menu anterior");
        int id = Validator.validateNumber(SCANNER.nextLine());
        if(id == 0) return;
        Optional<Task> taskOptional = TaskRepository.findByCriteria(ColumnsEnum.ID, String.valueOf(id)).stream()
                .filter( t -> t.getId().equals(id))
                .findFirst();

        if(taskOptional.isEmpty()){
            System.out.println("Tarefa não encontrada.");
            return;
        }

        Task taskFromDb = taskOptional.get();
        System.out.println("Tarefa: " + taskFromDb.getDescription());
        System.out.println("Status: " + taskFromDb.getStatus().getPortugueseStatusName());
        System.out.println("Digite o novo status");
        Menu.taskStatusMenu();
        int option = Validator.validateNumber(SCANNER.nextLine());
        TaskStatus newStatus = Menu.processingTaskStatusMenu(option);

        TaskRepository.updateStatus(taskFromDb, newStatus);
    }

    public static void update(){
        System.out.println("Digite o id da tarefa ou 0 para retornar ao menu anterior");
        int id = Validator.validateNumber(SCANNER.nextLine());
        if(id == 0) return;
        Optional<Task> taskOptional = TaskRepository.findByCriteria(ColumnsEnum.ID, String.valueOf(id)).stream()
                .filter( t -> t.getId().equals(id))
                .findFirst();

        if(taskOptional.isEmpty()){
            System.out.println("Tarefa não encontrada.");
            return;
        }

        Task taskFromDb = taskOptional.get();
        System.out.println("Tarefa: " + taskFromDb.getDescription());
        System.out.println("Digite a nova descrição. Em branco mantém a atual.");
        String updatedDescription = SCANNER.nextLine();
        updatedDescription = updatedDescription.isBlank() ? taskFromDb.getDescription() : updatedDescription;

        System.out.println("Data: " + Validator.validateDateToString(taskFromDb.getDate()));
        System.out.println("Digite a nova data. Em branco mantém a atual.");
        String stringToDate = SCANNER.nextLine();
        LocalDate updatedDate = stringToDate.isBlank() ? taskFromDb.getDate() : Validator.validateStringToDate(stringToDate);

        System.out.println("Categoria: " + taskFromDb.getCategory());
        System.out.println("Digite a nova categoria. Em branco mantém a atual.");
        String updatedCategory = SCANNER.nextLine();
        updatedCategory = updatedCategory.isBlank() ? taskFromDb.getCategory() : updatedCategory;

        Task updatedTask = Task.builder()
                .id(taskFromDb.getId())
                .description(updatedDescription)
                .date(updatedDate)
                .category(updatedCategory)
                .build();
        TaskRepository.update(updatedTask);
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
