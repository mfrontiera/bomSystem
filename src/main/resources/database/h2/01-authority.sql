--liquibase formatted sql

--changeset system:1
--changeset system:2

insert into users (id, username, name, password_signature, profile_picture) values (1, 'user','John Normal', '$2a$10$xdbKoM48VySZqVSU/cSlVeJn0Z04XCZ7KZBjUBC00eKo5uLswyOpe',
null);
insert into users (id, username, name, password_signature, profile_picture) values (2, 'admin','Emma Executive','$2a$10$jpLNVNeA7Ar/ZQ2DKbKCm.MuT2ESe.Qop96jipKMq7RaUgCoQedV.',
null);

