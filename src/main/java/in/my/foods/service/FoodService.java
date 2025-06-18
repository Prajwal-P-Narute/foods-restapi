package in.my.foods.service;

import in.my.foods.io.FoodRequest;
import in.my.foods.io.FoodResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FoodService {
    String uploadFile(MultipartFile file);

    FoodResponse addFood(FoodRequest request, MultipartFile file);

    List<FoodResponse> readFoods();

    FoodResponse readFood(String id);

   boolean deleteFile(String fileName);

   void deleteFood(String id);
}
