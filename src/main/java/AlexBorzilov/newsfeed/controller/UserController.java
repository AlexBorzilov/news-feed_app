package AlexBorzilov.newsfeed.controller;

import AlexBorzilov.newsfeed.dto.PutUserDto;
import AlexBorzilov.newsfeed.error.ValidationConstants;
import AlexBorzilov.newsfeed.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserInfoById(@PathVariable("id") @NotBlank(message = ValidationConstants.MAX_UPLOAD_SIZE_EXCEEDED)
                                                 UUID id){
        return ResponseEntity.ok(userService.getUserInfoById(id));
    }
    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo(){
        return ResponseEntity.ok(userService.getUserInfo());
    }

    @PutMapping
    public ResponseEntity<?> replaceUser(@RequestBody @Valid PutUserDto userNewData){
        return ResponseEntity.ok(userService.replaceUser(userNewData));
    }
}
