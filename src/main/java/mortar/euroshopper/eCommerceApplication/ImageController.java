package mortar.euroshopper.eCommerceApplication;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ImageController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private ItemService itemService;

    @GetMapping("/images/{id}")
    public void showImage(@PathVariable Long id, HttpServletResponse response) throws IOException {

        imageService.viewImage(id, response);
    }
    
    @GetMapping("/images/{id}/view")
    public String viewImage(Model model, @PathVariable Long id) {
        
        imageService.addAttributesToModelForPageImage(model, id);
        
        return "image";
    }

    @GetMapping("/items/{itemId}/images")
    public String viewItemImages(Model model, @PathVariable Long itemId) {

        imageService.addAttributesToPageImages(model, itemId);

        return "itemImages";
    }

    @PostMapping("/items/{itemId}/image")
    public String addNewImage(Model model, @PathVariable Long itemId, @Valid @ModelAttribute NewImage newImage, BindingResult bindingResult) throws IOException {

        if (imageService.hasErrorsOnCreation(newImage, bindingResult)) {

            itemService.addAttributesToModelForPageItem(model, itemId);

            return "item";
        }

        imageService.newImage(newImage.getContent(), itemId);

        return "redirect:/items/" + itemId;
    }

    @PostMapping("/items/{itemId}/images/{imageId}")
    public String deleteImage(@PathVariable Long itemId, @PathVariable Long imageId) {

        imageService.deleteImage(imageId);

        return "redirect:/items/" + itemId + "/images";
    }

}
