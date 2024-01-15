package AlexBorzilov.newsfeed.mappers;

import AlexBorzilov.newsfeed.dto.LoginUserDto;
import AlexBorzilov.newsfeed.dto.PutUserDto;
import AlexBorzilov.newsfeed.dto.RegisterUserDto;
import AlexBorzilov.newsfeed.entity.UserEntity;
import AlexBorzilov.newsfeed.response.PutUserDtoResponse;
import AlexBorzilov.newsfeed.view.PublicUserView;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserEntity registerUserDtoToUserEntity(RegisterUserDto registrationRequest);

    LoginUserDto userEntityToLoginUserDto(UserEntity user);

    PublicUserView userEntityToPublicUserView(UserEntity user);

    UserEntity putUserDtoToUserEntity(PutUserDto newDataUser);

    PutUserDtoResponse UserEntityToPutUserDtoResponse(UserEntity user);
}