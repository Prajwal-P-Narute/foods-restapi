package in.my.foods.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.my.foods.io.FoodRequest;
import in.my.foods.io.FoodResponse;
import in.my.foods.service.FoodService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/foods")
public class FoodController {
    private final FoodService foodService;
    @PostMapping
    public FoodResponse addFood(@RequestPart("food") String foodString,
                                @RequestPart("file") MultipartFile file){
        System.out.println("Food String: " + foodString);
        System.out.println("File Name: " + file.getOriginalFilename());
        ObjectMapper objectMapper = new ObjectMapper();
        FoodRequest request = null;

        try {
            request = objectMapper.readValue(foodString, FoodRequest.class);
        }catch (JsonProcessingException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid JSON format");

        }

        FoodResponse resonse = foodService.addFood(request, file);
        return resonse;
    }

    @GetMapping
    public List<FoodResponse> readFoods(){
       return foodService.readFoods();
    }

    @GetMapping("/{id}")
    public FoodResponse readFood(@PathVariable String id){
       return foodService.readFood(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFood(@PathVariable String id){
       foodService.deleteFood(id);
    }

}

