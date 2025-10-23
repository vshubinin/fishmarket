package technikal.task.fishmarket.models;

import java.util.List;

public class ProductDto {
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    private String name;
    private String description;
    private List<String> imageUrls;
}
