package technikal.task.fishmarket.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import technikal.task.fishmarket.models.UserEntity;
import technikal.task.fishmarket.services.UserRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if(userRepository.findByUsername("admin").isEmpty()) {
                UserEntity admin = new UserEntity();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin"));
                admin.setRole("ADMIN");
                userRepository.save(admin);
            }

            if(userRepository.findByUsername("user").isEmpty()) {
                UserEntity user = new UserEntity();
                user.setUsername("user");
                user.setPassword(passwordEncoder.encode("user"));
                user.setRole("USER");
                userRepository.save(user);
            }
        };
    }
}