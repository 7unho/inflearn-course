package com.april2nd.demo.user.controller.port;

import com.april2nd.demo.user.domain.User;

public interface UserReadService {
    User getByEmail(String email);

    User getById(long id);
}
