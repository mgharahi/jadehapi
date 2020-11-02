package ir.jadeh.api.repositories;

import ir.jadeh.api.models.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Long> {

    UserEntity findByMobile(String mobile);

    boolean existsByMobile(String mobile);

}