package AlexBorzilov.newsfeed.service;

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
        System.out.print("энкодер ");
        registrationRequest.setPassword(securityUtilities
                .getEncoder()
                .encode(registrationRequest.getPassword()));
        System.out.println("отработал: " + registrationRequest.getPassword());
        UserEntity user = userMapper.registerUserDtoToUserEntity(registrationRequest);
        user = userRepo.save(user);
        LoginUserDto userDto = userMapper.userEntityToLoginUserDto(user);
        System.out.print("токен ");
        userDto.setToken(securityUtilities.getToken(user.getId()));
        System.out.println("готов: " + userDto.getToken());
        return new CustomSuccessResponse<>(userDto);
    }
}
