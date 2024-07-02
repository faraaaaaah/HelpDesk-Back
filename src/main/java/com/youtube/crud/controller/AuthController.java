package com.youtube.crud.controller;


import com.youtube.crud.model.User;
import com.youtube.crud.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private UserRepo userRepo;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginForm loginForm) {
        try {
            Optional<User> userOptional = userRepo.findByEmail(loginForm.getEmail());

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                if (user.getPassword().equals(loginForm.getPassword())) {
                    // Generate token or session management
                    String token = "your_generated_token";
                    return ResponseEntity.ok().body(new AuthResponse(token));
                }
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        } catch (Exception e) {
            // Log the error
            System.err.println("Error during authentication: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }
    @GetMapping("/users/checkEmail/{email}")
    public ResponseEntity<?> checkEmailExists(@PathVariable String email) {
        try {
            Optional<User> userOptional = userRepo.findByEmail(email);
            if (userOptional.isPresent()) {
                return ResponseEntity.ok(true); // Email exists
            } else {
                return ResponseEntity.ok(false); // Email does not exist
            }
        } catch (Exception e) {
            // Log the error
            System.err.println("Error checking email: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error checking email");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> searchUser(@RequestBody RegisterForm registerForm) {
        try {
            System.out.println("Received registration request: " + registerForm);
            Optional<User> userOptional = userRepo.findByEmail(registerForm.getEmail());

            if (userOptional.isPresent()) {
                // Email already exists
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");
            } else {
                User newUser = new User();
                newUser.setEmail(registerForm.getEmail());
                newUser.setPassword(registerForm.getPassword());
                newUser.setName(registerForm.getName());
                newUser.setLastname(registerForm.getLastname());
                newUser.setDob(registerForm.getDob());
                userRepo.save(newUser);
                return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
            }
        } catch (Exception e) {
            // Log the error
            System.err.println("Error during registration: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

    // LoginForm class for receiving login request body
    static class LoginForm {
        private String email;
        private String password;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    // AuthResponse class for sending response
    static class AuthResponse {
        private String token;

        public AuthResponse(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
    static class RegisterForm {
        private String name;
        private String lastname;
        private String email;
        private String password;
        private Date dob;

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getPassword() {
                return password;
            }

            public void setPassword(String password) {
                this.password = password;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getLastname() {
                return lastname;
            }

            public void setLastname(String lastname) {
                this.lastname = lastname;
            }

            public Date getDob() {
                return dob;
            }

            public void setDob(Date dob) {
                this.dob = dob;
            }
        @Override
        public String toString() {
            return "RegisterForm{" +
                    "name='" + name + '\'' +
                    ", lastname='" + lastname + '\'' +
                    ", email='" + email + '\'' +
                    ", password='" + password + '\'' +
                    ", dob=" + dob +
                    '}';
        }
        }

}
