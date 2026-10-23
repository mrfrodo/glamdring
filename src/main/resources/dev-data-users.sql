INSERT INTO app_user (username, password_hash, enabled)
SELECT 'asd', '{noop}asd', TRUE
WHERE NOT EXISTS (SELECT 1 FROM app_user WHERE username = 'asd');

INSERT INTO app_user_role (user_id, role)
SELECT id, 'USER' FROM app_user
WHERE username = 'asd'
  AND NOT EXISTS (
      SELECT 1 FROM app_user_role r WHERE r.user_id = app_user.id AND r.role = 'USER');
