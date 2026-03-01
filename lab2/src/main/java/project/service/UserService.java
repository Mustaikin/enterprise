package project.service;

import project.model.User;
import project.model.Role;
import project.model.enumeration.RoleEnum;  // Добавляем импорт Enum
import project.repository.UserRepository;
import project.repository.RoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User registerUser(String username, String password) {
        // Проверяем, не занят ли логин
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists!");
        }

        // Создаем нового пользователя
        User user = new User();
        user.setUsername(username);

        // Шифруем пароль!!!
        user.setPassword(passwordEncoder.encode(password));

        // ИЗМЕНЕНИЕ: Теперь ищем по Enum, а не по String
        Role userRole = roleRepository.findByName(RoleEnum.ROLE_USER)  // Используем Enum
                .orElseGet(() -> {
                    // ИЗМЕНЕНИЕ: Создаем роль с Enum
                    Role newRole = new Role(RoleEnum.ROLE_USER);
                    return roleRepository.save(newRole);
                });

        // Добавляем роль пользователю
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);

        // Сохраняем пользователя
        return userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional
    public User registerAdmin(String username, String password) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists!");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));

        // Находим роль ADMIN
        Role adminRole = roleRepository.findByName(RoleEnum.ROLE_ADMIN)
                .orElseGet(() -> {
                    Role newRole = new Role(RoleEnum.ROLE_ADMIN);
                    return roleRepository.save(newRole);
                });

        Set<Role> roles = new HashSet<>();
        roles.add(adminRole);
        user.setRoles(roles);

        return userRepository.save(user);
    }
}