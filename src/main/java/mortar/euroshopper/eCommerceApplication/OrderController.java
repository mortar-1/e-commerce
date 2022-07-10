package mortar.euroshopper.eCommerceApplication;

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
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/orders")
    public String viewOrders(Model model) {

        orderService.addAttributesToModelForPageOrders(model);

        return "orders";
    }

    @GetMapping("/orders/{id}")
    public String viewOrder(Model model, @PathVariable Long id, @ModelAttribute NewUpdateOrder newEditOrder) {

        orderService.addAttributesToModelForPageOrder(model, id);

        return "order";
    }

    @GetMapping("/account/orders/{id}")
    public String viewCurrentAccountsOrder() {

        return "order_currentAccount";
    }

    @PostMapping("/cart/order")
    public String addNewOrder(Model model, @Valid @ModelAttribute NewOrder newOrder, BindingResult bindingResult) {

        orderService.newOrder(newOrder);

        return "redirect:/shop";
    }

    @PostMapping("/orders/{id}/update")
    public String updateOrder(Model model, @PathVariable Long id,  @Valid @ModelAttribute NewUpdateOrder newEditOrder, BindingResult br) {

        if (orderService.hasErrorsOnUpdate(newEditOrder, br)) {

            orderService.addAttributesToModelForPageOrder(model, id);

            return "order";
        }

        return "";
    }

    @PostMapping("/orders/{id}/status")
    public String changeStatus(@PathVariable Long id, @RequestParam Status status) {

        return "redirect:";
    }

    @PostMapping("/orders/{id}/delete")
    public String deleteOrder(@PathVariable Long id) {

        orderService.deleteOrder(id);

        return "redirect:/orders";
    }

}
