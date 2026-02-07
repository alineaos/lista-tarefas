package todolist.ui;

import todolist.controller.CategoryController;
import todolist.exceptions.Validator;
import todolist.model.Category;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class CategoryMenu {
    private final CategoryController categoryController;
    private final Scanner scanner;

    public CategoryMenu(CategoryController categoryController, Scanner scanner) {
        this.categoryController = categoryController;
        this.scanner = scanner;
    }

    public void runCategoryMenu() {
        while (true) {
            int option;
            do {
                showCategoryMenu();
                option = Validator.parseInteger(scanner.nextLine());
            } while (option < 0 || option > 5);
            if (option == 0) return;
            processingCategoryMenu(option);
        }
    }

    private void showCategoryMenu() {
        System.out.println("**********************");
        System.out.println("* MENU DE CATEGORIAS *");
        System.out.println("**********************\n");

        System.out.println("Selecione a opção desejada:");
        System.out.println("[1] Criar categoria");
        System.out.println("[2] Listar todas as categorias");
        System.out.println("[3] Listar categoria por Id");
        System.out.println("[4] Atualizar categoria");
        System.out.println("[5] Deletar categoria");
        System.out.println("[0] Voltar para o menu anterior");

    }

    private void processingCategoryMenu(int option) {
        switch (option) {
            case 1 -> handleSave();
            case 2 -> handleFindAll();
            case 3 -> handleFindById();
            case 4 -> handleUpdate();
            case 5 -> handleDelete();
            default -> throw new IllegalArgumentException("Valor inválido");
        }
    }

    //region CATEGORY ACTIONS (HANDLES)
// ========================================================================================================
// METHODS TO HANDLE USER ACTIONS
// ========================================================================================================
    private void handleSave() {
        System.out.println("Digite o nome da categoria");
        String name = scanner.nextLine();

        categoryController.save(name);
    }

    private void handleFindAll(){
        List<Category> categories = categoryController.findAll();
        printCategoryTable(categories);
    }

    private void handleFindById(){
        Optional<Category> categoryOptional = getCategoryById();

        if(categoryOptional.isEmpty()) return;

        printCategoryTable(List.of(categoryOptional.get()));
    }

    private void handleUpdate(){
        Optional<Category> categoryOptional = getCategoryById();
        if (categoryOptional.isEmpty()) return;
        Category categoryFromDb = categoryOptional.get();

        System.out.println("Digite o novo nome: ");
        String newName = scanner.nextLine();

        String message = categoryController.update(categoryFromDb, newName);
        System.out.println(message);
    }

    private void handleDelete(){
        Optional<Category> categoryOptional = getCategoryById();
        if (categoryOptional.isEmpty()) return;
        Category categoryFromDb = categoryOptional.get();

        System.out.println("Categoria selecionada: ");
        printCategoryTable(List.of(categoryFromDb));

        System.out.println("As tarefas cadastradas na categoria selecionada também serão deletadas.");
        System.out.println("Essa ação é irreversível, você tem certeza? (S/N)");
        String choice = scanner.nextLine();
        if (!choice.equalsIgnoreCase("S")){
            System.out.println("Ação cancelada.");
            return;
        }
        String message = categoryController.delete(categoryFromDb);
        System.out.println(message);
    }
//endregion

    //region I/O METHODS
// ========================================================================================================
// METHODS TO HELP WITH I/O
// ========================================================================================================
    private Optional<Category> getCategoryById() {
        System.out.println("Digite o id da categoria ou 0 para cancelar");
        int id = Validator.parseInteger(scanner.nextLine());
        if (id == 0) return Optional.empty();

        Optional<Category> categoryOptional = categoryController.findById(id);

        if (categoryOptional.isEmpty()) System.out.println("Categoria não encontrada.");

        return categoryOptional;
    }
//endregion

    //region AUX PRINT INTERFACE
// ========================================================================================================
// METHODS TO HELP WITH PRINTS (Table formatting)
// ========================================================================================================
    private void printCategoryTable(List<Category> categories) {
        if (categories.isEmpty()) {
            System.out.println("Lista vazia. Nenhuma categoria para exibir.");
            return;
        }

        String tablePattern = "[%-2s] %s%n";
        System.out.printf(tablePattern, "ID", "Nome");
        categories.forEach(c -> System.out.printf(tablePattern,
                c.getId(),
                c.getName()));
    }
//endregion
}
