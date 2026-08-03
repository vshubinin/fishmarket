package technikal.task.fishmarket;

import org.junit.jupiter.api.Test;
import technikal.task.fishmarket.models.Product;
import technikal.task.fishmarket.models.ProductImage;

import static org.assertj.core.api.Assertions.assertThat;

class ProductTest {

    @Test
    void shouldAddImageToProduct_andSetFishOnImage() {
        Product product = new Product();
        ProductImage image = new ProductImage();
        image.setFileName("fish.png");

        product.addImage(image);

        assertThat(product.getImages()).containsExactly(image);
        assertThat(image.getFish()).isEqualTo(product);
    }
}
