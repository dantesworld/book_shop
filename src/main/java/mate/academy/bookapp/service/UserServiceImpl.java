package mate.academy.bookapp.service;

import jakarta.transaction.Transactional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.academy.bookapp.dto.UserRegistrationRequestDto;
import mate.academy.bookapp.dto.UserResponseDto;
import mate.academy.bookapp.exceptions.RegistrationException;
import mate.academy.bookapp.mapper.UserMapper;
import mate.academy.bookapp.model.Role;
import mate.academy.bookapp.model.User;
import mate.academy.bookapp.repository.RoleRepository;
import mate.academy.bookapp.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RegistrationException("User with email " + requestDto.getEmail()
                    + " already exists");
        }
        User user = userMapper.toModel(requestDto);
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        user.setRoles(Set.of(roleRepository.findRoleByName(Role.RoleName.ROLE_USER)));
        return userMapper.toDto(userRepository.save(user));
    }
}
