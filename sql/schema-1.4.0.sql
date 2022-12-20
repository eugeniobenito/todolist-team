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

