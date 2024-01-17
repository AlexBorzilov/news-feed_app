package AlexBorzilov.newsfeed.controller;


import AlexBorzilov.newsfeed.dto.AuthDto;
import AlexBorzilov.newsfeed.dto.LoginUserDto;
import AlexBorzilov.newsfeed.dto.RegisterUserDto;
import AlexBorzilov.newsfeed.response.CustomSuccessResponse;
import AlexBorzilov.newsfeed.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<CustomSuccessResponse<LoginUserDto>> registerUser(@RequestBody @Valid RegisterUserDto registrationRequest) {
        return ResponseEntity.ok(authService.registerUser(registrationRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<CustomSuccessResponse<LoginUserDto>> login(@RequestBody @Valid AuthDto request) {
        return ResponseEntity.ok(authService.login(request));
    }

}
