package technikal.task.fishmarket.models;

import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

import java.util.Date;
import java.util.List;

public class FishDto {
	

	@NotBlank
	private String name;
	@Min(0)
	private double price;

	private Date catchDate;
	private List<MultipartFile> imageFile;
	public Date getCatchDate() {
		return catchDate;
	}

	public void setCatchDate(Date catchDate) {
		this.catchDate = catchDate;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}

	public List<MultipartFile> getImageFile() {
		return imageFile;
	}

	public void setImageFile(List<MultipartFile> imageFile) {
		this.imageFile = imageFile;
	}
}
