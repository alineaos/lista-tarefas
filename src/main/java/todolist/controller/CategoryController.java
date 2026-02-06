package todolist.controller;

import todolist.exceptions.Validator;
import todolist.model.Category;
import todolist.service.CategoryService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class CategoryController {
    private static final Scanner SCANNER = new Scanner(System.in);

    public static void save() {
        System.out.println("Digite o nome da categoria");
        String name = SCANNER.nextLine();

        Category categoryToSave = Category.builder()
                .name(name)
                .build();

        CategoryService.save(categoryToSave);
        System.out.println("Categoria salva com sucesso.");
    }

    public static void findAll() {
        List<Category> categories = CategoryService.findAll();
        printCategoryTable(categories);
    }

    public static void findById() {
        Optional<Category> categoryOptional = getCategoryById();

        if (categoryOptional.isEmpty()) return;

        printCategoryTable(List.of(categoryOptional.get()));
    }

    public static void update() {
        Optional<Category> categoryOptional = getCategoryById();
        if (categoryOptional.isEmpty()) return;
        Category categoryFromDb = categoryOptional.get();

        System.out.println("Digite o novo nome: ");
        String newName = SCANNER.nextLine();

        Category categoryToUpdate = Category.builder()
                .id(categoryFromDb.getId())
                .name(newName)
                .build();

        CategoryService.update(categoryToUpdate);
        System.out.printf("A categoria '%s' foi atualizada para '%s'.%n", categoryFromDb.getName(), newName);
    }

    public static void delete(){
        Optional<Category> categoryOptional = getCategoryById();
        if (categoryOptional.isEmpty()) return;
        Category categoryFromDb = categoryOptional.get();

        System.out.println("Categoria selecionada: ");
        printCategoryTable(List.of(categoryFromDb));

        if (confirmeAction("Essa ação é irreversível, você tem certeza?")) {
            CategoryService.delete(categoryFromDb.getId());
            System.out.printf("Categoria '%s' deletada com sucesso%n", categoryFromDb.getName());
        }
    }

    private static void printCategoryTable(List<Category> categories) {
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

    private static Optional<Category> getCategoryById() {
        System.out.println("Digite o id da categoria ou 0 para cancelar");
        int id = Validator.validateNumber(SCANNER.nextLine());
        if (id == 0) return Optional.empty();

        Optional<Category> categoryOptional = CategoryService.findById(id);

        if (categoryOptional.isEmpty()) System.out.println("Categoria não encontrada.");

        return categoryOptional;
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
