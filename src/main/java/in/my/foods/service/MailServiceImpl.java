package in.my.foods.service;

import in.my.foods.io.ContactRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService{

    @Autowired
    private JavaMailSender mailSender;
    @Override
    public void sendContactMail(ContactRequest request) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("prajwalnarute100@gmail.com");
        message.setSubject("Contact Form: " + request.getSubject());
        message.setReplyTo(request.getEmail());
        message.setText("From: " + request.getFirstName() + " " + request.getLastName() +
                "\nEmail: " + request.getEmail() +
                "\n\nMessage:\n" + request.getMessage());
        mailSender.send(message);
    }
}
