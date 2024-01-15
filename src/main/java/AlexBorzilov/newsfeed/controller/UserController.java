package AlexBorzilov.newsfeed.controller;


import AlexBorzilov.newsfeed.service.UserService;
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
}
