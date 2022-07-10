package mortar.euroshopper.eCommerceApplication;

import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NewImage {

    @NotEmpty
    private MultipartFile content;
    
}
