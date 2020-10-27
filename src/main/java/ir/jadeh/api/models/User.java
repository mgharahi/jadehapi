package ir.jadeh.api.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String mobile;
    private String verificationCode;
    private LocalDateTime verificationGeneratedOn;
    private LocalDateTime lastLoginOn;
    private boolean verified;

    public User() {

    }

    public User(String mobile, String verificationCode) {
        setMobile(mobile);
        setVerificationCode(verificationCode);
        setVerified(false);
    }

    public Long getId() {
        return id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
        if (!verificationCode.isEmpty()) {
            this.setVerificationGeneratedOn(LocalDateTime.now());
        }
    }

    public LocalDateTime getVerificationGeneratedOn() {
        return verificationGeneratedOn;
    }

    public void setVerificationGeneratedOn(LocalDateTime verificationGeneratedOn) {
        this.verificationGeneratedOn = verificationGeneratedOn;
    }

    public LocalDateTime getLastLoginOn() {
        return lastLoginOn;
    }

    public void setLastLoginOn(LocalDateTime lastLoginOn) {
        this.lastLoginOn = lastLoginOn;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}