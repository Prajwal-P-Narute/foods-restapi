package in.my.foods.service;

import in.my.foods.entity.FoodEntity;
import in.my.foods.io.FoodRequest;
import in.my.foods.io.FoodResponse;
import in.my.foods.repository.FoodRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FoodServiceImpl implements FoodService{
    @Autowired
    private S3Client s3Client;

    @Autowired
    private FoodRepository foodRepository;

    @Value("${aws.s3.bucketName}")
    private  String bucketName;

    @Override
    public String uploadFile(MultipartFile file) {
        String filenameExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);

        String key = UUID.randomUUID().toString()+"."+filenameExtension;
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .acl("public-read")
                    .contentType(file.getContentType())
                    .build();

            PutObjectResponse response = s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

            if(response.sdkHttpResponse().isSuccessful()){
                return "https://"+bucketName+".s3.amazonaws.com/"+key;
            }else{
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "file uploading failed");
            }

        }catch (IOException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occured while uploading the file");


        }

    }

    @Override
    public FoodResponse addFood(FoodRequest request, MultipartFile file) {

        System.out.println("request come to service layer");
FoodEntity newFoodEntity = convertEntity(request);
String imageUrl = uploadFile(file);
newFoodEntity.setImageUrl(imageUrl);
newFoodEntity = foodRepository.save(newFoodEntity);
        System.out.println("after saving data");
return convertToResponse(newFoodEntity);

    }

    @Override
    public List<FoodResponse> readFoods() {
        List<FoodEntity> databaseEntries = foodRepository.findAll();
        // here we have used java 8 feature
       return databaseEntries.stream().map(object -> convertToResponse(object)).collect(Collectors.toList());
    }

    @Override
    public FoodResponse readFood(String id) {
        FoodEntity existingFood = foodRepository.findById(id).orElseThrow( () ->new RuntimeException("Food not found for the id" +id));
        return convertToResponse(existingFood);
    }

    @Override
    public boolean deleteFile(String fileName) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        s3Client.deleteObject(deleteObjectRequest);
        return true;
    }

    @Override
    public void deleteFood(String id) {
        FoodResponse response = readFood(id);
        String imageUrl = response.getImageUrl();
        String fileName = imageUrl.substring(imageUrl.lastIndexOf("/")+1);
        boolean isFileDeleted = deleteFile(fileName);
        if(isFileDeleted){
            foodRepository.deleteById(response.getId());
        }
    }

    private FoodEntity convertEntity(FoodRequest request){
     return FoodEntity.builder()
             .name(request.getName())
             .description(request.getDescription())
             .category(request.getCategory())
             .price(request.getPrice())
             .build();
    }

    private FoodResponse convertToResponse(FoodEntity entity){
           return FoodResponse.builder()
                   .id(entity.getId())
                   .name(entity.getName())
                   .description(entity.getDescription())
                   .category(entity.getCategory())
                   .price(entity.getPrice())
                   .imageUrl(entity.getImageUrl())
                   .build();
    }
}

