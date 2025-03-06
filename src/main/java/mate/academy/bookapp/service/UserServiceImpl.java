package mate.academy.bookapp.service;

import lombok.RequiredArgsConstructor;
import mate.academy.bookapp.dto.UserRegistrationRequestDto;
import mate.academy.bookapp.dto.UserResponseDto;
import mate.academy.bookapp.exceptions.RegistrationException;
import mate.academy.bookapp.mapper.UserMapper;
import mate.academy.bookapp.model.User;
import mate.academy.bookapp.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RegistrationException("User with email " + requestDto.getEmail()
                    + " already exists");
        }
        User user = userMapper.toModel(requestDto);
        return userMapper.toDto(userRepository.save(user));
    }
}
