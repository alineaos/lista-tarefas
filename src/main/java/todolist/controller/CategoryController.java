package todolist.controller;

import todolist.exceptions.Validator;
import todolist.model.Category;
import todolist.service.CategoryService;

import java.util.List;
import java.util.Scanner;

public class CategoryController {
    private static final Scanner SCANNER = new Scanner(System.in);

    public static void save(){
        System.out.println("Digite o nome da categoria");
        String name = SCANNER.nextLine();

        Category categoryToSave = Category.builder()
                .name(name)
                .build();

        CategoryService.save(categoryToSave);
        System.out.println("Categoria salva com sucesso.");
    }

    public static void findAll(){
    List<Category> categories = CategoryService.findAll();
        printCategoryTable(categories);
    }

    public static void findById(){
        System.out.println("Digite o id para ser buscado");
        int id = Validator.validateNumber(SCANNER.nextLine());

        CategoryService.findById(id)
                .ifPresent(category -> printCategoryTable(List.of(category)));
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
}
