package com.april2nd.demo.user.controller.port;

import com.april2nd.demo.user.domain.User;
import com.april2nd.demo.user.domain.UserCreate;
import com.april2nd.demo.user.domain.UserUpdate;

public interface UserService {
    User getByEmail(String email);

    User getById(long id);

    User create(UserCreate userCreate);

    User update(long id, UserUpdate userUpdate);

    void login(long id);

    void verifyEmail(long id, String certificationCode);
}
