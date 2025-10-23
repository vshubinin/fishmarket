package technikal.task.fishmarket.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "fish")
public class Fish {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private String name;
	private double price;
	private Date catchDate;

	public List<FishImage> getFishImage() {
		return fishImage;
	}

	public void setFishImage(List<FishImage> fishImage) {
		this.fishImage = fishImage;
	}

	@OneToMany(mappedBy = "fish", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<FishImage> fishImage;

	public List<FishImage> getImages() {
		return images;
	}

	public void setImages(List<FishImage> images) {
		this.images = images;
	}


	// Теперь список фото
	@OneToMany(mappedBy = "fish", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<FishImage> images = new ArrayList<>();

	public void addImage(FishImage image) {
		image.setFish(this);
		images.add(image);
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public Date getCatchDate() {
		return catchDate;
	}
	public void setCatchDate(Date catchDate) {
		this.catchDate = catchDate;
	}


}
