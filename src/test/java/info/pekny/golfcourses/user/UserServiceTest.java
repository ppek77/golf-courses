package info.pekny.golfcourses.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, passwordEncoder);
    }

    @Test
    void createUser() {
        UserCreateForm form = new UserCreateForm();
        form.setFullName("John Doe");
        form.setEmail("john@example.com");
        form.setPassword("password123");
        form.setRole(Role.USER);
        form.setActive(true);

        when(passwordEncoder.encode("password123")).thenReturn("hashed");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User created = userService.create(form);

        assertThat(created.getFullName()).isEqualTo("John Doe");
        assertThat(created.getEmail()).isEqualTo("john@example.com");
        assertThat(created.getPassword()).isEqualTo("hashed");
        assertThat(created.getRole()).isEqualTo(Role.USER);
        assertThat(created.isActive()).isTrue();
    }

    @Test
    void updateUserWithoutPasswordChange() {
        User existing = new User("Old Name", "old@example.com", "oldhash", Role.USER);
        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        UserEditForm form = new UserEditForm();
        form.setFullName("New Name");
        form.setEmail("new@example.com");
        form.setRole(Role.ADMIN);
        form.setActive(false);
        form.setPassword("");

        User updated = userService.update(1L, form);

        assertThat(updated.getFullName()).isEqualTo("New Name");
        assertThat(updated.getPassword()).isEqualTo("oldhash");
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void updateProfileWithCorrectPassword() {
        User existing = new User("User", "user@example.com", "hashed", Role.USER);
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(existing));
        when(passwordEncoder.matches("currentpw", "hashed")).thenReturn(true);
        when(passwordEncoder.encode("newpassword")).thenReturn("newhash");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        ProfileEditForm form = new ProfileEditForm();
        form.setFullName("Updated Name");
        form.setCurrentPassword("currentpw");
        form.setNewPassword("newpassword");

        userService.updateProfile("user@example.com", form);

        assertThat(existing.getFullName()).isEqualTo("Updated Name");
        assertThat(existing.getPassword()).isEqualTo("newhash");
    }

    @Test
    void updateProfileWithWrongPasswordThrows() {
        User existing = new User("User", "user@example.com", "hashed", Role.USER);
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(existing));
        when(passwordEncoder.matches("wrongpw", "hashed")).thenReturn(false);

        ProfileEditForm form = new ProfileEditForm();
        form.setFullName("Updated Name");
        form.setCurrentPassword("wrongpw");

        assertThatThrownBy(() -> userService.updateProfile("user@example.com", form))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Současné heslo není správné");
    }
}
