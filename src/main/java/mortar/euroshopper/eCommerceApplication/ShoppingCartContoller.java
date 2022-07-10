package mortar.euroshopper.eCommerceApplication;

import org.hibernate.annotations.LazyCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ShoppingCartContoller {

    @Autowired
    private ShoppingCart shoppingCart;

    @Autowired
    private ItemRepository itemRepository;

    @GetMapping("/cart")
    public String viewCart(Model model) {

        model.addAttribute("items", shoppingCart.getItems());

        return "cart";
    }

    @PostMapping("/cart/items/{id}")
    public String addItem(@PathVariable Long id, @RequestParam String redirectTo) {

        Item item = itemRepository.getById(id);

        shoppingCart.addToCart(item);

        return "redirect:" + redirectTo;
    }

    @PostMapping("/cart/items/{id}/remove")
    public String removeItem(@PathVariable Long id, @RequestParam String redirectTo) {

        Item item = itemRepository.getById(id);

        shoppingCart.removeFromCart(item);

        return "redirect:" + redirectTo;
    }

}
