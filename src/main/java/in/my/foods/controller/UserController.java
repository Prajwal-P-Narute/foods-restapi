package in.my.foods.controller;

import in.my.foods.io.UserRequest;
import in.my.foods.io.UserResponse;
import in.my.foods.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@RequestBody UserRequest userRequest){
            return userService.registerUser(userRequest);
    }
}
