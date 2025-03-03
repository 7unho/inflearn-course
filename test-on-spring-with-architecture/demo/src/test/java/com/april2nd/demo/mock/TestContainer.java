package com.april2nd.demo.mock;

import com.april2nd.demo.common.service.port.ClockHolder;
import com.april2nd.demo.common.service.port.UuidHolder;
import com.april2nd.demo.post.controller.port.PostService;
import com.april2nd.demo.post.service.PostServiceImpl;
import com.april2nd.demo.post.service.port.PostRepository;
import com.april2nd.demo.user.controller.UserController;
import com.april2nd.demo.user.controller.UserCreateController;
import com.april2nd.demo.user.controller.port.*;
import com.april2nd.demo.user.service.CertificationService;
import com.april2nd.demo.user.service.UserServiceImpl;
import com.april2nd.demo.user.service.port.MailSender;
import com.april2nd.demo.user.service.port.UserRepository;
import lombok.Builder;

public class TestContainer {
    public final MailSender mailSender;
    public final UserRepository userRepository;
    public final PostRepository postRepository;
    public final UserCreateService userCreateService;
    public final UserUpdateService userUpdateService;
    public final UserReadService userReadService;
    public final AuthenticationService authenticationService;
    public final PostService postService;
    public final CertificationService certificationService;
    public final UserController userController;
    public final UserCreateController userCreateController;

    @Builder
    public TestContainer(UuidHolder uuidHolder, ClockHolder clockHolder) {
        this.mailSender = new FakeMailSender();
        this.userRepository = new FakeUserRepository();
        this.postRepository = new FakePostRepository();
        this.postService = PostServiceImpl.builder()
                .clockHolder(clockHolder)
                .postRepository(this.postRepository)
                .userRepository(this.userRepository)
                .build();
        this.certificationService = new CertificationService(this.mailSender);

        UserServiceImpl userService = UserServiceImpl.builder()
                .uuidHolder(uuidHolder)
                .clockHolder(clockHolder)
                .userRepository(this.userRepository)
                .certificationService(this.certificationService)
                .build();

        this.userCreateService = userService;
        this.userUpdateService = userService;
        this.userReadService = userService;
        this.authenticationService = userService;
        this.userController = UserController.builder()
                .userReadService(this.userReadService)
                .userCreateService(this.userCreateService)
                .userUpdateService(this.userUpdateService)
                .authenticationService(this.authenticationService)
                .build();
        this.userCreateController = UserCreateController.builder()
                .userCreateService(this.userCreateService)
                .build();
    }
}
