package mortar.euroshopper.eCommerceApplication;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NewUpdateAccount {
    
    @NotEmpty
    @Email
    public String email;
    
    @NotEmpty
    @Length(max = 100)
    private String firstName;

    @NotEmpty
    @Length(max = 100)
    private String lastName;

    @NotEmpty
    @Length(max = 100)
    private String address;

    @NotEmpty
    @Length(max = 10)
    private String postalCode;

    @NotEmpty
    @Length(max = 100)
    private String city;
    
    @NotEmpty
    @Length(max = 100)
    private String country;
    
    @NotEmpty
    @Length(max = 20)
    private String phoneNumber;
    
    private String currentPassword;
    
    private String adminPassword;

}
