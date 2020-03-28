-- Create the initial schema (using default 'public' schema)
--
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
--
CREATE TABLE public.user_role
(
    user_id VARCHAR(255) NOT NULL,
    role VARCHAR(255),
    CONSTRAINT fkey_user_id FOREIGN KEY (user_id)
    REFERENCES public.user(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);
--
CREATE TABLE IF NOT EXISTS public.message
(
    id VARCHAR(255) NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    content VARCHAR(255) NOT NULL,
    type VARCHAR(255),
    sender_id VARCHAR(255),
    recipient_id VARCHAR(255),
    CONSTRAINT message_pkey PRIMARY KEY (id),
    CONSTRAINT fkey_message_sender_id FOREIGN KEY (sender_id)
        REFERENCES public.user (id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkey_message_recipient_id FOREIGN KEY (recipient_id)
        REFERENCES public.user (id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);