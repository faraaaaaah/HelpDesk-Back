package com.youtube.crud.controller;

/*import com.youtube.crud.model.User;
import com.youtube.crud.repo.UserRepo;
import com.youtube.crud.service.interfaces.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.*;

import jakarta.mail.MessagingException;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class MailController {

    @Autowired
    private MailService mailService;

    @Autowired
    private UserRepo userRepo;

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        Optional<User> userOptional = userRepo.findByEmail(email);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email not found");
        }

        // Generate a reset token and save it with the user (omitted for simplicity)
        String resetToken = "generated_reset_token"; // Implement token generation logic

        String resetUrl = "http://localhost:4200/reset-password?token=" + resetToken;

        String emailText = "<p>To reset your password, click the link below:</p>" +
                "<p><a href=\"" + resetUrl + "\">Reset Password</a></p>";

        try {
            mailService.sendEmail(email, "Password Reset Request", emailText);
        } catch (MailException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending email");
        }

        return ResponseEntity.ok("Password reset email sent");
    }
}*/

import com.youtube.crud.service.interfaces.MailService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.youtube.crud.model.User;
import com.youtube.crud.repo.UserRepo;
import com.youtube.crud.service.interfaces.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class MailController {

    @Autowired
    private MailService mailService;

    @Autowired
    private UserRepo userRepo;
    private Map<String, String> resetTokenMap = new HashMap<>();

    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@RequestParam String email) {
        Optional<User> userOptional = userRepo.findByEmail(email);
        Map<String, String> response = new HashMap<>();

        if (userOptional.isEmpty()) {
            response.put("message", "Email not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // Generate a random token
        String resetToken = UUID.randomUUID().toString().substring(0, 6); // Example: 6-character token

        // Save the token with the user (for verification later), omitted for simplicity
        resetTokenMap.put(email, resetToken);
        System.out.println("Stored reset token: " + resetToken + " for email: " + email);
        System.out.println("Current resetTokenMap: " + resetTokenMap);
        // Prepare email content with the token
        String resetUrl = "http://localhost:4200/verify";

        String emailText = "<p>Your verification code is: " + resetToken + "</p>";

        try {
            mailService.sendEmail(email, "Password Reset Request", emailText);
        } catch (MailException e) {
            response.put("message", "Error sending email");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        response.put("message", "Password reset email sent with verification code");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify-reset-code")
    public ResponseEntity<Map<String, String>> verifyResetCode(@RequestBody Map<String, String> resetCodeRequest) {
        String email = resetCodeRequest.get("email");
        String resetCode = resetCodeRequest.get("resetCode");

        System.out.println("Received request to verify reset code");
        System.out.println("Email: " + email);
        System.out.println("Reset code: " + resetCode);

        String storedResetToken = resetTokenMap.get(email);
        if (storedResetToken == null || !storedResetToken.equals(resetCode)) {
            System.out.println("Invalid reset code for email " + email);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Invalid reset code");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        resetTokenMap.remove(email);
        System.out.println("Reset code verified successfully for email " + email);

        Map<String, String> successResponse = new HashMap<>();
        successResponse.put("message", "Reset code verified successfully");
        return ResponseEntity.ok(successResponse);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody Map<String, String> payload) {
        try {
            String email = payload.get("email");
            String newPassword = payload.get("newPassword");

            Optional<User> userOptional = userRepo.findByEmail(email);
            if (userOptional.isEmpty()) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Email not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            User user = userOptional.get();
            user.setPassword(newPassword); // Ensure to hash the password in a real application
            userRepo.save(user);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Password reset successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Error resetting password: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @PostMapping("/compare-password")
    public ResponseEntity<String> comparePassword(@RequestBody Map<String, String> payload) {
        try {
            String email = payload.get("email");
            String newPassword = payload.get("newPassword");

            Optional<User> userOptional = userRepo.findByEmail(email);
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email not found");
            }

            User user = userOptional.get();
            String currentPassword = user.getPassword();

            // Here, you would hash the new password and compare it with the hashed current password
            // Assuming passwords are not hashed for this example
            if (currentPassword.equals(newPassword)) {
                return ResponseEntity.ok("Password matches");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Passwords do not match");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error comparing password: " + e.getMessage());
        }
    }
    @PostMapping("/get-password")
    public ResponseEntity<Map<String, String>> getPasswordByEmail(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        Optional<User> userOptional = userRepo.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Map<String, String> response = new HashMap<>();
            response.put("password", user.getPassword()); // Return the password as JSON
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}




