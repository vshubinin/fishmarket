package technikal.task.fishmarket;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import technikal.task.fishmarket.models.UserEntity;
import technikal.task.fishmarket.services.CustomUserDetailsService;
import technikal.task.fishmarket.services.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomUserDetailsServiceTest {

    private UserRepository userRepository;
    private CustomUserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userDetailsService = new CustomUserDetailsService(userRepository);
    }

    @Test
    void shouldLoadUserByUsername_whenUserExists() {
        UserEntity user = new UserEntity();
        user.setUsername("john");
        user.setPassword("secret");
        user.setRole("ADMIN");

        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailsService.loadUserByUsername("john");

        assertThat(userDetails.getUsername()).isEqualTo("john");
        assertThat(userDetails.getPassword()).isEqualTo("secret");
        assertThat(userDetails.getAuthorities()).extracting("authority").containsExactly("ROLE_ADMIN");
    }

    @Test
    void shouldThrowUsernameNotFoundException_whenUserDoesNotExist() {
        when(userRepository.findByUsername("missing")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("missing"));
    }
}
