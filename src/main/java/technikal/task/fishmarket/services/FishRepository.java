package technikal.task.fishmarket.services;

import org.springframework.data.jpa.repository.JpaRepository;

import technikal.task.fishmarket.models.Product;

public interface FishRepository extends JpaRepository<Product, Long> {

}
