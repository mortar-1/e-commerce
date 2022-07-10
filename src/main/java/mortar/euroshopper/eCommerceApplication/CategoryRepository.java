package mortar.euroshopper.eCommerceApplication;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findByName(String name);

    @Query("select c from Category c where c.parentCategory is null")
    List<Category> findMainCategories();

}
