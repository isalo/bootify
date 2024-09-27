INSERT INTO users (
    id,
    email,
    password,
    first_name,
    last_name,
    reset_password_token,
    reset_password_token_sent_at,
    email_confirmation_token,
    email_confirmation_token_sent_at,
    email_confirmed,
    country_code,
    phone,
    phone_confirmation_token,
    phone_confirmation_token_sent_at,
    phone_confirmed,
    profile_image_url,
    is_active,
    is_locked,
    date_created,
    last_updated
) VALUES (
    'a9b7ba70-783b-317e-9998-dc4dd82eb3c5',
    'roleAdmin',
    '{bcrypt}$2a$10$FMzmOkkfbApEWxS.4XzCKOR7EbbiwzkPEyGgYh6uQiPxurkpzRMa6',
    'Nulla facilisis.',
    'Sed diam voluptua.',
    'Consetetur sadipscing.',
    '2024-09-02 14:30:00',
    'Nam liber tempor.',
    '2024-09-02 14:30:00',
    TRUE,
    'Lorem ipsu',
    'Viverra suspendisse.',
    'Sed diam nonumy.',
    '2024-09-02 14:30:00',
    TRUE,
    'Nulla facilisis.',
    TRUE,
    TRUE,
    '2024-09-02 14:30:00',
    '2024-09-02 14:30:00'
);

INSERT INTO user_roles (
    user_id,
    role_id
) VALUES (
    'a9b7ba70-783b-317e-9998-dc4dd82eb3c5',
    'a96e0a04-d20f-3096-bc64-dac2d639a577'
);

INSERT INTO users (
    id,
    email,
    password,
    first_name,
    last_name,
    reset_password_token,
    reset_password_token_sent_at,
    email_confirmation_token,
    email_confirmation_token_sent_at,
    email_confirmed,
    country_code,
    phone,
    phone_confirmation_token,
    phone_confirmation_token_sent_at,
    phone_confirmed,
    profile_image_url,
    is_active,
    is_locked,
    date_created,
    last_updated
) VALUES (
    'b8c37e33-defd-351c-b91e-1e03e51657da',
    'roleUser',
    '{bcrypt}$2a$10$FMzmOkkfbApEWxS.4XzCKOR7EbbiwzkPEyGgYh6uQiPxurkpzRMa6',
    'Et ea rebum.',
    'At vero eos.',
    'Sed diam nonumy.',
    '2024-09-03 14:30:00',
    'Consetetur sadipscing.',
    '2024-09-03 14:30:00',
    FALSE,
    'Duis autem',
    'Nec ullamcorper.',
    'Sed diam voluptua.',
    '2024-09-03 14:30:00',
    FALSE,
    'Et ea rebum.',
    FALSE,
    FALSE,
    '2024-09-03 14:30:00',
    '2024-09-03 14:30:00'
);

INSERT INTO user_roles (
    user_id,
    role_id
) VALUES (
    'b8c37e33-defd-351c-b91e-1e03e51657da',
    'b8bff625-bdb0-3939-92c9-d4db0c6bbe45'
);
