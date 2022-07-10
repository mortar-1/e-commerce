package mortar.euroshopper.eCommerceApplication;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;
    
    @Autowired
    private ItemRepository itemRepository;
    
    public void addAttributesToPageImages(Model model, Long itemId) {
        
        model.addAttribute("images", imageRepository.findByItemId(itemId));
    }
    
    public void  addAttributesToModelForPageImage(Model model, Long id) {
        
        model.addAttribute("image", imageRepository.getById(id));
    }

    public void viewImage(Long id, HttpServletResponse response) throws IOException {

        response.setContentType("image/jpg");

        Image image = imageRepository.getById(id);

        InputStream is = new ByteArrayInputStream(image.getContent());

        IOUtils.copy(is, response.getOutputStream());
    }

    public Boolean hasErrorsOnCreation(NewImage newImage, BindingResult bindingResult) throws IOException {

        Double binarybitesToMegabitesCoefficient = 0.00000095367432;

        if (newImage.getContent().getSize() * binarybitesToMegabitesCoefficient >= 5) {

            bindingResult.rejectValue("image", "error.newImage", "Maximum image size 5MB!");
        }

        if (newImage.getContent().getBytes().length == 0) {

            bindingResult.rejectValue("image", "error.newImage", "No image selected!");
        }

        return bindingResult.hasErrors();
    }
    
    public void newImage(MultipartFile newImage, Long itemId) throws IOException{
        
        Item item = itemRepository.getById(itemId);
        
        Image image = new Image(newImage.getBytes(), item);
                              
        imageRepository.save(image);
        
        if (imageRepository.findByItemId(itemId).size() == 1) {
                        
            item.setMainImageId(image.getId());
        }
        
        item.getImages().add(image);
        
        itemRepository.save(item);
    }
            
    public void deleteImage(Long id) {
        
        imageRepository.deleteById(id);
    }

}
