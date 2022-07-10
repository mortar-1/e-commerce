package mortar.euroshopper.eCommerceApplication;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Item implements Serializable, Comparable<Item> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String itemType;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "category")
    private Category category;

    private String productNumber;

    private String name;

    private String amount;

    private Unit unit;

    private String manufacturer;

    private String wholesaler;

    private Double price;

    private Double discountPrice;

    private Double discountPercentage;
    
    private LocalDate discountStarts;
    
    private LocalDate discountEnds;

    private Double stock;
    
    private LocalDate expirationDate;

    @Column(length = 10000)
    private String description;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Image> images = new ArrayList<>();

    private Long mainImageId;

    private Boolean isHidden;
            
    private PromotionLevel promotionLevel;

    @Override
    public int compareTo(Item other) {

        return name.compareToIgnoreCase(other.name);
    }

    public Item(Long productNumber, String name, String manufacturer, String wholesaler, Double price, String description, Double stock, Category category) {

        this.name = name;
        this.price = price;
        this.description = description;
        this.stock = stock;
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof Item)) {
            return false;
        }

        Item other = (Item) o;

        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {

        return getClass().hashCode();
    }

}
