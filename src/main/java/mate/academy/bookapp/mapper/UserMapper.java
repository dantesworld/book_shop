package mate.academy.bookapp.mapper;

import mate.academy.bookapp.config.MapperConfig;
import mate.academy.bookapp.dto.user.UserRegistrationRequestDto;
import mate.academy.bookapp.dto.user.UserResponseDto;
import mate.academy.bookapp.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponseDto toDto(User user);

    User toModel(UserRegistrationRequestDto requestDto);
}
