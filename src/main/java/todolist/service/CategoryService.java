package todolist.service;

import todolist.exceptions.BusinessException;
import todolist.exceptions.Validator;
import todolist.model.Category;
import todolist.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;

public class CategoryService {
private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public void save(Category category) {
        Validator.categoryName(category.getName());
        existsByName(category);
        categoryRepository.save(category);
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Optional<Category> findById(int id) {
        return categoryRepository.findById(id);
    }

    public void update(Category category) {
        Validator.categoryName(category.getName());
        existsByName(category);
        categoryRepository.update(category);
    }

    public void delete(int id) {
        categoryRepository.delete(id);
    }

    private void existsByName(Category category) {
        boolean nameAlreadyExists = categoryRepository.existsByName(category);
        if (nameAlreadyExists) {
            throw new BusinessException("O nome '" + category.getName() + "' já existe no banco de dados.");
        }
    }

    public Category getCategoryById(int id){
        return findById(id)
                .orElseThrow(() -> new BusinessException("A categoria com o ID "+id +" não foi encontrada"));
    }
}
