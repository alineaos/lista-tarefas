package todolist.controller;

import todolist.model.Category;
import todolist.service.CategoryService;

import java.util.List;
import java.util.Optional;

public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public String save(String name) {
        Category categoryToSave = Category.builder()
                .name(name)
                .build();

        categoryService.save(categoryToSave);
        return "Categoria salva com sucesso.";
    }

    public List<Category> findAll() {
        return categoryService.findAll();
    }

    public Optional<Category> findById(int id) {
        return categoryService.findById(id);
    }

    public String update(Category category, String newName) {
        Category categoryToUpdate = Category.builder()
                .id(category.getId())
                .name(newName)
                .build();

        categoryService.update(categoryToUpdate);
        return "A categoria '%s' foi atualizada para '%s'.%n".formatted(category.getName(), newName);
    }

    public String delete(Category category) {
        categoryService.delete(category.getId());
        return "Categoria '%s' deletada com sucesso%n".formatted(category.getName());
    }
}
