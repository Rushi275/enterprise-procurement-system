package EnterpriseProcurementSystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import EnterpriseProcurementSystem.entity.Category;
import EnterpriseProcurementSystem.repository.CategoryRepository;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    
    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

  
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}