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
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtUtility jwtUtility;
    private final PasswordEncoder passwordEncoder;
    private final UserRepo userRepo;

    public CustomSuccessResponse<LoginUserDto> registerUser(RegisterUserDto registrationRequest) {
        if (!userRepo.findByEmail(registrationRequest.getEmail()).isEmpty()) {
            throw new NewsFeedException(ErrorCodes.USER_ALREADY_EXISTS.getErrorMessage());
        }
        registrationRequest.setPassword(passwordEncoder
                .encode(registrationRequest.getPassword()));
        UserEntity user = UserMapper.INSTANCE.registerUserDtoToUserEntity(registrationRequest);
        user = userRepo.save(user);
        LoginUserDto userDto = UserMapper.INSTANCE.userEntityToLoginUserDto(user);
        userDto.setToken(jwtUtility.generateToken(user));
        return new CustomSuccessResponse<>(userDto);
    }

    public CustomSuccessResponse<LoginUserDto> login(AuthDto request) {
        UserEntity user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new NewsFeedException(ErrorCodes.USER_NOT_FOUND.getErrorMessage()));
        if (!passwordEncoder
                .matches(request.getPassword(), user.getPassword())) {
            throw new NewsFeedException(ErrorCodes.PASSWORD_NOT_VALID.getErrorMessage());
        }

        LoginUserDto userDto = UserMapper.INSTANCE.userEntityToLoginUserDto(user);
        userDto.setToken(jwtUtility.generateToken(user));
        return new CustomSuccessResponse<>(userDto);
    }

}
