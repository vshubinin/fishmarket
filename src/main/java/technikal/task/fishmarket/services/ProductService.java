package technikal.task.fishmarket.services;

import org.springframework.stereotype.Service;
import technikal.task.fishmarket.models.Product;
import technikal.task.fishmarket.models.ProductImage;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductImageRepository imageRepository;

    public ProductService(ProductRepository productRepository, ProductImageRepository imageRepository) {
        this.productRepository = productRepository;
        this.imageRepository = imageRepository;
    }

    public Product addProductWithImages(Product product, List<String> imageUrls) {
        imageUrls.forEach(url -> product.addImage(new ProductImage(url)));
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}