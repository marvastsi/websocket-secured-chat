-- This file is automatically loaded on startup
--
-- The password is the name's value
INSERT INTO public.user (id, created_at, updated_at, cpf, password, name)
 VALUES ('7db77e37-7241-45e9-8063-e09159a80dbd', LOCALTIMESTAMP(), now(), '96370780022', '$2a$10$ZUjzkn.qMVb3df4xEIRADu0d00AWHtAyiWUFRvSsvFMnhR12zORG6', 'Frodo123'),
 		('8655400c-c223-4569-90f0-086eadb29e15', LOCALTIMESTAMP(), now(), '66582560042', '$2a$10$EAge/tMddp.sQjmrYXZIDu4cGB5MAthIecpPOLo2jP7ez89oQ4L4.', 'Gandalf123');
 
INSERT INTO user_role (user_id, role)
 VALUES ('7db77e37-7241-45e9-8063-e09159a80dbd', 'ROLE_API_USER'),
 		('8655400c-c223-4569-90f0-086eadb29e15', 'ROLE_API_USER');
