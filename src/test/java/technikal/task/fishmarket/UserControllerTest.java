package technikal.task.fishmarket;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import technikal.task.fishmarket.controllers.UserController;
import technikal.task.fishmarket.models.UserEntity;
import technikal.task.fishmarket.services.UserRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserControllerTest {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private UserController userController;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        userController = new UserController(userRepository, passwordEncoder);
    }

    @Test
    void shouldReturnUsersListView_andAddUsersToModel() {
        Model model = mock(Model.class);
        UserEntity user = new UserEntity();

        when(userRepository.findAll()).thenReturn(List.of(user));

        String view = userController.listUsers(model);

        assertThat(view).isEqualTo("users/list");
        verify(model).addAttribute(eq("users"), eq(List.of(user)));
    }

    @Test
    void shouldReturnNewUserFormView() {
        Model model = mock(Model.class);

        String view = userController.newUserForm(model);

        assertThat(view).isEqualTo("users/new");
        verify(model).addAttribute(eq("user"), any(UserEntity.class));
    }

    @Test
    void shouldSaveUserAndRedirect() {
        UserEntity user = new UserEntity();
        user.setPassword("raw");

        when(passwordEncoder.encode("raw")).thenReturn("encoded");

        String view = userController.createUser(user);

        assertThat(view).isEqualTo("redirect:/admin/users");
        assertThat(user.getPassword()).isEqualTo("encoded");
        verify(userRepository).save(user);
    }
}
