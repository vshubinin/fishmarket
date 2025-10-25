package technikal.task.fishmarket.controllers;


import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
import technikal.task.fishmarket.models.Fish;
import technikal.task.fishmarket.models.FishDto;
import technikal.task.fishmarket.models.FishImage;
import technikal.task.fishmarket.services.FishRepository;

@Controller
public class FishController {
	
	@Autowired
	private FishRepository repo;
	
	@GetMapping("/fish")
	public String showFishList(Model model) {
		List<Fish> fishlist = repo.findAll(Sort.by(Sort.Direction.DESC, "id"));
		model.addAttribute("fishlist", fishlist);
		return "index";
	}

	@GetMapping("/fish/{id}")
	public String viewFish(@PathVariable Long id, Model model) {
		Fish fish = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Невірний ID риби: " + id));
		model.addAttribute("fish", fish);
		return "viewFish";
	}
	
	@GetMapping("/fish/create")
	public String showCreatePage(Model model) {
		FishDto fishDto = new FishDto();
		model.addAttribute("fishDto", fishDto);
		return "createFish";
	}
	
	@GetMapping("/fish/delete")
	public String deleteFish(@RequestParam long id) {
		
		try {
			
			Fish fish = repo.findById(id).get();
			
			Path imagePath = Paths.get("public/images/"+fish.getImages().get(0).getFileName());
			Files.delete(imagePath);
			repo.delete(fish);
			
		}catch(Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
		}
		
		return "redirect:/fish";
	}

	@PostMapping("/fish/create")
	public String addFish(@Valid @ModelAttribute FishDto fishDto, BindingResult result) {

		if (fishDto.getImageFile() == null || fishDto.getImageFile().isEmpty()) {
			result.addError(new FieldError("fishDto", "imageFile", "Потрібне фото рибки"));
		}

		if (result.hasErrors()) {
			return "createFish";
		}

		// Создаём сущность рыбы
		Fish fish = new Fish();
		fish.setCatchDate(new Date());
		fish.setName(fishDto.getName());
		fish.setPrice(fishDto.getPrice());

		String uploadDir = "public/images/";
		Path uploadPath = Paths.get(uploadDir);

		try {
			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}

			// 🔁 Перебор всех файлов
			for (MultipartFile file : fishDto.getImageFile()) {
				if (file.isEmpty()) continue;

				String storageFileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

				try (InputStream inputStream = file.getInputStream()) {
					Files.copy(inputStream, uploadPath.resolve(storageFileName), StandardCopyOption.REPLACE_EXISTING);
				}

				// Создаём объект FishImage и добавляем в рыбу
				FishImage img = new FishImage();
				img.setFileName(storageFileName);
				img.setFish(fish); // важно связать обратно

				fish.addImage(img);
			}

		} catch (Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
		}

		// 💾 Сохраняем рыбу вместе с изображениями
		repo.save(fish);

		return "redirect:/fish";
	}

}
