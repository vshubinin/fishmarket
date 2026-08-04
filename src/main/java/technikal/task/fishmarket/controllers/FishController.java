package technikal.task.fishmarket.controllers;


import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import technikal.task.fishmarket.models.Product;
import technikal.task.fishmarket.models.ProductDto;
import technikal.task.fishmarket.models.ProductImage;
import technikal.task.fishmarket.services.ProductRepository;

@Controller
public class FishController {

    @Autowired
    private ProductRepository repo;

    @GetMapping("/")
    public String home(Model model) {
        List<Product> fishlist = repo.findAll(Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("fishlist", fishlist);
        return "index";
    }

    @GetMapping("/fish")
    public String showFishList(Model model) {
        List<Product> fishlist = repo.findAll(Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("fishlist", fishlist);
        return "index";
    }

    @GetMapping("/fish/{id}")
    public String viewFish(@PathVariable Long id, Model model) {
        Product fish = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Невірний ID риби: " + id));
        model.addAttribute("fish", fish);
        return "viewFish";
    }

    @GetMapping("/fish/create")
    public String showCreatePage(Model model) {
        ProductDto fishDto = new ProductDto();
        model.addAttribute("fishDto", fishDto);
        return "createFish";
    }

    @GetMapping("/fish/edit")
    public String showEditPage(@RequestParam long id, Model model) {
        Product product = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Невірний ID риби: " + id));

        ProductDto fishDto = new ProductDto();
        fishDto.setId(product.getId());
        fishDto.setName(product.getName());
        fishDto.setPrice(product.getPrice());
        model.addAttribute("fishDto", fishDto);
        return "editFish";
    }

    @PostMapping("/fish/update")
    public String updateFish(@Valid @ModelAttribute ProductDto productDto, BindingResult result) {
        if (result.hasErrors()) {
            return "editFish";
        }

        Product product = repo.findById(productDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Невірний ID риби: " + productDto.getId()));

        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());

        if (productDto.getImageFile() != null && productDto.getImageFile().stream().anyMatch(file -> !file.isEmpty())) {
            if (product.getImages() != null) {
                for (ProductImage img : new ArrayList<>(product.getImages())) {
                    try {
                        Files.deleteIfExists(Paths.get("public/images/" + img.getFileName()));
                    } catch (Exception ex) {
                        System.out.println("Exception deleting old image: " + ex.getMessage());
                    }
                }
                product.getImages().clear();
            }
            saveImages(product, productDto.getImageFile());
        }

        repo.save(product);

        return "redirect:/fish";
    }

    @GetMapping("/fish/delete")
    public String deleteFish(@RequestParam long id) {
        Product product = repo.findById(id).get();
        try {
            Path imagePath = Paths.get("public/images/" + product.getImages().get(0).getFileName());
            Files.delete(imagePath);
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        } finally {
            repo.delete(product);
        }

        return "redirect:/fish";
    }

    @PostMapping("/fish/create")
    public String addFish(@Valid @ModelAttribute ProductDto productDto, BindingResult result) {

        if (productDto.getImageFile().isEmpty()) {
            result.addError(new FieldError("productDto", "imageFile", "Потрібне фото рибки"));
        }

        if (result.hasErrors()) {
            return "createFish";
        }

        // Создаём сущность рыбы
        Product product = new Product();
        product.setCatchDate(new Date());
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());

        String uploadDir = "public/images/";
        Path uploadPath = Paths.get(uploadDir);

        try {
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 🔁 Перебор всех файлов
            for (MultipartFile file : productDto.getImageFile()) {
                if (file.isEmpty()) continue;

                String originalName = file.getOriginalFilename();
                String storageFileName = System.currentTimeMillis() + "_" + originalName;

                try (InputStream inputStream = file.getInputStream()) {
                    Files.copy(
                            inputStream,
                            uploadPath.resolve(storageFileName),
                            StandardCopyOption.REPLACE_EXISTING
                    );
                }

                ProductImage img = new ProductImage();
                img.setFileName(storageFileName);
                img.setFish(product);

                product.addImage(img);
            }
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }

        // 💾 Сохраняем рыбу вместе с изображениями
        saveImages(product, productDto.getImageFile());
        repo.save(product);

        return "redirect:/fish";
    }

    private void saveImages(Product product, List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            return;
        }

        String uploadDir = "public/images/";
        Path uploadPath = Paths.get(uploadDir);

        try {
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            for (MultipartFile file : files) {
                if (file.isEmpty()) continue;

                String originalName = file.getOriginalFilename();
                String storageFileName = System.currentTimeMillis() + "_" + originalName;

                try (InputStream inputStream = file.getInputStream()) {
                    Files.copy(
                            inputStream,
                            uploadPath.resolve(storageFileName),
                            StandardCopyOption.REPLACE_EXISTING
                    );
                }

                ProductImage img = new ProductImage();
                img.setFileName(storageFileName);
                img.setFish(product);
                product.addImage(img);
            }
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
    }

}
