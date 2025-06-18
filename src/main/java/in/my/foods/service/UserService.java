package in.my.foods.service;

import in.my.foods.io.UserRequest;
import in.my.foods.io.UserResponse;

public interface UserService {

    UserResponse registerUser(UserRequest request);

    String findByUserId();
}
