package technikal.task.fishmarket.services;


import org.springframework.data.jpa.repository.JpaRepository;
import technikal.task.fishmarket.models.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
}