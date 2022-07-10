package mortar.euroshopper.eCommerceApplication;

import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping("/items")
    public String viewItems(Model model, @RequestParam(required = false) String type, @RequestParam(required = false) String category, @RequestParam(required = false) String showHidden, @RequestParam(required = false) String name, @RequestParam(required = false) String productNumber, @RequestParam(required = false) String manufacturer, @RequestParam(required = false) String wholesaler) {

        itemService.addAttributesToModelForPageItems(model, type, category, showHidden, name, productNumber, manufacturer, wholesaler);

        return "items";
    }

    @GetMapping("/items/{id}")
    public String viewItem(Model model, @PathVariable Long id, @ModelAttribute NewImage newImage) {

        itemService.addAttributesToModelForPageItem(model, id);

        return "item";
    }

    @GetMapping("/items/new")
    public String viewNewItem(Model model, @ModelAttribute NewItem newItem) {

        model.addAttribute("newItem", new NewItem());

        return "newItem";
    }

    @GetMapping("/items/{id}/edit")
    public String viewEditItem(Model model, @PathVariable Long id, @ModelAttribute NewEditItem newEditItem) {

        itemService.addAttributesToModelForPageEditItem(model, id);

        return "editItem";
    }

    @GetMapping("/shop")
    public String viewShop(Model model) {

        itemService.addAttributesToModelForPageShop(model);

        return "shop";
    }

    @PostMapping("/items/new")
    public String addItem(Model model, @Valid @ModelAttribute NewItem newItem, BindingResult br) throws IOException {

        if (itemService.hasErrorsOnCreation(newItem, br)) {

            model.addAttribute("newItem", newItem);

            return "newItem";
        }

        itemService.newItem(newItem);

        return "redirect:/items";
    }

    @PostMapping("/items/{id}/edit")
    public String editItem(Model model, @PathVariable Long id, @Valid @ModelAttribute NewEditItem nei, BindingResult br) {

        if (itemService.hasErrorsOnEditing(nei, br)) {

            itemService.addAttributesToModelForPageEditItem(model, id);

            return "editItem";
        }

        itemService.editItem(id, nei);

        return "redirect:/items/" + id + "/edit?itemUpdated=true";
    }

    @PostMapping("/items/{id}/hide")
    public String hideItem(@PathVariable Long id, @RequestParam String redirectTo) {

        itemService.hideItem(id);

        return "redirect:" + redirectTo;
    }

    @PostMapping("/items/{id}/unhide")
    public String unHideItem(@PathVariable Long id, @RequestParam String redirectTo) {

        itemService.unHideItem(id);

        return "redirect:" + redirectTo;
    }

    @PostMapping("/items/{id}/delete")
    public String deleteItem(@PathVariable Long id, @RequestParam String redirectTo) {

        itemService.deleteItem(id);

        return "redirect:" + redirectTo;
    }

    @PostConstruct
    public void atStart() throws IOException {

        itemService.createItemsForDevelopment();
    }
}
