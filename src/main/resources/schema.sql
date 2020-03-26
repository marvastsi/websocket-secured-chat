-- To enable automatically load for his file on startup to create the database schema change this file nome to 'schema.sql'
-- And configure jpa spring property 'ddl-auto' => spring.jpa.hibernate.ddl-auto=none
CREATE TABLE public.user
(
    id VARCHAR(255) NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    cpf VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    CONSTRAINT user_pkey PRIMARY KEY (id),
    CONSTRAINT ukey_user_cpf UNIQUE (cpf)
);

CREATE TABLE public.user_role
(
    user_id VARCHAR(255) NOT NULL,
    role VARCHAR(255),
    CONSTRAINT fkey_user_id FOREIGN KEY (user_id)
    REFERENCES public.user(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);