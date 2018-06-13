INSERT INTO user(id, username,password, name, email) VALUES (1, 'admin','$2a$10$a8Phy5I6EN.EGfMvmNfW7.aodvfSkrjR3ffUxzzqzEu2Q7Bh4SOR2','老魏','i@waylau.com');
INSERT INTO user(id, username,password, name, email) VALUES (2, 'waylau','$2a$10$hJ.DS1tDn/TaBc0HjYehweA2.UpMMpVKwF4wBQPZoxWgfDZPpif.K','way lau','waylau@waylau.com');

INSERT INTO authority(id, name) VALUES (1, 'ROLE_ADMIN');
INSERT INTO authority(id, name) VALUES (2, 'ROLE_USER');

INSERT INTO user_authority(user_id, authority_id) VALUES (1, 1);
INSERT INTO user_authority(user_id, authority_id) VALUES (2, 2);