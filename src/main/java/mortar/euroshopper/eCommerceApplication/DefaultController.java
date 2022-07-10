package mortar.euroshopper.eCommerceApplication;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DefaultController {

    @GetMapping("/")
    public String goToShop() {

        return "redirect:/shop";
    }

    @GetMapping("/privacy")
    public String viewPrivacyPolicy() {

        return "privacy-policy";
    }
}
