--
-- PostgreSQL database dump
--

-- Dumped from database version 15.1 (Debian 15.1-1.pgdg110+1)
-- Dumped by pg_dump version 15.1 (Debian 15.1-1.pgdg110+1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

ALTER TABLE ONLY public.equipo_usuario DROP CONSTRAINT fksabsvjgvfuen6hmyg7gn7oq4v;
ALTER TABLE ONLY public.equipo_usuario DROP CONSTRAINT fkk4y9gec15ccnirp3r7w29o66p;
ALTER TABLE ONLY public.tareas DROP CONSTRAINT fkdmoaxl7yv4q6vkc9h32wvbddr;
ALTER TABLE ONLY public.usuarios DROP CONSTRAINT usuarios_pkey;
ALTER TABLE ONLY public.tareas DROP CONSTRAINT tareas_pkey;
ALTER TABLE ONLY public.equipos DROP CONSTRAINT equipos_pkey;
ALTER TABLE ONLY public.equipo_usuario DROP CONSTRAINT equipo_usuario_pkey;
ALTER TABLE public.usuarios ALTER COLUMN id DROP DEFAULT;
ALTER TABLE public.tareas ALTER COLUMN id DROP DEFAULT;
ALTER TABLE public.equipos ALTER COLUMN id DROP DEFAULT;
DROP SEQUENCE public.usuarios_id_seq;
DROP TABLE public.usuarios;
DROP SEQUENCE public.tareas_id_seq;
DROP TABLE public.tareas;
DROP SEQUENCE public.equipos_id_seq;
DROP TABLE public.equipos;
DROP TABLE public.equipo_usuario;
SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: equipo_usuario; Type: TABLE; Schema: public; Owner: mads
--

CREATE TABLE public.equipo_usuario (
    fk_equipo bigint NOT NULL,
    fk_usuario bigint NOT NULL
);


ALTER TABLE public.equipo_usuario OWNER TO mads;

--
-- Name: equipos; Type: TABLE; Schema: public; Owner: mads
--

CREATE TABLE public.equipos (
    id bigint NOT NULL,
    nombre character varying(255),
    descripcion character varying(255)
);


ALTER TABLE public.equipos OWNER TO mads;

--
-- Name: equipos_id_seq; Type: SEQUENCE; Schema: public; Owner: mads
--

CREATE SEQUENCE public.equipos_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.equipos_id_seq OWNER TO mads;

--
-- Name: equipos_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: mads
--

ALTER SEQUENCE public.equipos_id_seq OWNED BY public.equipos.id;


--
-- Name: tareas; Type: TABLE; Schema: public; Owner: mads
--

CREATE TABLE public.tareas (
    id bigint NOT NULL,
    titulo character varying(255) NOT NULL,
    usuario_id bigint NOT NULL
);


ALTER TABLE public.tareas OWNER TO mads;

--
-- Name: tareas_id_seq; Type: SEQUENCE; Schema: public; Owner: mads
--

CREATE SEQUENCE public.tareas_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.tareas_id_seq OWNER TO mads;

--
-- Name: tareas_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: mads
--

ALTER SEQUENCE public.tareas_id_seq OWNED BY public.tareas.id;


--
-- Name: usuarios; Type: TABLE; Schema: public; Owner: mads
--

CREATE TABLE public.usuarios (
    id bigint NOT NULL,
    blocked boolean,
    email character varying(255) NOT NULL,
    fecha_nacimiento date,
    is_admin boolean,
    nombre character varying(255),
    password character varying(255)
);


ALTER TABLE public.usuarios OWNER TO mads;

--
-- Name: usuarios_id_seq; Type: SEQUENCE; Schema: public; Owner: mads
--

CREATE SEQUENCE public.usuarios_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.usuarios_id_seq OWNER TO mads;

--
-- Name: usuarios_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: mads
--

ALTER SEQUENCE public.usuarios_id_seq OWNED BY public.usuarios.id;


--
-- Name: equipos id; Type: DEFAULT; Schema: public; Owner: mads
--

ALTER TABLE ONLY public.equipos ALTER COLUMN id SET DEFAULT nextval('public.equipos_id_seq'::regclass);


--
-- Name: tareas id; Type: DEFAULT; Schema: public; Owner: mads
--

ALTER TABLE ONLY public.tareas ALTER COLUMN id SET DEFAULT nextval('public.tareas_id_seq'::regclass);


--
-- Name: usuarios id; Type: DEFAULT; Schema: public; Owner: mads
--

ALTER TABLE ONLY public.usuarios ALTER COLUMN id SET DEFAULT nextval('public.usuarios_id_seq'::regclass);


--
-- Data for Name: equipo_usuario; Type: TABLE DATA; Schema: public; Owner: mads
--

COPY public.equipo_usuario (fk_equipo, fk_usuario) FROM stdin;
2	1
1	3
5	3
10	3
\.


--
-- Data for Name: equipos; Type: TABLE DATA; Schema: public; Owner: mads
--

COPY public.equipos (id, nombre, descripcion) FROM stdin;
1	mads	\N
2	 barcelona	\N
3	madrid	\N
4	scrum	\N
5	dca	\N
6	rayo vallecano	falcao\r\n
7	iweb	php laravel y nunca node \r\n
8	sergio team	
9	eu team	el equipo de eu\r\n
10	alvaro team	el equipo de alvaro\r\n
\.


--
-- Data for Name: tareas; Type: TABLE DATA; Schema: public; Owner: mads
--

COPY public.tareas (id, titulo, usuario_id) FROM stdin;
1	sacar backup de la db	1
2	estudiar	3
3	salir a correr	3
4	ir a natacion	4
5	entregar mads	3
6	mirar las notas	3
7	ver a argentina	3
\.


--
-- Data for Name: usuarios; Type: TABLE DATA; Schema: public; Owner: mads
--

COPY public.usuarios (id, blocked, email, fecha_nacimiento, is_admin, nombre, password) FROM stdin;
1	f	admin@admin	\N	t	admin	123
2	f	eu@eu	\N	f	eugenio	123
3	f	ser@ser	\N	f	sergio	123
4	f	alv@alv	\N	f	alvaro	123
\.


--
-- Name: equipos_id_seq; Type: SEQUENCE SET; Schema: public; Owner: mads
--

SELECT pg_catalog.setval('public.equipos_id_seq', 10, true);


--
-- Name: tareas_id_seq; Type: SEQUENCE SET; Schema: public; Owner: mads
--

SELECT pg_catalog.setval('public.tareas_id_seq', 7, true);


--
-- Name: usuarios_id_seq; Type: SEQUENCE SET; Schema: public; Owner: mads
--

SELECT pg_catalog.setval('public.usuarios_id_seq', 4, true);


--
-- Name: equipo_usuario equipo_usuario_pkey; Type: CONSTRAINT; Schema: public; Owner: mads
--

ALTER TABLE ONLY public.equipo_usuario
    ADD CONSTRAINT equipo_usuario_pkey PRIMARY KEY (fk_equipo, fk_usuario);


--
-- Name: equipos equipos_pkey; Type: CONSTRAINT; Schema: public; Owner: mads
--

ALTER TABLE ONLY public.equipos
    ADD CONSTRAINT equipos_pkey PRIMARY KEY (id);


--
-- Name: tareas tareas_pkey; Type: CONSTRAINT; Schema: public; Owner: mads
--

ALTER TABLE ONLY public.tareas
    ADD CONSTRAINT tareas_pkey PRIMARY KEY (id);


--
-- Name: usuarios usuarios_pkey; Type: CONSTRAINT; Schema: public; Owner: mads
--

ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT usuarios_pkey PRIMARY KEY (id);


--
-- Name: tareas fkdmoaxl7yv4q6vkc9h32wvbddr; Type: FK CONSTRAINT; Schema: public; Owner: mads
--

ALTER TABLE ONLY public.tareas
    ADD CONSTRAINT fkdmoaxl7yv4q6vkc9h32wvbddr FOREIGN KEY (usuario_id) REFERENCES public.usuarios(id);


--
-- Name: equipo_usuario fkk4y9gec15ccnirp3r7w29o66p; Type: FK CONSTRAINT; Schema: public; Owner: mads
--

ALTER TABLE ONLY public.equipo_usuario
    ADD CONSTRAINT fkk4y9gec15ccnirp3r7w29o66p FOREIGN KEY (fk_equipo) REFERENCES public.equipos(id);


--
-- Name: equipo_usuario fksabsvjgvfuen6hmyg7gn7oq4v; Type: FK CONSTRAINT; Schema: public; Owner: mads
--

ALTER TABLE ONLY public.equipo_usuario
    ADD CONSTRAINT fksabsvjgvfuen6hmyg7gn7oq4v FOREIGN KEY (fk_usuario) REFERENCES public.usuarios(id);


--
-- PostgreSQL database dump complete
--

