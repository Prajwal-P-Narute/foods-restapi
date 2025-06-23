package in.my.foods.io;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ContactRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String subject;
    private String message;
}
