CREATE TABLE users (
    id UUID NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    reset_password_token VARCHAR(255),
    reset_password_token_sent_at TIMESTAMP WITHOUT TIME ZONE,
    email_confirmation_token VARCHAR(255),
    email_confirmation_token_sent_at TIMESTAMP WITHOUT TIME ZONE,
    email_confirmed BOOLEAN,
    country_code VARCHAR(10),
    phone VARCHAR(50),
    phone_confirmation_token VARCHAR(255),
    phone_confirmation_token_sent_at TIMESTAMP WITHOUT TIME ZONE,
    phone_confirmed BOOLEAN,
    profile_image_url VARCHAR(255),
    is_active BOOLEAN,
    is_locked BOOLEAN,
    date_created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_updated TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT users_pkey PRIMARY KEY (id)
);

CREATE TABLE roles (
    id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    date_created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_updated TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT roles_pkey PRIMARY KEY (id)
);

CREATE TABLE privileges (
    id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    date_created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_updated TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT privileges_pkey PRIMARY KEY (id)
);

CREATE TABLE login_histories (
    id UUID NOT NULL,
    user_agent VARCHAR(255),
    last_login_at TIMESTAMP WITHOUT TIME ZONE,
    last_login_ip VARCHAR(255),
    user_id UUID,
    date_created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_updated TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT login_histories_pkey PRIMARY KEY (id)
);

CREATE TABLE congregations (
    id UUID NOT NULL,
    number INTEGER NOT NULL,
    name VARCHAR(255) NOT NULL,
    country VARCHAR(50),
    state VARCHAR(255),
    city VARCHAR(255),
    address VARCHAR(255),
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    date_created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_updated TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT congregations_pkey PRIMARY KEY (id)
);

CREATE TABLE user_roles (
    user_id UUID NOT NULL,
    role_id UUID NOT NULL
);

CREATE TABLE role_privileges (
    role_id UUID NOT NULL,
    privilege_id UUID NOT NULL
);

CREATE TABLE user_congregations (
    user_id UUID NOT NULL,
    congregation_id UUID NOT NULL
);

ALTER TABLE users ADD CONSTRAINT unique_users_email UNIQUE (email);

ALTER TABLE roles ADD CONSTRAINT unique_roles_name UNIQUE (name);

ALTER TABLE privileges ADD CONSTRAINT unique_privileges_name UNIQUE (name);

ALTER TABLE login_histories ADD CONSTRAINT fk_login_histories_user_id FOREIGN KEY (user_id) REFERENCES users (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE user_roles ADD CONSTRAINT pk_user_roles PRIMARY KEY (user_id, role_id);

ALTER TABLE user_roles ADD CONSTRAINT fk_user_roles_user_id FOREIGN KEY (user_id) REFERENCES users (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE user_roles ADD CONSTRAINT fk_user_roles_role_id FOREIGN KEY (role_id) REFERENCES roles (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE role_privileges ADD CONSTRAINT pk_role_privileges PRIMARY KEY (role_id, privilege_id);

ALTER TABLE role_privileges ADD CONSTRAINT fk_role_privileges_role_id FOREIGN KEY (role_id) REFERENCES roles (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE role_privileges ADD CONSTRAINT fk_role_privileges_privilege_id FOREIGN KEY (privilege_id) REFERENCES privileges (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE user_congregations ADD CONSTRAINT pk_user_congregations PRIMARY KEY (user_id, congregation_id);

ALTER TABLE user_congregations ADD CONSTRAINT fk_user_congregations_user_id FOREIGN KEY (user_id) REFERENCES users (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE user_congregations ADD CONSTRAINT fk_user_congregations_congregation_id FOREIGN KEY (congregation_id) REFERENCES congregations (id) ON UPDATE NO ACTION ON DELETE NO ACTION;
