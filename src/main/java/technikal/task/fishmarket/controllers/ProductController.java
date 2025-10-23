package technikal.task.fishmarket.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import technikal.task.fishmarket.models.Product;
import technikal.task.fishmarket.models.ProductDto;
import technikal.task.fishmarket.services.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody ProductDto dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        Product saved = productService.addProductWithImages(product, dto.getImageUrls());
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public List<Product> getAll() {
        return productService.getAllProducts();
    }
}
