package mortar.euroshopper.eCommerceApplication;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {

    Item findByProductNumber(String productNumber);
    
    List<Item> findByItemTypeIgnoreCaseContainingAndCategoryAndIsHiddenAndNameIgnoreCaseContainingAndProductNumberContainingAndManufacturerIgnoreCaseContainingAndWholesalerIgnoreCaseContainingOrderByName(String itemType, Category category, Boolean isHidden, String name, String productNumber, String manufacturer, String wholesaler);
    
    List<Item> findByItemTypeIgnoreCaseContainingAndIsHiddenAndNameIgnoreCaseContainingAndProductNumberContainingAndManufacturerIgnoreCaseContainingAndWholesalerIgnoreCaseContainingOrderByName(String itemType, Boolean isHidden, String name, String productNumber, String manufacturer, String wholesaler);

    List<Item> findByItemTypeIgnoreCaseContainingAndCategoryAndNameIgnoreCaseContainingAndProductNumberContainingAndManufacturerIgnoreCaseContainingAndWholesalerIgnoreCaseContainingOrderByName(String itemType, Category category, String name, String productNumber, String manufacturer, String wholesaler);
    
    List<Item> findByItemTypeIgnoreCaseContainingAndNameIgnoreCaseContainingAndProductNumberContainingAndManufacturerIgnoreCaseContainingAndWholesalerIgnoreCaseContainingOrderByName(String itemType, String name, String productNumber, String manufacturer, String wholesaler);
    
    List<Item> findByCategoryOrderByName(Category category);

}
