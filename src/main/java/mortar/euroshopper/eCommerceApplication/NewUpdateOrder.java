package mortar.euroshopper.eCommerceApplication;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.OneToMany;

public class NewUpdateOrder {
    
    private String eMail;

    private String fistName;

    private String lastName;

    private String address;

    private String postalCode;

    private String city;

    private String country;

    private String phoneNumber;

    private List<Long> items;

    private Double sum;

    private LocalDateTime created;
    
    private LocalDateTime edited;

    private Status status;

}
