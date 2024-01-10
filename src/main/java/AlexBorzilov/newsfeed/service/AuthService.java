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

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final SecurityUtilities securityUtilities;
    private final UserRepo userRepo;

    public CustomSuccessResponse<LoginUserDto> registerUser(RegisterUserDto registrationRequest) {
        if (userRepo.findByEmail(registrationRequest.getEmail()) != null) {
            throw new NewsFeedException(ErrorCodes.USER_ALREADY_EXISTS.getErrorMessage());
        }
        registrationRequest.setPassword(securityUtilities
                .getEncoder()
                .encode(registrationRequest.getPassword()));
        UserEntity user = UserMapper.INSTANCE.registerUserDtoToUserEntity(registrationRequest);
        user = userRepo.save(user);
        LoginUserDto userDto = UserMapper.INSTANCE.userEntityToLoginUserDto(user);
        userDto.setToken(securityUtilities.getToken(user.getId()));
        return new CustomSuccessResponse<>(userDto);
    }
}
