package mortar.euroshopper.eCommerceApplication;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NewEditItem {
    
    private String itemType;

    private Category category;

    private String productNumber;

    @NotEmpty
    private String name;

    private String amount;

    private Unit unit;

    @Length(max = 100)
    private String manufacturer;

    @Length(max = 100)
    private String wholesaler;

    private Double price;

    private Double discountPrice;

    private Double discountPercentage;

    private String discountStarts;

    private String discountEnds;

    private Double stock;

    private String expirationDate;

    @Column(length = 10000)
    private String description;

    private Boolean isHidden;

    private PromotionLevel promotionLevel;

}
