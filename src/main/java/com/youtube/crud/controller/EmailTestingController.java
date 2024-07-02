/*package com.youtube.crud.controller;


import com.youtube.crud.service.implementation.EmailServiceImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailTestingController {
    private final EmailServiceImpl emailService;
    public EmailTestingController(EmailServiceImpl emailService) {this.emailService = emailService;}
    @RequestMapping("/send-test-email")
    public String sendTestEmail() {
        emailService.sendEmail("faylaboubaker@gmail.com", "email testing ","this is test");
        return "email test sent successfully";
    }
}*/
