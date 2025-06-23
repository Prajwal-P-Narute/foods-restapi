package in.my.foods.controller;

import in.my.foods.io.ContactRequest;
import in.my.foods.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/contact")
public class ContactController {

    @Autowired
    private MailService mailService;

    @PostMapping
    public String handleContactForm(@RequestBody ContactRequest request){
       mailService.sendContactMail(request);
       return "Message Send Successfully.";
    }
}
