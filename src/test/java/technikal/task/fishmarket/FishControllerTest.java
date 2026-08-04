package technikal.task.fishmarket;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import technikal.task.fishmarket.controllers.FishController;
import technikal.task.fishmarket.models.Product;
import technikal.task.fishmarket.models.ProductDto;
import technikal.task.fishmarket.services.ProductRepository;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FishControllerTest {

    @Mock
    private ProductRepository repo;

    @InjectMocks
    private FishController controller;

    @Test
    void shouldReturnIndexViewAndAddFishListForHome() {
        Model model = org.mockito.Mockito.mock(Model.class);
        List<Product> products = List.of(new Product());
        when(repo.findAll(Sort.by(Sort.Direction.DESC, "id"))).thenReturn(products);

        String view = controller.home(model);

        assertThat(view).isEqualTo("index");
        verify(model).addAttribute(eq("fishlist"), eq(products));
    }

    @Test
    void shouldReturnCreateFishWhenValidationFails() {
        ProductDto dto = new ProductDto();
        dto.setImageFile(List.of());
        BindingResult result = org.mockito.Mockito.mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(true);

        String view = controller.addFish(dto, result);

        assertThat(view).isEqualTo("createFish");
    }

    @Test
    void shouldSaveOnlyOneImageWhenSingleFileIsUploaded() throws Exception {
        ProductDto dto = new ProductDto();
        dto.setName("Сом");
        dto.setPrice(50.0);
        MockMultipartFile file = new MockMultipartFile(
                "imageFile",
                "fish.png",
                "image/png",
                "image-data".getBytes(StandardCharsets.UTF_8)
        );
        dto.setImageFile(List.of(file));

        BindingResult result = org.mockito.Mockito.mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);
        when(repo.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        String view = controller.addFish(dto, result);

        assertThat(view).isEqualTo("redirect:/fish");

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(repo).save(captor.capture());
        assertThat(captor.getValue().getImages()).hasSize(1);
    }
}
