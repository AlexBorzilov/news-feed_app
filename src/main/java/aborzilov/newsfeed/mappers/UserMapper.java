package aborzilov.newsfeed.mappers;

import aborzilov.newsfeed.dto.LoginUserDto;
import aborzilov.newsfeed.dto.PutUserDto;
import aborzilov.newsfeed.dto.RegisterUserDto;
import aborzilov.newsfeed.entity.UserEntity;
import aborzilov.newsfeed.response.PutUserDtoResponse;
import aborzilov.newsfeed.view.PublicUserView;
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
