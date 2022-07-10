package mortar.euroshopper.eCommerceApplication;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Category extends AbstractPersistable<Long> {

    @NotNull
    private String name;

    @JsonBackReference
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "parentCategory")
    private Category parentCategory;

    @OneToMany(mappedBy = "parentCategory")
    private List<Category> subCategories = new ArrayList<>();

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "category")
    private List<Item> items = new ArrayList<>();
    
    public Category(String name) {
        
        this.name = name;
    }
    
    public Category(String name, Category parent) {
        
        this.name = name;
        
        this.parentCategory = parent;
    }
    
    public Integer categoryLevel;
    
    @Override
    public String toString() {
        
        return this.name;
    }

}
