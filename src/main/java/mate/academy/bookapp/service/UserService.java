package mate.academy.bookapp.service;

import mate.academy.bookapp.dto.user.UserRegistrationRequestDto;
import mate.academy.bookapp.dto.user.UserResponseDto;
import mate.academy.bookapp.exceptions.RegistrationException;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException;
}
