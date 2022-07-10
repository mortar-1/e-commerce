package mortar.euroshopper.eCommerceApplication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NewUpdatePassword {
    
    private String currentPassword;
    
    @ValidPassword
    private  String newPassword;
    
    private  String confirmNewPassword;

}
