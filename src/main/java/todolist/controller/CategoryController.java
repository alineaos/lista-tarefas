package todolist.controller;

import todolist.model.Category;
import todolist.service.CategoryService;

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
}
