package com.april2nd.demo.user.service.port;

import com.april2nd.demo.user.domain.UserStatus;
import com.april2nd.demo.user.infrastructure.UserEntity;

import java.util.Optional;

public interface UserRepository {
    Optional<UserEntity> findByIdAndStatus(long id, UserStatus userStatus);

    Optional<UserEntity> findByEmailAndStatus(String email, UserStatus userStatus);

    UserEntity save(UserEntity userEntity);

    Optional<UserEntity> findById(long id);
}
