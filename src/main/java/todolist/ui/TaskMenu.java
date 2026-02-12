package todolist.ui;

import todolist.controller.TaskController;
import todolist.exceptions.Validator;
import todolist.model.Category;
import todolist.model.Task;
import todolist.model.enums.TaskStatus;
import todolist.util.TaskColumn;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Function;

public class TaskMenu {
    private final TaskController taskController;
    private final Scanner scanner;

    public TaskMenu(TaskController taskController, Scanner scanner) {
        this.taskController = taskController;
        this.scanner = scanner;
    }

    public void runTaskMenu() {
        while (true) {
            int option;
            do {
                showTaskMenu();
                option = Validator.parseInteger(scanner.nextLine());
            } while (option < 0 || option > 8);
            if (option == 0) return;
            processingTaskMenuOption(option);
        }
    }

    private void showTaskMenu() {
        System.out.println("\n*******************");
        System.out.println("* MENU DE TAREFAS *");
        System.out.println("*******************\n");

        System.out.println("Selecione a opção desejada:");
        System.out.println("[1] Criar tarefa");
        System.out.println("[2] Listar todas as tarefas");
        System.out.println("[3] Listar as tarefas pela data mais próxima");
        System.out.println("[4] Listar as tarefas por...");
        System.out.println("[5] Atualizar o Status da tarefa");
        System.out.println("[6] Atualizar tarefa (descrição, data e categoria)");
        System.out.println("[7] Deletar tarefa");
        System.out.println("[8] Deletar todas as tarefas");
        System.out.println("[0] Voltar para o menu anterior");
    }

    private void processingTaskMenuOption(int option) {
        switch (option) {
            case 1 -> handleSave();
            case 2 -> handleFindAll();
            case 3 -> handleFindByDataAsc();
            case 4 -> handleFindByCriteria();
            case 5 -> handleUpdateStatus();
            case 6 -> handleUpdate();
            case 7 -> handleDelete();
            case 8 -> handleDeleteAll();
            default -> throw new IllegalArgumentException("Valor inválido");
        }
    }


//region TASK ACTIONS (HANDLES)
// ========================================================================================================
// METHODS TO HANDLE USER ACTIONS
// ========================================================================================================
    private void handleSave() {
        System.out.println("Descreva a tarefa");
        String description = scanner.nextLine();

        System.out.println("Selecione o status da tarefa");

        TaskStatus status = runTaskStatusMenu();
        System.out.println("Digite a data limite no formato DD/MM/AAAA");

        LocalDate date = Validator.parseDate(scanner.nextLine());
        System.out.println("Escolha o ID da categoria da tarefa");

        Category category = getCategoryById();
        String message = taskController.save(description, status, date, category);
        System.out.println(message);
    }

    private void handleFindAll() {
        List<Task> tasks = taskController.findAll();
        printTaskTable(tasks);
    }

    private void handleFindByDataAsc() {
        List<Task> tasks = taskController.findByDataAsc();
        printTaskTable(tasks);
    }

    private void handleFindByCriteria() {
        TaskColumn selectedCriteria = runCriteriasMenu();
        System.out.println("Digite o parâmetro de busca");

        Object param = switch (selectedCriteria.getEnglishColumnName()) {
            case "Status" -> runTaskStatusMenu();
            case "Date" -> {
                System.out.println("A data precisa estar no formato DD/MM/YYYY");
                yield Validator.parseDate(scanner.nextLine());
            }
            case "Category_Id" -> getCategoryById().getId();

            default -> scanner.nextLine();
        };

        List<Task> tasks = taskController.findByCriteria(selectedCriteria, param);
        printTaskTable(tasks);
    }

    private void handleUpdateStatus() {
        Task taskFromDb = optionalToTask();
        if (taskFromDb == null) return;

        System.out.println("Tarefa: " + taskFromDb.getDescription());
        System.out.println("Status: " + taskFromDb.getStatus().getPortugueseStatusName());
        System.out.println("Digite o novo status");
        TaskStatus newStatus = runTaskStatusMenu();

        String message = taskController.updateStatus(taskFromDb, newStatus);
        System.out.println(message);
    }

    private void handleUpdate(){
        Task taskFromDb = optionalToTask();
        if (taskFromDb == null) return;

        String updatedDescription = fieldToUpdate(TaskColumn.DESCRIPTION.getPortugueseColumnName(), taskFromDb.getDescription());
        LocalDate updatedDate = fieldToUpdate(TaskColumn.DATE.getPortugueseColumnName(), taskFromDb.getDate(),
                Validator::formatDate,
                Validator::parseDate);
        String categoryFromDb = fieldToUpdate(TaskColumn.CATEGORY_ID.getPortugueseColumnName(), String.valueOf(taskFromDb.getCategory().getId()));

        int updatedCategoryId = Integer.parseInt(categoryFromDb);
        String message = taskController.update(taskFromDb.getId(), updatedDescription, updatedDate, updatedCategoryId);
        System.out.println(message);
    }

    private void handleDelete(){
        Task taskFromDb = optionalToTask();
        if (taskFromDb == null) return;

        System.out.println("Tarefa selecionada: ");
        printTaskTable(List.of(taskFromDb));

        if (confirmeAction("Essa ação é irreversível, você tem certeza?")) {
            String message = taskController.delete(taskFromDb.getId());
            System.out.println(message);
        }
    }

    private void handleDeleteAll(){
        if (confirmeAction("Você tem certeza que deseja deletar TODAS as tarefas do banco de dados? Essa ação é irreversível.")) {
            String message = taskController.deleteAll();
            System.out.println(message);
        }
    }
//endregion

//region SUBMENUS (AUX MENUS)
// ========================================================================================================
// METHODS TO HELP WITH I/O
// ========================================================================================================

    private void showCriteriasMenu() {
        System.out.println("Selecione o critério:");
        for (TaskColumn columns : TaskColumn.values()) {
            System.out.printf("[%d] %s%n", columns.getColumnClassification(), columns.getPortugueseColumnName());
        }
    }

    private TaskColumn processingCriteriasMenuOption(int option) {
        return TaskColumn.selectByColumnClassification(option);
    }

    private TaskColumn runCriteriasMenu() {
        showCriteriasMenu();
        int option = Validator.parseInteger(scanner.nextLine());
        return processingCriteriasMenuOption(option);
    }

    private void showTaskStatusMenu() {
        for (TaskStatus status : TaskStatus.values()) {
            System.out.printf("[%d] %s%n", status.getStatusClassification(), status.getPortugueseStatusName());
        }
    }

    private TaskStatus processingTaskStatusMenu(int option) {
        return TaskStatus.selectByStatusId(option);
    }

    private TaskStatus runTaskStatusMenu() {
        showTaskStatusMenu();
        int option = Validator.parseInteger(scanner.nextLine());
        return processingTaskStatusMenu(option);
    }

    private Category getCategoryById() {
        printCategoriesList();
        int categoryId = Validator.parseInteger(scanner.nextLine());
        return taskController.getCategoryById(categoryId);
    }

    private Optional<Task> getTaskById() {
        System.out.println("Digite o id da tarefa ou 0 para retornar ao menu anterior");
        int id = Validator.parseInteger(scanner.nextLine());
        if (id == 0) return Optional.empty();
        Optional<Task> taskOptional = taskController.getTaskById(id);

        if (taskOptional.isEmpty()) {
            System.out.println("Tarefa não encontrada.");
        }

        return taskOptional;
    }

    private Task optionalToTask() {
        Optional<Task> taskOptional = getTaskById();
        return taskOptional.orElse(null);
    }

    private String fieldToUpdate(String field, String currentValue) {
        System.out.printf("%s: %s%n", field, currentValue);
        System.out.printf("Digite o(a) novo(a) %s. Em branco para manter o(a) atual.%n", field);
        if (field.equals("Id Categoria")) printCategoriesList();
        String input = scanner.nextLine();
        return input.isBlank() ? currentValue : input;

    }

    private <T> T fieldToUpdate(String field, T currentValue, Function<T, String> formatter, Function<String, T> converter) {
        System.out.printf("%s: %s%n", field, formatter.apply(currentValue));
        System.out.printf("Digite o(a) novo(a) %s. Em branco para manter o(a) atual.%n", field);
        String input = scanner.nextLine();

        if (input.isBlank()) {
            return currentValue;
        }
        return converter.apply(input);

    }

    private boolean confirmeAction(String message) {
        while (true) {
            System.out.printf("%s (S/N)%n", message);
            String input = scanner.nextLine().toUpperCase();
            if (input.equals("S")) return true;
            if (input.equals("N")) {
                System.out.println("Ação cancelada.");
                return false;
            }
        }
    }
//endregion

//region AUX PRINT INTERFACE
// ========================================================================================================
// METHODS TO HELP WITH PRINTS (Table formatting)
// ========================================================================================================
    private void printCategoriesList() {
        System.out.println("[ID] Categoria");
        taskController.getCategoriesList()
                .forEach(c -> System.out.printf("[%-2s] %s%n", c.getId(), c.getName()));
    }

    private int descriptionLargestLength(List<Task> tasks) {
        int max = tasks.stream()
                .map(Task::getDescription)
                .mapToInt(String::length)
                .max()
                .orElse(10);

        return Math.max(max, 6);
    }

    private void printTaskTable(List<Task> tasks) {
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
                f.getCategory().getName()));
    }
    //endregion
}
