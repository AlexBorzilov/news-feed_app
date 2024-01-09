package AlexBorzilov.newsfeed.controller;

import AlexBorzilov.newsfeed.dto.AuthDto;
import AlexBorzilov.newsfeed.dto.LoginUserDto;
import AlexBorzilov.newsfeed.dto.RegisterUserDto;
import AlexBorzilov.newsfeed.response.CustomSuccessResponse;
import AlexBorzilov.newsfeed.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> registerUser(@RequestBody @Valid RegisterUserDto register){
        return ResponseEntity.ok(authService.registrationRequest(register));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthDto request){
        return ResponseEntity.ok(authService.request(request));
    }

}
