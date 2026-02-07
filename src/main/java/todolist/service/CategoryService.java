package todolist.service;

import todolist.exceptions.BusinessException;
import todolist.exceptions.Validator;
import todolist.model.Category;
import todolist.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;

public class CategoryService {

    public static void save(Category category) {
        Validator.categoryName(category.getName());
        existsByName(category);
        CategoryRepository.save(category);
    }

    public static List<Category> findAll() {
        return CategoryRepository.findAll();
    }

    public static Optional<Category> findById(int id) {
        return CategoryRepository.findById(id);
    }

    public static void update(Category category) {
        Validator.categoryName(category.getName());
        existsByName(category);
        CategoryRepository.update(category);
    }

    public static void delete(int id) {
        CategoryRepository.delete(id);
    }

    private static void existsByName(Category category) {
        boolean nameAlreadyExists = CategoryRepository.existsByName(category);
        if (nameAlreadyExists) {
            throw new BusinessException("O nome '" + category.getName() + "' já existe no banco de dados.");
        }
    }

    public static Category getCategoryById(int id){
        return findById(id)
                .orElseThrow(() -> new BusinessException("A categorid com o ID "+id +" não foi encontrada"));
    }
}
