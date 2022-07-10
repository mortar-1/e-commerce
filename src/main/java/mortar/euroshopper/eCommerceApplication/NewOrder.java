package mortar.euroshopper.eCommerceApplication;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NewOrder {

    @NotEmpty
    @Email
    public String eMail;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @NotEmpty
    private String address;

    @NotEmpty
    private String postalCode;

    @NotEmpty
    private String city;
    
    @NotEmpty
    private String country;
    
    @NotEmpty
    private String phoneNumber;
}
