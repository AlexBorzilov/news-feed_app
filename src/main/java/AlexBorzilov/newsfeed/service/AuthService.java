package AlexBorzilov.newsfeed.service;

import AlexBorzilov.newsfeed.dto.AuthDto;
import AlexBorzilov.newsfeed.dto.LoginUserDto;
import AlexBorzilov.newsfeed.dto.RegisterUserDto;
import AlexBorzilov.newsfeed.entity.UserEntity;
import AlexBorzilov.newsfeed.error.ErrorCodes;
import AlexBorzilov.newsfeed.error.NewsFeedException;
import AlexBorzilov.newsfeed.mappers.UserMapper;
import AlexBorzilov.newsfeed.mappers.UserMapperImpl;
import AlexBorzilov.newsfeed.repository.UserRepo;
import AlexBorzilov.newsfeed.response.CustomSuccessResponse;
import AlexBorzilov.newsfeed.security.SecurityUtilities;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.beans.Encoder;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final SecurityUtilities securityUtilities;
    private final UserRepo userRepo;
    private final static UserMapper userMapper = new UserMapperImpl();

    public CustomSuccessResponse<LoginUserDto> registrationRequest(RegisterUserDto registrationRequest) {
        if (userRepo.findByEmail(registrationRequest.getEmail()) != null) {
            throw new NewsFeedException(ErrorCodes
                    .USER_ALREADY_EXISTS
                    .getErrorMessage());
        }
        registrationRequest.setPassword(securityUtilities
                .getEncoder()
                .encode(registrationRequest.getPassword()));
        UserEntity user = userMapper.registerUserDtoToUserEntity(registrationRequest);
        user = userRepo.save(user);
        LoginUserDto userDto = userMapper.userEntityToLoginUserDto(user);
        userDto.setToken(securityUtilities.getToken(user.getId()));
        return new CustomSuccessResponse<>(userDto);
    }
    public CustomSuccessResponse<LoginUserDto> request(AuthDto authRequest){
        UserEntity user = Optional
                .ofNullable(userRepo.findByEmail(authRequest.getEmail()))
                .orElseThrow(() -> new NewsFeedException(ErrorCodes.USER_NOT_FOUND.getErrorMessage()));

        if (!securityUtilities
                .getEncoder()
                .matches(authRequest.getPassword(), user.getPassword())) {
            throw new NewsFeedException(ErrorCodes.PASSWORD_NOT_VALID.getErrorMessage());
        }
        else {
            LoginUserDto userDto = userMapper.userEntityToLoginUserDto(user);
            userDto.setToken(securityUtilities.getToken(user.getId()));
            return new CustomSuccessResponse<>(userDto);
        }
    }
}
