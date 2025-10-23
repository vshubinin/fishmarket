package technikal.task.fishmarket.services;

import org.springframework.data.jpa.repository.JpaRepository;
import technikal.task.fishmarket.models.ProductImage;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {}

