package AlexBorzilov.newsfeed.controller;

import java.util.List;
import java.util.UUID;

import AlexBorzilov.newsfeed.dto.PutUserDto;
import AlexBorzilov.newsfeed.error.ValidationConstants;
import AlexBorzilov.newsfeed.response.BaseSuccessResponse;
import AlexBorzilov.newsfeed.response.CustomSuccessResponse;
import AlexBorzilov.newsfeed.response.PutUserDtoResponse;
import AlexBorzilov.newsfeed.service.UserService;
import AlexBorzilov.newsfeed.view.PublicUserView;
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
