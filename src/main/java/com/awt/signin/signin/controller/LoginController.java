package com.awt.signin.signin.controller;


import com.awt.signin.signin.entity.Registration;
import com.awt.signin.signin.entity.Login;
import com.awt.signin.signin.repository.RegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {


    @Autowired
    private final RegistrationRepository registrationRepository;

    @Autowired
    public LoginController(RegistrationRepository registrationRepository) {
        this.registrationRepository = registrationRepository;
    }


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Login login) {
        String email = login.getEmail();
        String password = login.getPassword();


        Registration registration = registrationRepository.findByUserEmailIgnoreCase(email);

        if (registration == null || !registration.getUserPassword().equals(password)) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        } else {

            return ResponseEntity.ok("Login successful");
        }
    }


}