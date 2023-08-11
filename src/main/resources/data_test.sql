INSERT INTO users(enabled,username,password)
--password Jghq#sKX!yf4PC
SELECT true,'sergej','$2a$10$YCmPqlLPpbiOo8A6shVOXubpzkLvE.DQxMr5/VX8uCxNImKnZ3eJ.'
WHERE NOT EXISTS (
    SELECT 1 FROM users
    WHERE username = 'sergej'
);

INSERT INTO authorities(authority, username)
SELECT 'ROLE_ADMIN', 'sergej'
WHERE NOT EXISTS (
    SELECT 1 FROM authorities
    WHERE username = 'sergej'
);