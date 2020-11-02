package ir.jadeh.api.controllers;

import ir.jadeh.api.controllers.infrastructure.Message;
import ir.jadeh.api.controllers.infrastructure.SessionIdMessage;
import ir.jadeh.api.models.UserEntity;
import ir.jadeh.api.repositories.UserRepository;
import ir.jadeh.api.services.UserAuthentication;
import ir.jadeh.api.services.Redis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/User")
public class UserController {
    final int verificationCodeSize = 5;
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Operation(summary = "Login Or Register New User By Mobile.", description = "", tags = {"Registration"})
    @GetMapping(
            value = "/LoginOrRegister",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Message LoginOrRegister(@RequestParam String mobile) {
        Message message = new Message();

        if (mobile == null || mobile.isEmpty()) {
            return message.setFailedMessage("Mobile Is Empty");
        }

        UserEntity userEntity;

        int minVerificationCode = (int) Math.pow(10, verificationCodeSize - 1);
        int maxVerificationCode = (minVerificationCode * 10) - 1;
        String newVerificationCode = Integer.toString((int) ((Math.random() * (maxVerificationCode - minVerificationCode)) + minVerificationCode));

        boolean registered = userRepository.existsByMobile(mobile);

        if (!registered) {
            message.setSuccessfulMessage("New User Inserted");
        } else {
            message.setSuccessfulMessage("New Verification Generated");
        }

        new Redis().setAndShutdown(mobile, newVerificationCode);

        return message;
    }

    @Operation(summary = "", description = "", tags = {"Registration"})
    @GetMapping(
            value = "/GetAllUsers",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<UserEntity>> GetAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @Operation(summary = "Verify Mobile By Verification Code", description = "", tags = {"Registration"})
    @GetMapping(
            value = "/VerifyPhoneNumber",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Message VerifyPhoneNumber(HttpSession session, @RequestParam String mobile, @RequestParam String verificationCode) {
        SessionIdMessage message = new SessionIdMessage();

        if (mobile == null || mobile.isEmpty()) {
            return message.setFailedMessage("Mobile Is Empty");
        }
        if (verificationCode == null || verificationCode.isEmpty()) {
            return message.setFailedMessage("VerificationCode Is Empty");
        }

        String savedVerificationCode = new Redis().getAndShutdown(mobile);
        if (savedVerificationCode == null || !savedVerificationCode.equals(verificationCode))
            message.setFailedMessage("Verification Code Or Mobile Is Not Valid");
        else {
            UserEntity userEntity = userRepository.findByMobile(mobile);
            if (userEntity == null) {
                userEntity = new UserEntity(mobile);
            }
            userEntity.setVerified(true);
            userEntity.setLastLoginOn(LocalDateTime.now());
            userRepository.save(userEntity);

            String token = new UserAuthentication().login(mobile);

            message.setToken(token);
            message.setSuccessfulMessage("Valid VerificationCode");
        }
        return message;
    }
}