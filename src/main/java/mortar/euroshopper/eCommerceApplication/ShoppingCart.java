package mortar.euroshopper.eCommerceApplication;

import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Data
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ShoppingCart {

    private Map<Item, Long> items = new HashMap<>();

    public void addToCart(Item item) {

        Long itemCount = this.items.getOrDefault(item, 0L) + 1L;

        this.items.put(item, itemCount);

    }

    public void removeFromCart(Item item) {

        if (this.items.get(item) == 1L) {

            this.items.remove(item);
            
            return;
        }

        Long itemCount = this.items.get(item) - 1L;

        this.items.put(item, itemCount);
    }

    public Double getSum() {

        return this.items.keySet().stream()
                .map(item -> (item.getPrice() * this.items.get(item)))
                .reduce(0.0, (currentSum, nextCost) -> currentSum + nextCost);

    }

}
