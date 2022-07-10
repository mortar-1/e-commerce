package mortar.euroshopper.eCommerceApplication;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Category newMainCategory(String name) {

        Category ca = new Category(name);
        
        ca.setCategoryLevel(1);

        categoryRepository.save(ca);

        return ca;
    }

    public Category newSubCategory(String name, Category parent) {

        Category ca = new Category(name, parent);
        
        ca.setCategoryLevel(parent.getCategoryLevel() + 1);

        categoryRepository.save(ca);
 
        return ca;
    }

    public void deleteCategory(Long id) {
        
        Category ca = categoryRepository.getById(id);
        
        for (Category current : ca.getSubCategories()) 
            
            deleteCategory(current.getId());

        categoryRepository.deleteById(id);
    }
}
