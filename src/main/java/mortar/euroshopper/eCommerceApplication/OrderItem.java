package mortar.euroshopper.eCommerceApplication;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderItem extends AbstractPersistable<Long>{

    @OneToOne
    private Item item;

    private Long itemCount;
    
    @ManyToOne
    private Order order;

}
