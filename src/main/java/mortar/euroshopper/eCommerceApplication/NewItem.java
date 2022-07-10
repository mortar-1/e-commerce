package mortar.euroshopper.eCommerceApplication;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NewItem {
    
    private String itemType;

    private Category category;

    @NotNull(message = "Product number needed.")
    private String productNumber;

    @NotEmpty(message = "Item name needed.")
    private String name;

    private String amount;

    private Unit unit;

    @Length(max = 100, message = "Maximum 100 characters.")
    private String manufacturer;

    @Length(max = 100, message = "Maximum 100 characters.")
    private String wholesaler;

    private Double price;

    private Double discountPrice;

    private Double discountPercentage;

    private String discountStarts;

    private String discountEnds;

    private Double stock;

    private String expirationDate;

    @Column(length = 10000)
    @Length(max = 10000, message = "Maximum 10 000 characters.")
    private String description;

    private MultipartFile image;

    private Boolean isHidden;
    
    private PromotionLevel promotionLevel;

}
