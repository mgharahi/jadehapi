package ir.jadeh.api.apis;

import com.fasterxml.jackson.core.JsonProcessingException;
import ir.jadeh.api.apis.infrastructure.Message;
import ir.jadeh.api.models.User;
import ir.jadeh.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/User")
public class UserApi {
    final int verificationCodeSize = 5;
    @Autowired
    private UserRepository repository;
    private final Message message;

    public UserApi() {
        message = new Message();
    }

    @RequestMapping(method = RequestMethod.GET,
            value = "/LoginOrRegister",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String LoginOrRegister(String mobile) throws JsonProcessingException {

        if (mobile == null || mobile.isEmpty()) {
            return message.SetFailedMessage("Mobile Is Empty").ToJson();
        }

        User user;

        int minVerificationCode = (int) Math.pow(10, verificationCodeSize - 1);
        int maxVerificationCode = (minVerificationCode * 10) - 1;
        String newVerificationCode = Integer.toString((int) ((Math.random() * (maxVerificationCode - minVerificationCode)) + minVerificationCode));


        boolean registered = repository.existsByMobile(mobile);

        if (!registered) {
            user = new User(mobile, newVerificationCode);
            repository.save(user);
            message.SetSuccessfulMessage("New User Inserted");
        } else {
            user = repository.findByMobile(mobile);
            user.setVerificationCode(newVerificationCode);
            repository.save(user);
            message.SetSuccessfulMessage("New Verification Generated");
        }

        return message.ToJson();
    }

    @RequestMapping(method = RequestMethod.GET,
            value = "/VerifyPhoneNumber",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String VerifyPhoneNumber(String mobile, String verificationCode) throws JsonProcessingException {
        if (mobile == null || mobile.isEmpty()) {
            return message.SetFailedMessage("Mobile Is Empty").ToJson();
        }
        if (verificationCode == null || verificationCode.isEmpty()) {
            return message.SetFailedMessage("VerificationCode Is Empty").ToJson();
        }

        User user = repository.findByMobileAndVerificationCode(mobile, verificationCode);
        if (user == null)
            message.SetFailedMessage("Verification Code Or Mobile Is Not Valid");
        else {
            user.setVerified(true);
            user.setLastLoginOn(LocalDateTime.now());
            repository.save(user);
            message.SetSuccessfulMessage("Valid VerificationCode");
        }
        return message.ToJson();
    }
}