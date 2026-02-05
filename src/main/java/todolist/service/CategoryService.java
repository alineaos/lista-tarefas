package todolist.service;

import todolist.exceptions.Validator;
import todolist.model.Category;
import todolist.repository.CategoryRepository;

import java.util.List;

public class CategoryService {

    public static void save(Category category){
        Validator.validateCategoryName(category.getName());
        CategoryRepository.save(category);
    }
    public static List<Category> findAll() {
        return CategoryRepository.findAll();
    }

}
