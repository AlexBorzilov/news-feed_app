package aborzilov.newsfeed.controller;

import java.util.List;
import java.util.UUID;

import aborzilov.newsfeed.dto.PutUserDto;
import aborzilov.newsfeed.error.ValidationConstants;
import aborzilov.newsfeed.response.BaseSuccessResponse;
import aborzilov.newsfeed.response.CustomSuccessResponse;
import aborzilov.newsfeed.response.PutUserDtoResponse;
import aborzilov.newsfeed.service.UserService;
import aborzilov.newsfeed.view.PublicUserView;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<CustomSuccessResponse<List<PublicUserView>>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomSuccessResponse<PublicUserView>> getUserInfoById(
            @PathVariable("id")
            @NotBlank(message = ValidationConstants.MAX_UPLOAD_SIZE_EXCEEDED) UUID id) {
        return ResponseEntity.ok(userService.getUserInfoById(id));
    }

    @GetMapping("/info")
    public ResponseEntity<CustomSuccessResponse<PublicUserView>> getUserInfo() {
        return ResponseEntity.ok(userService.getUserInfo());
    }

    @PutMapping
    public ResponseEntity<CustomSuccessResponse<PutUserDtoResponse>> replaceUser(
            @RequestBody @Valid PutUserDto userNewData) {
        return ResponseEntity.ok(userService.replaceUser(userNewData));
    }

    @DeleteMapping
    public ResponseEntity<BaseSuccessResponse> deleteUser() {
        return ResponseEntity.ok(userService.deleteUser());
    }

}
