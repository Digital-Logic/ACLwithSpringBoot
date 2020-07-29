INSERT INTO user(email, encoded_password, account_enabled)
    VALUES('joe@dirt.com', '{bcrypt}\0442a\04410\0447R3KvnidSbUCatBBZ.gEEe4FtvZyUwQM6W7LyImwmPUQKB92x6J66', 1);

/*  Add user role to user 'joe@dirt.com' */
INSERT INTO user_roles(user_id, role_id)
    SELECT
          (SELECT id FROM user WHERE user.email = 'joe@dirt.com') as user_id,
          (SELECT id FROM role WHERE name = 'USER') as role_id;