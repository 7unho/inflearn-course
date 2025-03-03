package com.april2nd.demo.user.controller.port;

import com.april2nd.demo.user.domain.User;
import com.april2nd.demo.user.domain.UserUpdate;

public interface UserUpdateService {
    User update(long id, UserUpdate userUpdate);
}
