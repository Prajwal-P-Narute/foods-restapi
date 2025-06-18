package in.my.foods.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {
    @GetMapping("/api/test")
    public String test() {
        return "Hello from backend!";
    }
}
