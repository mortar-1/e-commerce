package mortar.euroshopper.eCommerceApplication;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;
import org.springframework.core.env.Environment;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private Environment environment;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ShoppingCart shoppingCart;

    @Autowired
    private ImageService imageService;

    public void addAttributesToModelForPageItems(Model model, String type, String category, String showHidden, String name, String productNumber, String manufacturer, String wholesaler) {

        List<Item> items = new ArrayList<>();

        System.out.println("!!!!: type = " + type + "; category = " + category + "; showHidden = " + showHidden + "; name = " + name + "; productNumber = " + productNumber + "; manufacturer = " + manufacturer + "; wholesaler = " + wholesaler);

        if (type != null) {

            if (category.equals("") && showHidden.equals("all")) {

                items = itemRepository.findByItemTypeIgnoreCaseContainingAndNameIgnoreCaseContainingAndProductNumberContainingAndManufacturerIgnoreCaseContainingAndWholesalerIgnoreCaseContainingOrderByName(type, name, productNumber, manufacturer, wholesaler);
            }

            if (!category.equals("") && showHidden.equals("all")) {
                items = itemRepository.findByItemTypeIgnoreCaseContainingAndCategoryAndNameIgnoreCaseContainingAndProductNumberContainingAndManufacturerIgnoreCaseContainingAndWholesalerIgnoreCaseContainingOrderByName(type, categoryRepository.findByName(category), name, productNumber, manufacturer, wholesaler);
            }

            if (category.equals("") && !showHidden.equals("all")) {
                items = itemRepository.findByItemTypeIgnoreCaseContainingAndIsHiddenAndNameIgnoreCaseContainingAndProductNumberContainingAndManufacturerIgnoreCaseContainingAndWholesalerIgnoreCaseContainingOrderByName(type, Boolean.valueOf(showHidden), name, productNumber, manufacturer, wholesaler);
            }

            if (!category.equals("") && !showHidden.equals("all")) {
                items = itemRepository.findByItemTypeIgnoreCaseContainingAndCategoryAndIsHiddenAndNameIgnoreCaseContainingAndProductNumberContainingAndManufacturerIgnoreCaseContainingAndWholesalerIgnoreCaseContainingOrderByName(type, categoryRepository.findByName(category), Boolean.valueOf(showHidden), name, productNumber, manufacturer, wholesaler);
            }
        }

        model.addAttribute("items", items);

        model.addAttribute("mainCategories", categoryRepository.findMainCategories());

        model.addAttribute("type", type);

        model.addAttribute("category", category);

        model.addAttribute("showHidden", showHidden);

        model.addAttribute("name", name);

        model.addAttribute("productNumber", productNumber);

        model.addAttribute("manufacturer", manufacturer);

        model.addAttribute("wholesaler", wholesaler);

    }

    public void addAttributesToModelForPageItem(Model model, Long id) {

        Item item = itemRepository.getById(id);

        model.addAttribute("item", item);

        model.addAttribute("itemCount", shoppingCart.getItems().getOrDefault(item, 0L));
    }

    public void addAttributesToModelForPageEditItem(Model model, Long id) {

        Item item = itemRepository.getById(id);

        model.addAttribute("item", item);
    }

    public void addAttributesToModelForPageShop(Model model) {

        model.addAttribute("items", itemRepository.findAll());
    }

    public void newItem(NewItem ni) throws IOException {

        Item item = new Item();

        item.setItemType(ni.getItemType());
        item.setCategory(ni.getCategory());
        item.setProductNumber(ni.getProductNumber());
        item.setName(ni.getName());
        item.setAmount(ni.getAmount());
        item.setUnit(ni.getUnit());
        item.setManufacturer(ni.getManufacturer());
        item.setWholesaler(ni.getWholesaler());
        item.setPrice(ni.getPrice());
        item.setDiscountPrice(ni.getDiscountPrice());
        item.setDiscountPercentage(ni.getDiscountPercentage());
        if (!ni.getDiscountStarts().isEmpty()) {
            item.setDiscountStarts(LocalDate.parse(ni.getDiscountStarts()));
        }
        if (!ni.getDiscountEnds().isEmpty()) {
            item.setDiscountEnds(LocalDate.parse(ni.getDiscountEnds()));
        }
        item.setStock(ni.getStock());
        if (!ni.getExpirationDate().isEmpty()) {
            item.setExpirationDate(LocalDate.parse(ni.getExpirationDate()));
        }
        item.setDescription(ni.getDescription());
        item.setIsHidden(ni.getIsHidden());
        item.setPromotionLevel(ni.getPromotionLevel());

        itemRepository.save(item);

        if (ni.getImage() != null) {

            imageService.newImage(ni.getImage(), item.getId());
        }
    }

    public Boolean hasErrorsOnCreation(NewItem ni, BindingResult br) {

        Double binarybitesToMegabitesCoefficient = 0.00000095367432;

        if (ni.getImage() != null) {
            if (ni.getImage().getSize() * binarybitesToMegabitesCoefficient >= 20) {

                br.rejectValue("image", "error.newItem", "The image is too large (Max. 20 MB).");
            }
        }

        if (itemRepository.findByProductNumber(ni.getProductNumber()) != null) {

            br.rejectValue("productNumber", "error.newItem", "An item with this product number already exists.");
        }

        if (ni.getPrice() != null && ni.getDiscountPercentage() != null && ni.getDiscountPrice() != null) {
            if (ni.getDiscountPrice() != round((ni.getPrice() * (100 - ni.getDiscountPercentage()) / 100), 3)) {

                br.rejectValue("discountPrice", "error.newItem", "Discount price does not correlate with discount percentage.");
            }
        }

        return br.hasErrors();
    }

    public Boolean hasErrorsOnEditing(NewEditItem nei, BindingResult br) {

        if (itemRepository.findByProductNumber(nei.getProductNumber()) != null) {

            br.rejectValue("productNumber", "error.newItem", "An item with this product number already exists.");
        }

        if (nei.getDiscountPrice() != round((nei.getPrice() * (100 - nei.getDiscountPercentage()) / 100), 3)) {

            br.rejectValue("discountPrice", "error.newItem", "Discount price does not correlate with discount percentage.");
        }

        return br.hasErrors();
    }

    public void editItem(Long id, NewEditItem nei) {

        Item item = itemRepository.getById(id);

        item.setItemType(nei.getItemType());
        item.setCategory(nei.getCategory());
        item.setProductNumber(nei.getProductNumber());
        item.setName(nei.getName());
        item.setAmount(nei.getAmount());
        item.setUnit(nei.getUnit());
        item.setManufacturer(nei.getManufacturer());
        item.setWholesaler(nei.getWholesaler());
        item.setPrice(nei.getPrice());
        item.setDiscountPrice(nei.getDiscountPrice());
        item.setDiscountPercentage(nei.getDiscountPercentage());
        if (nei.getDiscountStarts() != null) {
            item.setDiscountStarts(LocalDate.parse(nei.getDiscountStarts()));
        }
        if (nei.getDiscountEnds() != null) {
            item.setDiscountEnds(LocalDate.parse(nei.getDiscountEnds()));
        }
        item.setStock(nei.getStock());
        if (nei.getExpirationDate() != null) {
            item.setExpirationDate(LocalDate.parse(nei.getExpirationDate()));
        }
        item.setDescription(nei.getDescription());
        item.setIsHidden(nei.getIsHidden());
        item.setPromotionLevel(nei.getPromotionLevel());

        itemRepository.save(item);

    }

    public void hideItem(Long id) {

        Item item = itemRepository.getById(id);

        item.setIsHidden(true);

        itemRepository.save(item);
    }

    public void unHideItem(Long id) {

        Item item = itemRepository.getById(id);

        item.setIsHidden(false);

        itemRepository.save(item);
    }

    public void deleteItem(Long id) {

        if (itemRepository.findById(id) != null) {

            itemRepository.deleteById(id);
        }
    }

    public void createItemsForDevelopment() throws IOException {

        if (Arrays.asList(environment.getActiveProfiles()).contains("dev")) {

            if (itemRepository.findAll().isEmpty()) {

                Category drink = categoryService.newMainCategory("Drink");

                Category soda = categoryService.newSubCategory("Soda", drink);
                
                Category lightSoda = categoryService.newSubCategory("Light Soda", soda);

                Category alcohol = categoryService.newSubCategory("Alcohol", drink);

                Category beer = categoryService.newSubCategory("Beer", alcohol);

                Category lager = categoryService.newSubCategory("Lager", beer);

                Category stout = categoryService.newSubCategory("Stout", beer);

                Category vegetables = categoryService.newMainCategory("Vegetables");
                
                Category fruit = categoryService.newSubCategory("Fruit", vegetables);

                Category apple = categoryService.newSubCategory("Apple", fruit);

                newItem(new NewItem("PRODUCT", soda, "5017726180034", "Coca-Cola", "500", Unit.ML, "The Coca-Cola Company", "Kesko", 1.90, 1.90, 0.0, "", "", 151.0, "2022-03-01", "A refreshing beverage.", null, false, PromotionLevel.NORMAL));

                newItem(new NewItem("PRODUCT", soda, "5449000050205", "Coca-Cola light", "500", Unit.ML, "The Coca-Cola Company", "Kesko", 1.90, 1.90, 0.0, "", "", 122.0, "2022-11-05", "A refreshing, sugarless beverage.", null, false, PromotionLevel.NORMAL));

                newItem(new NewItem("PRODUCT", lager, "6415600501613", "Budweiser", "333", Unit.ML, "Anheuser-Busch", "Kesko", 3.51, 3.51, 0.0, "", "", 59.0, "2023-01-01", "American-style pale lager.", null, false, PromotionLevel.NORMAL));

                newItem(new NewItem("PRODUCT", lager, "8594404110127", "Pilsner Urquell", "333", Unit.ML, "Pilsner Urquell Brewery", "Kesko", 3.48, 3.48, 0.0, "", "", 12.0, "2022-03-10", "Lager-type beer from Czech Republic.", null, false, PromotionLevel.NORMAL));

                newItem(new NewItem("PRODUCT", lager, "6415600549639", "Karhu", "500", Unit.ML, "Sinebrychoff/Carlsberg", "Kesko", 2.20, 2.20, 0.0, "", "", 212.0, "2022-02-12", "Finnish-style pale lager.", null, false, PromotionLevel.NORMAL));

                newItem(new NewItem("PRODUCT", lager, "5000213000137", "Guinness Extra Stout", "500", Unit.ML, "Guinness Brewery", "Kesko", 4.20, 4.20, 0.0, "", "", 12.0, "2022-06-04", "As deep as Guinness Original’s colour is its taste. Crisp barley cuts through hops. A bite draws you in, bold flavours linger. Bitter marries sweet. A rich, refreshing taste. Brewed with skill. Built to last.", null, false, PromotionLevel.NORMAL));

                newItem(new NewItem("PRODUCT", apple, "5000213000137", "Pink Lady", "1", Unit.KG, "", "Kesko", 5.15, 5.15, 0.0, "", "", 21.53, "", "Country of origin: Italy", null, false, PromotionLevel.NORMAL));

                newItem(new NewItem("PRODUCT", apple, "2000519300002", "Royal Gala", "1", Unit.KG, "", "Kesko", 3.42, 3.42, 0.0, "", "", 5.02, "", "Country of origin: Italy", null, false, PromotionLevel.NORMAL));

                newItem(new NewItem("PRODUCT", apple, "6408643149867", "Shampion", "1", Unit.KG, "", "Kesko", 2.20, 2.20, 0.0, "", "", 15.22, "", "Country of origin: Poland", null, false, PromotionLevel.NORMAL));
            }
        }
    }

    public static double round(double value, int places) {

        if (places < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal bd = BigDecimal.valueOf(value);

        bd = bd.setScale(places, RoundingMode.HALF_UP);

        return bd.doubleValue();
    }
}
