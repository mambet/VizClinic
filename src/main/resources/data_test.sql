INSERT INTO users(enabled,username,password)
--password 0
SELECT true,'s','$2a$10$xTmC5gnvno9acbNKJA8KpuznqtAWne4bTd6mRqcoE4qMv/aiSf98u'
WHERE NOT EXISTS (
    SELECT 1 FROM users
    WHERE username = 's'
);

INSERT INTO authorities(authority, username)
SELECT 'ROLE_ADMIN', 's'
WHERE NOT EXISTS (
    SELECT 1 FROM authorities
    WHERE username = 's'
);