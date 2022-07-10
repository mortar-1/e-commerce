package mortar.euroshopper.eCommerceApplication;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
@Table(name = "orders")
public class Order implements Serializable, Comparable<Order> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Account account;

    private String email;

    private String fistName;

    private String lastName;

    private String address;

    private String postalCode;

    private String city;

    private String country;

    private String phoneNumber;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
    private List<OrderItem> orderItems;

    private Double sum;

    private LocalDateTime created;

    private LocalDateTime edited;

    private Status status;

    public Order(String em, String fn, String ln, String ad, String pc, String ci, String co, String pn, Double su, LocalDateTime cr) {

        this.email = em;
        this.fistName = fn;
        this.lastName = ln;
        this.address = ad;
        this.postalCode = pc;
        this.city = ci;
        this.country = co;
        this.phoneNumber = pn;
        this.orderItems = new ArrayList<>();
        this.sum = su;
        this.created = cr;
        this.status = Status.RECIEVED;
    }

    @Transient
    public String getOrderNumber() {

        DecimalFormat df = new DecimalFormat("ORD000000");

        return df.format(id);
    }

    @Override
    public int compareTo(Order other) {

        return id.compareTo(other.id);
    }

}
