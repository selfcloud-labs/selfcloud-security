INSERT INTO security.authorities (name, description) VALUES ('READ_ORDER', 'Permission to read orders');
INSERT INTO security.authorities (name, description) VALUES ('CREATE_ORDER', 'Permission to create orders');
INSERT INTO security.authorities (name, description) VALUES ('UPDATE_ORDER', 'Permission to update orders');
INSERT INTO security.authorities (name, description) VALUES ('DELETE_ORDER', 'Permission to delete orders');
INSERT INTO security.authorities (name, description) VALUES ('CREATE_USER', 'Permission to create users');
INSERT INTO security.authorities (name, description) VALUES ('DELETE_USER', 'Permission to delete users');
INSERT INTO security.authorities (name, description) VALUES ('GRAND_AUTHORITY', 'Permission to grant authorities');
INSERT INTO security.authorities (name, description) VALUES ('REVOKE_AUTHORITY', 'Permission to revoke authorities');
INSERT INTO security.authorities (name, description) VALUES ('READ_ANNOUNCEMENT', 'Permission to read orders');
INSERT INTO security.authorities (name, description) VALUES ('CREATE_ANNOUNCEMENT', 'Permission to create orders');
INSERT INTO security.authorities (name, description) VALUES ('UPDATE_ANNOUNCEMENT', 'Permission to update orders');
INSERT INTO security.authorities (name, description) VALUES ('DELETE_ANNOUNCEMENT', 'Permission to delete orders');


INSERT INTO security.roles (name, description) VALUES ('ROLE_USER', 'Standard user role with basic permissions');
INSERT INTO security.roles (name, description) VALUES ('ROLE_MODERATOR', 'Moderator role with extended permissions');
INSERT INTO security.roles (name, description) VALUES ('ROLE_ADMIN', 'Administrator role with full permissions');

INSERT INTO security.roles_authorities (role_id, authority_id) VALUES (1, 1);
INSERT INTO security.roles_authorities (role_id, authority_id) VALUES (2, 1);
INSERT INTO security.roles_authorities (role_id, authority_id) VALUES (2, 2);
INSERT INTO security.roles_authorities (role_id, authority_id) VALUES (3, 1);
INSERT INTO security.roles_authorities (role_id, authority_id) VALUES (3, 2);
INSERT INTO security.roles_authorities (role_id, authority_id) VALUES (3, 3);
INSERT INTO security.roles_authorities (role_id, authority_id) VALUES (3, 4);
INSERT INTO security.roles_authorities (role_id, authority_id) VALUES (3, 5);
INSERT INTO security.roles_authorities (role_id, authority_id) VALUES (3, 6);
INSERT INTO security.roles_authorities (role_id, authority_id) VALUES (3, 7);
INSERT INTO security.roles_authorities (role_id, authority_id) VALUES (3, 8);
-- ROLE_USER should have READ_ANNOUNCEMENT permission
INSERT INTO security.roles_authorities (role_id, authority_id) VALUES (1, 9);

-- ROLE_MODERATOR should have READ_ANNOUNCEMENT and CREATE_ANNOUNCEMENT permissions
INSERT INTO security.roles_authorities (role_id, authority_id) VALUES (2, 9);
INSERT INTO security.roles_authorities (role_id, authority_id) VALUES (2, 10);

-- ROLE_ADMIN should have all new permissions
INSERT INTO security.roles_authorities (role_id, authority_id) VALUES (3, 9);
INSERT INTO security.roles_authorities (role_id, authority_id) VALUES (3, 10);
INSERT INTO security.roles_authorities (role_id, authority_id) VALUES (3, 11);
INSERT INTO security.roles_authorities (role_id, authority_id) VALUES (3, 12);
