package AlexBorzilov.newsfeed.mappers;

import AlexBorzilov.newsfeed.dto.LoginUserDto;
import AlexBorzilov.newsfeed.dto.RegisterUserDto;
import AlexBorzilov.newsfeed.entity.UserEntity;
import AlexBorzilov.newsfeed.view.PublicUserView;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {
    UserEntity registerUserDtoToUserEntity(RegisterUserDto registrationRequest);

    LoginUserDto userEntityToLoginUserDto(UserEntity user);

    PublicUserView userEntityToPublicUserView(UserEntity user);
}
