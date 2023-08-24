package com.awt.signin.signin.controller;


import com.awt.signin.signin.entity.Registration;
import com.awt.signin.signin.entity.ResetPasswordInputs;
import com.awt.signin.signin.repository.RegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class PasswordResetController {

    @Autowired
    RegistrationRepository registrationRepository;

    @PostMapping("/reset")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordInputs request) {

        Optional<Registration> optionalRequest = registrationRepository.findById(request.getId());

        if (optionalRequest.isPresent()) {
            Registration existingRequest = optionalRequest.get();

            if (!existingRequest.getUserPassword().equals(request.getOldPassword())) {
                return ResponseEntity.badRequest().body("Invalid old password");
            } else if (request.getNewPassword().equals(request.getOldPassword())) {
                return ResponseEntity.badRequest().body("Same old and new password");
            } else {
                existingRequest.setUserPassword(request.getNewPassword());
                registrationRepository.save(existingRequest);

                return ResponseEntity.ok("Password reset successful");
            }

        } else {
            return ResponseEntity.ok("Registration Not Found");
        }
    }
}