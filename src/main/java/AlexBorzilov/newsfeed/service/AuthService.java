package AlexBorzilov.newsfeed.service;

import AlexBorzilov.newsfeed.dto.LoginUserDto;
import AlexBorzilov.newsfeed.dto.RegisterUserDto;
import AlexBorzilov.newsfeed.entity.UserEntity;
import AlexBorzilov.newsfeed.mappers.UserMapper;
import AlexBorzilov.newsfeed.repository.UserRepo;
import AlexBorzilov.newsfeed.response.CustomSuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepo userRepo;
    public CustomSuccessResponse<LoginUserDto> registrationRequest(RegisterUserDto registerUserDto){
        UserEntity user = new UserEntity();
        LoginUserDto userDto = new LoginUserDto();
        user = UserMapper.INSTANCE.employeeDTOtoEmployee(registerUserDto);
        userRepo.save(user);
        userDto = UserMapper.INSTANCE.UserEntityToRegisterUserEntityDTO(user);

        return new CustomSuccessResponse<>(userDto);
    }
}
