package technikal.task.fishmarket.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "product")
public class Product {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private String name;
	private double price;
	private Date catchDate;

	public List<ProductImage> getImages() {
		return images;
	}

	public void setImages(List<ProductImage> images) {
		this.images = images;
	}


	// Теперь список фото
	@OneToMany(mappedBy = "fish", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProductImage> images = new ArrayList<>();

	public void addImage(ProductImage image) {
		image.setFish(this);
		this.images.add(image);
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
