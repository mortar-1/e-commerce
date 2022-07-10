package mortar.euroshopper.eCommerceApplication;

import java.time.LocalDateTime;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import java.util.stream.Collectors;
import javax.servlet.http.HttpSession;
import org.springframework.validation.BindingResult;

@Service
public class OrderService {

    @Autowired
    private ShoppingCart shoppingCart;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private HttpSession session;

    public void addAttributesToModelForPageOrders(Model model) {

        model.addAttribute("orders", orderRepository.findAll());
    }

    public void addAttributesToModelForPageOrder(Model model, Long id) {

        model.addAttribute("order", orderRepository.getById(id));
    }

    public Boolean hasErrorsOnUpdate(NewUpdateOrder nuo, BindingResult br) {

        return br.hasErrors();
    }

    public void newOrder(NewOrder newOrder) {

        Order order = new Order(
                newOrder.getEMail(),
                newOrder.getFirstName(),
                newOrder.getLastName(),
                newOrder.getAddress(),
                newOrder.getPostalCode(),
                newOrder.getCity(),
                newOrder.getCountry(),
                newOrder.getPhoneNumber(),
                shoppingCart.getSum(),
                LocalDateTime.now());
        
        order.setOrderItems(shoppingCart.getItems().keySet().stream()
                .map(item -> new OrderItem(item, shoppingCart.getItems().get(item), order))
                .collect(Collectors.toCollection(ArrayList::new)));

        orderRepository.save(order);

        shoppingCart.getItems().clear();
    }

    public void deleteOrder(Long id) {

        orderRepository.deleteById(id);
    }

}
