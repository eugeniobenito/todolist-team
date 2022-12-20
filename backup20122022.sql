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
ALTER TABLE ONLY public.comentarios_equipo DROP CONSTRAINT fkpbjt37tnds4u4l1fa8xan2ayx;
ALTER TABLE ONLY public.tareaproyecto_usuario DROP CONSTRAINT fkna9g2makro282w4rxai5k1ktf;
ALTER TABLE ONLY public.equipo_usuario DROP CONSTRAINT fkk4y9gec15ccnirp3r7w29o66p;
ALTER TABLE ONLY public.equipos DROP CONSTRAINT fkiri6546i8h6alclpsea6oicc8;
ALTER TABLE ONLY public.comentarios_equipo DROP CONSTRAINT fkhj3eve3tmqwfurukas5et1pp3;
ALTER TABLE ONLY public.tareas DROP CONSTRAINT fkdmoaxl7yv4q6vkc9h32wvbddr;
ALTER TABLE ONLY public.tareaproyecto_usuario DROP CONSTRAINT fka7q8jamak4dc937ursugo1067;
ALTER TABLE ONLY public.tareas_proyecto DROP CONSTRAINT fk7b90nnmkad8n7sxeggwud4t0d;
ALTER TABLE ONLY public.proyectos DROP CONSTRAINT fk5w1gfpqqeuumkj1897ndr0uwn;
ALTER TABLE ONLY public.usuarios DROP CONSTRAINT usuarios_pkey;
ALTER TABLE ONLY public.tareas_proyecto DROP CONSTRAINT tareas_proyecto_pkey;
ALTER TABLE ONLY public.tareas DROP CONSTRAINT tareas_pkey;
ALTER TABLE ONLY public.tareaproyecto_usuario DROP CONSTRAINT tareaproyecto_usuario_pkey;
ALTER TABLE ONLY public.proyectos DROP CONSTRAINT proyectos_pkey;
ALTER TABLE ONLY public.invitaciones DROP CONSTRAINT invitaciones_pkey;
ALTER TABLE ONLY public.equipos DROP CONSTRAINT equipos_pkey;
ALTER TABLE ONLY public.equipo_usuario DROP CONSTRAINT equipo_usuario_pkey;
ALTER TABLE ONLY public.comentarios_equipo DROP CONSTRAINT comentarios_equipo_pkey;
ALTER TABLE public.usuarios ALTER COLUMN id DROP DEFAULT;
ALTER TABLE public.tareas_proyecto ALTER COLUMN id DROP DEFAULT;
ALTER TABLE public.tareas ALTER COLUMN id DROP DEFAULT;
ALTER TABLE public.proyectos ALTER COLUMN id DROP DEFAULT;
ALTER TABLE public.invitaciones ALTER COLUMN id DROP DEFAULT;
ALTER TABLE public.equipos ALTER COLUMN id DROP DEFAULT;
ALTER TABLE public.comentarios_equipo ALTER COLUMN id DROP DEFAULT;
DROP SEQUENCE public.usuarios_id_seq;
DROP TABLE public.usuarios;
DROP SEQUENCE public.tareas_proyecto_id_seq;
DROP TABLE public.tareas_proyecto;
DROP SEQUENCE public.tareas_id_seq;
DROP TABLE public.tareas;
DROP TABLE public.tareaproyecto_usuario;
DROP SEQUENCE public.proyectos_id_seq;
DROP TABLE public.proyectos;
DROP SEQUENCE public.invitaciones_id_seq;
DROP TABLE public.invitaciones;
DROP SEQUENCE public.equipos_id_seq;
DROP TABLE public.equipos;
DROP TABLE public.equipo_usuario;
DROP SEQUENCE public.comentarios_equipo_id_seq;
DROP TABLE public.comentarios_equipo;
SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: comentarios_equipo; Type: TABLE; Schema: public; Owner: mads
--

CREATE TABLE public.comentarios_equipo (
    id bigint NOT NULL,
    comentario character varying(255) NOT NULL,
    fecha timestamp without time zone,
    equipo_id bigint NOT NULL,
    usuario_id bigint NOT NULL
);


ALTER TABLE public.comentarios_equipo OWNER TO mads;

--
-- Name: comentarios_equipo_id_seq; Type: SEQUENCE; Schema: public; Owner: mads
--

CREATE SEQUENCE public.comentarios_equipo_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.comentarios_equipo_id_seq OWNER TO mads;

--
-- Name: comentarios_equipo_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: mads
--

ALTER SEQUENCE public.comentarios_equipo_id_seq OWNED BY public.comentarios_equipo.id;


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
    descripcion character varying(255),
    is_private boolean,
    nombre character varying(255),
    admin_id bigint
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
-- Name: invitaciones; Type: TABLE; Schema: public; Owner: mads
--

CREATE TABLE public.invitaciones (
    id bigint NOT NULL,
    id_equipo bigint NOT NULL,
    id_usuario bigint NOT NULL
);


ALTER TABLE public.invitaciones OWNER TO mads;

--
-- Name: invitaciones_id_seq; Type: SEQUENCE; Schema: public; Owner: mads
--

CREATE SEQUENCE public.invitaciones_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.invitaciones_id_seq OWNER TO mads;

--
-- Name: invitaciones_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: mads
--

ALTER SEQUENCE public.invitaciones_id_seq OWNED BY public.invitaciones.id;


--
-- Name: proyectos; Type: TABLE; Schema: public; Owner: mads
--

CREATE TABLE public.proyectos (
    id bigint NOT NULL,
    nombre character varying(255),
    equipo_id bigint NOT NULL
);


ALTER TABLE public.proyectos OWNER TO mads;

--
-- Name: proyectos_id_seq; Type: SEQUENCE; Schema: public; Owner: mads
--

CREATE SEQUENCE public.proyectos_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.proyectos_id_seq OWNER TO mads;

--
-- Name: proyectos_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: mads
--

ALTER SEQUENCE public.proyectos_id_seq OWNED BY public.proyectos.id;


--
-- Name: tareaproyecto_usuario; Type: TABLE; Schema: public; Owner: mads
--

CREATE TABLE public.tareaproyecto_usuario (
    fk_usuario bigint NOT NULL,
    fk_tareaproyecto bigint NOT NULL
);


ALTER TABLE public.tareaproyecto_usuario OWNER TO mads;

--
-- Name: tareas; Type: TABLE; Schema: public; Owner: mads
--

CREATE TABLE public.tareas (
    id bigint NOT NULL,
    fecha_limite date,
    status character varying(255),
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
-- Name: tareas_proyecto; Type: TABLE; Schema: public; Owner: mads
--

CREATE TABLE public.tareas_proyecto (
    id bigint NOT NULL,
    nombre character varying(255),
    status integer,
    proyecto_id bigint NOT NULL
);


ALTER TABLE public.tareas_proyecto OWNER TO mads;

--
-- Name: tareas_proyecto_id_seq; Type: SEQUENCE; Schema: public; Owner: mads
--

CREATE SEQUENCE public.tareas_proyecto_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.tareas_proyecto_id_seq OWNER TO mads;

--
-- Name: tareas_proyecto_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: mads
--

ALTER SEQUENCE public.tareas_proyecto_id_seq OWNED BY public.tareas_proyecto.id;


--
-- Name: usuarios; Type: TABLE; Schema: public; Owner: mads
--

CREATE TABLE public.usuarios (
    id bigint NOT NULL,
    blocked boolean,
    code integer NOT NULL,
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
-- Name: comentarios_equipo id; Type: DEFAULT; Schema: public; Owner: mads
--

ALTER TABLE ONLY public.comentarios_equipo ALTER COLUMN id SET DEFAULT nextval('public.comentarios_equipo_id_seq'::regclass);


--
-- Name: equipos id; Type: DEFAULT; Schema: public; Owner: mads
--

ALTER TABLE ONLY public.equipos ALTER COLUMN id SET DEFAULT nextval('public.equipos_id_seq'::regclass);


--
-- Name: invitaciones id; Type: DEFAULT; Schema: public; Owner: mads
--

ALTER TABLE ONLY public.invitaciones ALTER COLUMN id SET DEFAULT nextval('public.invitaciones_id_seq'::regclass);


--
-- Name: proyectos id; Type: DEFAULT; Schema: public; Owner: mads
--

ALTER TABLE ONLY public.proyectos ALTER COLUMN id SET DEFAULT nextval('public.proyectos_id_seq'::regclass);


--
-- Name: tareas id; Type: DEFAULT; Schema: public; Owner: mads
--

ALTER TABLE ONLY public.tareas ALTER COLUMN id SET DEFAULT nextval('public.tareas_id_seq'::regclass);


--
-- Name: tareas_proyecto id; Type: DEFAULT; Schema: public; Owner: mads
--

ALTER TABLE ONLY public.tareas_proyecto ALTER COLUMN id SET DEFAULT nextval('public.tareas_proyecto_id_seq'::regclass);


--
-- Name: usuarios id; Type: DEFAULT; Schema: public; Owner: mads
--

ALTER TABLE ONLY public.usuarios ALTER COLUMN id SET DEFAULT nextval('public.usuarios_id_seq'::regclass);


--
-- Data for Name: comentarios_equipo; Type: TABLE DATA; Schema: public; Owner: mads
--

COPY public.comentarios_equipo (id, comentario, fecha, equipo_id, usuario_id) FROM stdin;
1	A trabajar ya!	2022-12-20 23:18:59.706	1	2
2	Vivan los collares con macarrones	2022-12-20 23:22:19.626	4	3
\.


--
-- Data for Name: equipo_usuario; Type: TABLE DATA; Schema: public; Owner: mads
--

COPY public.equipo_usuario (fk_equipo, fk_usuario) FROM stdin;
1	2
1	3
4	3
\.


--
-- Data for Name: equipos; Type: TABLE DATA; Schema: public; Owner: mads
--

COPY public.equipos (id, descripcion, is_private, nombre, admin_id) FROM stdin;
1	Equipo 15 de la aplicación de MADS	t	Equipo 15	2
2	Equipo de los que le gusta la aplicacion\r\n	f	ToDoListApp	2
3		f	Amantes de la programacion	2
4		f	Magisterio	2
5		f	Equipo de Eu	3
\.


--
-- Data for Name: invitaciones; Type: TABLE DATA; Schema: public; Owner: mads
--

COPY public.invitaciones (id, id_equipo, id_usuario) FROM stdin;
2	1	4
\.


--
-- Data for Name: proyectos; Type: TABLE DATA; Schema: public; Owner: mads
--

COPY public.proyectos (id, nombre, equipo_id) FROM stdin;
1	MADS	1
2	Aplicacion de adi	1
3	Tareas de Eu	5
\.


--
-- Data for Name: tareaproyecto_usuario; Type: TABLE DATA; Schema: public; Owner: mads
--

COPY public.tareaproyecto_usuario (fk_usuario, fk_tareaproyecto) FROM stdin;
2	2
2	4
2	3
3	2
3	4
3	1
\.


--
-- Data for Name: tareas; Type: TABLE DATA; Schema: public; Owner: mads
--

COPY public.tareas (id, fecha_limite, status, titulo, usuario_id) FROM stdin;
3	2022-12-23	TODO	Entregar DCA	2
4	2023-12-01	TODO	Ir a Cancun	2
2	2022-12-21	IN_PROGRESS	Presentar mads	2
1	\N	DONE	Comprar regalos de navidad	2
5	2022-12-23	TODO	Cartulina navidad	3
6	2022-12-21	TODO	Postal de adviento	3
7	2022-12-20	TODO	Comprar fluimocil	4
8	2023-12-22	TODO	Comprar mas fluimocil	4
\.


--
-- Data for Name: tareas_proyecto; Type: TABLE DATA; Schema: public; Owner: mads
--

COPY public.tareas_proyecto (id, nombre, status, proyecto_id) FROM stdin;
1	Historia de usuario 01	0	1
4	Historia de usuario 04	0	1
2	Historia de usuario 02	1	1
3	Historia de usuario 03	2	1
\.


--
-- Data for Name: usuarios; Type: TABLE DATA; Schema: public; Owner: mads
--

COPY public.usuarios (id, blocked, code, email, fecha_nacimiento, is_admin, nombre, password) FROM stdin;
1	f	69779	admin@admin	\N	t	Administrador Paco	123
3	f	87068	eugeniobenito00@gmail.com	\N	f	Eugenio	123
4	f	18060	lariosanchezalvaro99@gmail.com	\N	f	Alvaro	123
2	f	53451	sergiobaeza077@gmail.com	\N	f	Sergio	123
\.


--
-- Name: comentarios_equipo_id_seq; Type: SEQUENCE SET; Schema: public; Owner: mads
--

SELECT pg_catalog.setval('public.comentarios_equipo_id_seq', 2, true);


--
-- Name: equipos_id_seq; Type: SEQUENCE SET; Schema: public; Owner: mads
--

SELECT pg_catalog.setval('public.equipos_id_seq', 5, true);


--
-- Name: invitaciones_id_seq; Type: SEQUENCE SET; Schema: public; Owner: mads
--

SELECT pg_catalog.setval('public.invitaciones_id_seq', 2, true);


--
-- Name: proyectos_id_seq; Type: SEQUENCE SET; Schema: public; Owner: mads
--

SELECT pg_catalog.setval('public.proyectos_id_seq', 3, true);


--
-- Name: tareas_id_seq; Type: SEQUENCE SET; Schema: public; Owner: mads
--

SELECT pg_catalog.setval('public.tareas_id_seq', 8, true);


--
-- Name: tareas_proyecto_id_seq; Type: SEQUENCE SET; Schema: public; Owner: mads
--

SELECT pg_catalog.setval('public.tareas_proyecto_id_seq', 4, true);


--
-- Name: usuarios_id_seq; Type: SEQUENCE SET; Schema: public; Owner: mads
--

SELECT pg_catalog.setval('public.usuarios_id_seq', 4, true);


--
-- Name: comentarios_equipo comentarios_equipo_pkey; Type: CONSTRAINT; Schema: public; Owner: mads
--

ALTER TABLE ONLY public.comentarios_equipo
    ADD CONSTRAINT comentarios_equipo_pkey PRIMARY KEY (id);


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
-- Name: invitaciones invitaciones_pkey; Type: CONSTRAINT; Schema: public; Owner: mads
--

ALTER TABLE ONLY public.invitaciones
    ADD CONSTRAINT invitaciones_pkey PRIMARY KEY (id);


--
-- Name: proyectos proyectos_pkey; Type: CONSTRAINT; Schema: public; Owner: mads
--

ALTER TABLE ONLY public.proyectos
    ADD CONSTRAINT proyectos_pkey PRIMARY KEY (id);


--
-- Name: tareaproyecto_usuario tareaproyecto_usuario_pkey; Type: CONSTRAINT; Schema: public; Owner: mads
--

ALTER TABLE ONLY public.tareaproyecto_usuario
    ADD CONSTRAINT tareaproyecto_usuario_pkey PRIMARY KEY (fk_tareaproyecto, fk_usuario);


--
-- Name: tareas tareas_pkey; Type: CONSTRAINT; Schema: public; Owner: mads
--

ALTER TABLE ONLY public.tareas
    ADD CONSTRAINT tareas_pkey PRIMARY KEY (id);


--
-- Name: tareas_proyecto tareas_proyecto_pkey; Type: CONSTRAINT; Schema: public; Owner: mads
--

ALTER TABLE ONLY public.tareas_proyecto
    ADD CONSTRAINT tareas_proyecto_pkey PRIMARY KEY (id);


--
-- Name: usuarios usuarios_pkey; Type: CONSTRAINT; Schema: public; Owner: mads
--

ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT usuarios_pkey PRIMARY KEY (id);


--
-- Name: proyectos fk5w1gfpqqeuumkj1897ndr0uwn; Type: FK CONSTRAINT; Schema: public; Owner: mads
--

ALTER TABLE ONLY public.proyectos
    ADD CONSTRAINT fk5w1gfpqqeuumkj1897ndr0uwn FOREIGN KEY (equipo_id) REFERENCES public.equipos(id);


--
-- Name: tareas_proyecto fk7b90nnmkad8n7sxeggwud4t0d; Type: FK CONSTRAINT; Schema: public; Owner: mads
--

ALTER TABLE ONLY public.tareas_proyecto
    ADD CONSTRAINT fk7b90nnmkad8n7sxeggwud4t0d FOREIGN KEY (proyecto_id) REFERENCES public.proyectos(id);


--
-- Name: tareaproyecto_usuario fka7q8jamak4dc937ursugo1067; Type: FK CONSTRAINT; Schema: public; Owner: mads
--

ALTER TABLE ONLY public.tareaproyecto_usuario
    ADD CONSTRAINT fka7q8jamak4dc937ursugo1067 FOREIGN KEY (fk_tareaproyecto) REFERENCES public.tareas_proyecto(id);


--
-- Name: tareas fkdmoaxl7yv4q6vkc9h32wvbddr; Type: FK CONSTRAINT; Schema: public; Owner: mads
--

ALTER TABLE ONLY public.tareas
    ADD CONSTRAINT fkdmoaxl7yv4q6vkc9h32wvbddr FOREIGN KEY (usuario_id) REFERENCES public.usuarios(id);


--
-- Name: comentarios_equipo fkhj3eve3tmqwfurukas5et1pp3; Type: FK CONSTRAINT; Schema: public; Owner: mads
--

ALTER TABLE ONLY public.comentarios_equipo
    ADD CONSTRAINT fkhj3eve3tmqwfurukas5et1pp3 FOREIGN KEY (equipo_id) REFERENCES public.equipos(id);


--
-- Name: equipos fkiri6546i8h6alclpsea6oicc8; Type: FK CONSTRAINT; Schema: public; Owner: mads
--

ALTER TABLE ONLY public.equipos
    ADD CONSTRAINT fkiri6546i8h6alclpsea6oicc8 FOREIGN KEY (admin_id) REFERENCES public.usuarios(id);


--
-- Name: equipo_usuario fkk4y9gec15ccnirp3r7w29o66p; Type: FK CONSTRAINT; Schema: public; Owner: mads
--

ALTER TABLE ONLY public.equipo_usuario
    ADD CONSTRAINT fkk4y9gec15ccnirp3r7w29o66p FOREIGN KEY (fk_equipo) REFERENCES public.equipos(id);


--
-- Name: tareaproyecto_usuario fkna9g2makro282w4rxai5k1ktf; Type: FK CONSTRAINT; Schema: public; Owner: mads
--

ALTER TABLE ONLY public.tareaproyecto_usuario
    ADD CONSTRAINT fkna9g2makro282w4rxai5k1ktf FOREIGN KEY (fk_usuario) REFERENCES public.usuarios(id);


--
-- Name: comentarios_equipo fkpbjt37tnds4u4l1fa8xan2ayx; Type: FK CONSTRAINT; Schema: public; Owner: mads
--

ALTER TABLE ONLY public.comentarios_equipo
    ADD CONSTRAINT fkpbjt37tnds4u4l1fa8xan2ayx FOREIGN KEY (usuario_id) REFERENCES public.usuarios(id);


--
-- Name: equipo_usuario fksabsvjgvfuen6hmyg7gn7oq4v; Type: FK CONSTRAINT; Schema: public; Owner: mads
--

ALTER TABLE ONLY public.equipo_usuario
    ADD CONSTRAINT fksabsvjgvfuen6hmyg7gn7oq4v FOREIGN KEY (fk_usuario) REFERENCES public.usuarios(id);


--
-- PostgreSQL database dump complete
--

