package mortar.euroshopper.eCommerceApplication;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long>{

    List<Image> findByItemId(Long itemId);
}
