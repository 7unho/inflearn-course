package com.april2nd.demo.user.controller.port;

import com.april2nd.demo.user.domain.User;
import com.april2nd.demo.user.domain.UserCreate;

public interface UserCreateService {
    User create(UserCreate userCreate);
}
