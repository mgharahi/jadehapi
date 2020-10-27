package ir.jadeh.api.repositories;

import ir.jadeh.api.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<ir.jadeh.api.models.User, Long> {

    User findByMobile(String mobile);

    boolean existsByMobile(String mobile);

    User findByMobileAndVerificationCode(String mobile, String verificationCode);
}