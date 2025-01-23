insert into `users` (`id`, `email`, `nickname`, `address`, `certification_code`, `status`, `last_login_at`)
values (100, 'rlawnsgh8395@naver.com', 'april2nd', 'seoul', 'aaaaa-aaaaaaaaaa-aaaaa-aaaaa', 'ACTIVE', 0);

insert into `users` (`id`, `email`, `nickname`, `address`, `certification_code`, `status`, `last_login_at`)
values (200, 'user@pending.com', 'april2nd_pending', 'seoul', 'aaaaa-aaaaaaaaaa-aaaaa-aaaaa', 'PENDING', 0);

insert into `posts` (`id`, `content`, `created_at`, `modified_at`, `user_id`)
values (100, 'helloworld', 1678530673958, 0, 100);