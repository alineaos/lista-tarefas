package todolist.service;

import todolist.exceptions.Validator;
import todolist.model.Category;
import todolist.repository.CategoryRepository;

public class CategoryService {

    public static void save(Category category){
        Validator.validateCategoryName(category.getName());
        CategoryRepository.save(category);
    }
}
