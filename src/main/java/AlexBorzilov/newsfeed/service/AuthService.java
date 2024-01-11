package AlexBorzilov.newsfeed.service;

import AlexBorzilov.newsfeed.dto.AuthDto;
import AlexBorzilov.newsfeed.dto.LoginUserDto;
import AlexBorzilov.newsfeed.dto.RegisterUserDto;
import AlexBorzilov.newsfeed.entity.UserEntity;
import AlexBorzilov.newsfeed.error.ErrorCodes;
import AlexBorzilov.newsfeed.error.NewsFeedException;

import AlexBorzilov.newsfeed.mappers.UserMapper;
import AlexBorzilov.newsfeed.repository.UserRepo;
import AlexBorzilov.newsfeed.response.CustomSuccessResponse;
import AlexBorzilov.newsfeed.security.JwtUtility;
import AlexBorzilov.newsfeed.security.SecurityConfig;
import lombok.RequiredArgsConstructor;


import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtUtility jwtUtility;
    private final SecurityConfig securityConfig;
    private final UserRepo userRepo;

    public CustomSuccessResponse<LoginUserDto> registerUser(RegisterUserDto registrationRequest) {
        if (userRepo.findByEmail(registrationRequest.getEmail()) != null) {
            throw new NewsFeedException(ErrorCodes.USER_ALREADY_EXISTS.getErrorMessage());
        }
        registrationRequest.setPassword(securityConfig
                .getEncoder()
                .encode(registrationRequest.getPassword()));
        UserEntity user = UserMapper.INSTANCE.registerUserDtoToUserEntity(registrationRequest);
        user = userRepo.save(user);
        LoginUserDto userDto = UserMapper.INSTANCE.userEntityToLoginUserDto(user);
        userDto.setToken(jwtUtility.getToken(user.getId()));
        return new CustomSuccessResponse<>(userDto);
    }

    public CustomSuccessResponse<LoginUserDto> login(AuthDto request) {
        UserEntity user = Optional
                .ofNullable(userRepo.findByEmail(request.getEmail()))
                .orElseThrow(() -> new NewsFeedException(ErrorCodes.USER_NOT_FOUND.getErrorMessage()));
        if (!securityConfig
                .getEncoder()
                .matches(request.getPassword(), user.getPassword())) {
            throw new NewsFeedException(ErrorCodes.PASSWORD_NOT_VALID.getErrorMessage());
        }

        LoginUserDto userDto = UserMapper.INSTANCE.userEntityToLoginUserDto(user);
        userDto.setToken(jwtUtility.getToken(user.getId()));
        return new CustomSuccessResponse<>(userDto);
    }
}
