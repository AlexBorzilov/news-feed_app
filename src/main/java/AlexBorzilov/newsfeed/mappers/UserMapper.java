package AlexBorzilov.newsfeed.mappers;

import AlexBorzilov.newsfeed.dto.LoginUserDto;
import AlexBorzilov.newsfeed.dto.RegisterUserDto;
import AlexBorzilov.newsfeed.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.security.core.userdetails.User;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    LoginUserDto UserEntityToRegisterUserEntityDTO(UserEntity userEntity);

    UserEntity employeeDTOtoEmployee(RegisterUserDto userDto);
//    tring avatar, String email, UUID id, String role, String token
}
