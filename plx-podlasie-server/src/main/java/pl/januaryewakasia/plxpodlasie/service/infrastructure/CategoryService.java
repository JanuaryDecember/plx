package pl.januaryewakasia.plxpodlasie.service.infrastructure;

import org.springframework.stereotype.Service;
import pl.januaryewakasia.plxpodlasie.exception.NotFoundException;
import pl.januaryewakasia.plxpodlasie.model.Category;
import pl.januaryewakasia.plxpodlasie.repository.CategoryRepository;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category findById(Long id) throws NotFoundException {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category with id: " + id + " not found"));
    }

    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }

    public Category updateCategory(Category category) {
        if (!categoryRepository.existsById(category.getId()))
            throw new NotFoundException("Category with id: " + category.getId() + " not found");
        return categoryRepository.save(category);
    }
}
