package aborzilov.newsfeed;

import java.util.Optional;
import java.util.UUID;

import aborzilov.newsfeed.service.AuthService;
import aborzilov.newsfeed.dto.AuthDto;
import aborzilov.newsfeed.dto.LoginUserDto;
import aborzilov.newsfeed.dto.RegisterUserDto;
import aborzilov.newsfeed.entity.UserEntity;
import aborzilov.newsfeed.error.ErrorCodes;
import aborzilov.newsfeed.error.NewsFeedException;
import aborzilov.newsfeed.mappers.UserMapper;
import aborzilov.newsfeed.repository.UserRepo;
import aborzilov.newsfeed.response.CustomSuccessResponse;
import aborzilov.newsfeed.security.JwtUtility;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepo userRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtility jwtUtility;

    SoftAssertions softAssertions = new SoftAssertions();
    private final String EMAIL_TEST = "testing@test.ru";
    private final String PASSWORD_TEST = "TEST_PASSWORD1111";
    private final String TOKEN = "token";
    private final AuthDto authDto = new AuthDto();
    private RegisterUserDto registrationRequest;
    private UserEntity user;
    private String id;

    @BeforeEach
    void setUp() {
        registrationRequest = new RegisterUserDto();
        registrationRequest.setAvatar("test.jpg");
        registrationRequest.setEmail(EMAIL_TEST);
        registrationRequest.setName("test");
        registrationRequest.setPassword(PASSWORD_TEST);
        registrationRequest.setRole("user");
        authDto.setPassword(PASSWORD_TEST);
        authDto.setEmail(EMAIL_TEST);
        user = UserMapper.INSTANCE.registerUserDtoToUserEntity(registrationRequest);
        user.setPassword(new BCryptPasswordEncoder().encode(registrationRequest.getPassword()));
        user.setId(UUID.randomUUID());
        id = user
                .getId()
                .toString();
    }

    @Test
    void incorrectRegisterUserTest() {
        when(userRepo.findByEmail(registrationRequest.getEmail()))
                .thenReturn(Optional.ofNullable(user));
        NewsFeedException e = Assertions.assertThrows(NewsFeedException.class,
                () -> authService.registerUser(registrationRequest));
        softAssertions.assertThat(e.getMessage())
                .isEqualTo(ErrorCodes.USER_ALREADY_EXISTS.getErrorMessage());
        softAssertions.assertAll();
    }

    @Test
    void loginUserTest() {
        when(userRepo.findByEmail(authDto.getEmail()))
                .thenReturn(Optional.ofNullable(user));
        when(passwordEncoder.matches(anyString(), anyString()))
                .thenReturn(true);
        CustomSuccessResponse<LoginUserDto> response = authService.login(authDto);
        softAssertions.assertThat(response.getData()).isNotNull();
        softAssertions.assertThat(response.getData().getEmail()).isEqualTo(authDto.getEmail());
        softAssertions.assertAll();
    }

    @Test
    void incorrectLoginUserTest() {
        when(userRepo.findByEmail(anyString()))
                .thenReturn(Optional.ofNullable(user));
        NewsFeedException e = Assertions.assertThrows(NewsFeedException.class,
                () -> authService.login(authDto));
        softAssertions.assertThat(e.getMessage())
                .isEqualTo(ErrorCodes.PASSWORD_NOT_VALID.getErrorMessage());
        softAssertions.assertAll();
    }
}
