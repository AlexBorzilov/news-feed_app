package AlexBorzilov.newsfeed.controller;


import AlexBorzilov.newsfeed.dto.PutUserDto;
import AlexBorzilov.newsfeed.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> all() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping
    public ResponseEntity<?> replaceUser(@RequestBody @Valid PutUserDto userNewData){
        return ResponseEntity.ok(userService.replaceUser(userNewData));
    }
}
