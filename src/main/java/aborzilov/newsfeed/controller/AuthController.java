package aborzilov.newsfeed.controller;


import aborzilov.newsfeed.dto.AuthDto;
import aborzilov.newsfeed.dto.LoginUserDto;
import aborzilov.newsfeed.dto.RegisterUserDto;
import aborzilov.newsfeed.response.CustomSuccessResponse;
import aborzilov.newsfeed.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

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
    public ResponseEntity<CustomSuccessResponse<LoginUserDto>> registerUser(
            @RequestBody @Valid RegisterUserDto registrationRequest) {
        return ResponseEntity.ok(authService.registerUser(registrationRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<CustomSuccessResponse<LoginUserDto>> login(
            @RequestBody @Valid AuthDto request) {
        return ResponseEntity.ok(authService.login(request));
    }

}
