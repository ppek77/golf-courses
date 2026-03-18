package info.pekny.golfcourses.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    public User create(UserCreateForm form) {
        User user = new User(
                form.getFullName(),
                form.getEmail(),
                passwordEncoder.encode(form.getPassword()),
                form.getRole()
        );
        user.setActive(form.isActive());
        return userRepository.save(user);
    }

    @Transactional
    public User update(Long id, UserEditForm form) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
        user.setFullName(form.getFullName());
        user.setEmail(form.getEmail());
        user.setRole(form.getRole());
        user.setActive(form.isActive());
        if (form.getPassword() != null && !form.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(form.getPassword()));
        }
        return userRepository.save(user);
    }

    @Transactional
    public void updateProfile(String email, ProfileEditForm form) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + email));
        if (!passwordEncoder.matches(form.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Současné heslo není správné");
        }
        user.setFullName(form.getFullName());
        if (form.getNewPassword() != null && !form.getNewPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(form.getNewPassword()));
        }
        userRepository.save(user);
    }
}
