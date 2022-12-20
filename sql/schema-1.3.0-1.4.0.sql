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

ALTER TABLE public.equipos ADD COLUMN is_private BOOLEAN;
ALTER TABLE public.equipos ADD COLUMN  nombre character varying(255);
ALTER TABLE public.equipos ADD COLUMN admin_id bigint;


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
--Name: invitaciones_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: mads


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

ALTER TABLE public.tareas ADD COLUMN status character varying(255);
ALTER TABLE public.tareas ADD COLUMN  titulo character varying(255) NOT NULL;



CREATE TABLE public.tareas_proyecto (
        id bigint NOT NULL,
        nombre character varying(255),
         status integer,
         proyecto_id bigint NOT NULL
);


ALTER TABLE public.tareas_proyecto OWNER TO mads;


 CREATE SEQUENCE public.tareas_proyecto_id_seq
         START WITH 1
     INCREMENT BY 1
    NO MINVALUE
     NO MAXVALUE
    CACHE 1;

ALTER TABLE public.tareas_proyecto_id_seq OWNER TO mads;

 ALTER SEQUENCE public.tareas_proyecto_id_seq OWNED BY public.tareas_proyecto.id;

ALTER TABLE public.usuarios ADD COLUMN code integer NOT NULL;


ALTER TABLE ONLY public.comentarios_equipo ALTER COLUMN id SET DEFAULT nextval('public.comentarios_equipo_id_seq'::regclass);

ALTER TABLE ONLY public.invitaciones ALTER COLUMN id SET DEFAULT nextval('public.invitaciones_id_seq'::regclass);


ALTER TABLE ONLY public.proyectos ALTER COLUMN id SET DEFAULT nextval('public.proyectos_id_seq'::regclass);


ALTER TABLE ONLY public.tareas_proyecto ALTER COLUMN id SET DEFAULT nextval('public.tareas_proyecto_id_seq'::regclass);


ALTER TABLE ONLY public.comentarios_equipo
    ADD CONSTRAINT comentarios_equipo_pkey PRIMARY KEY (id);


ALTER TABLE ONLY public.invitaciones
   ADD CONSTRAINT invitaciones_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.proyectos
         ADD CONSTRAINT proyectos_pkey PRIMARY KEY (id);


ALTER TABLE ONLY public.tareaproyecto_usuario
     ADD CONSTRAINT tareaproyecto_usuario_pkey PRIMARY KEY (fk_tareaproyecto, fk_usuario);


ALTER TABLE ONLY public.tareas_proyecto
      ADD CONSTRAINT tareas_proyecto_pkey PRIMARY KEY (id);



 -- Name: proyectos fk5w1gfpqqeuumkj1897ndr0uwn; Type: FK CONSTRAINT; Schema: public; Owner: mads
 --

 ALTER TABLE ONLY public.proyectos
         ADD CONSTRAINT fk5w1gfpqqeuumkj1897ndr0uwn FOREIGN KEY (equipo_id) REFERENCES public.equipos(id);


 --
 -- Name: tareas_proyecto fk7b90nnmkad8n7sxeggwud4t0d; Type: FK CONSTRAINT; Schema: public; Owner: mads
 --

 ALTER TABLE ONLY public.tareas_proyecto    ADD CONSTRAINT fk7b90nnmkad8n7sxeggwud4t0d FOREIGN KEY (proyecto_id) REFERENCES public.proyectos(id);


 --
 -- Name: tareaproyecto_usuario fka7q8jamak4dc937ursugo1067; Type: FK CONSTRAINT; Schema: public; Owner: mads
ALTER TABLE ONLY public.tareaproyecto_usuario
         ADD CONSTRAINT fka7q8jamak4dc937ursugo1067 FOREIGN KEY (fk_tareaproyecto) REFERENCES public.tareas_proyecto(id);

-- Name: comentarios_equipo fkhj3eve3tmqwfurukas5et1pp3; Type: FK CONSTRAINT; Schema: public; Owner: mads
--

ALTER TABLE ONLY public.comentarios_equipo
         ADD CONSTRAINT fkhj3eve3tmqwfurukas5et1pp3 FOREIGN KEY (equipo_id) REFERENCES public.equipos(id);


 --
 -- Name: equipos fkiri6546i8h6alclpsea6oicc8; Type: FK CONSTRAINT; Schema: public; Owner: mads
 --

ALTER TABLE ONLY public.equipos
    ADD CONSTRAINT fkiri6546i8h6alclpsea6oicc8 FOREIGN KEY (admin_id) REFERENCES public.usuarios(id);

-- Name: tareaproyecto_usuario fkna9g2makro282w4rxai5k1ktf; Type: FK CONSTRAINT; Schema: public; Owner: mads
 --

ALTER TABLE ONLY public.tareaproyecto_usuario
         ADD CONSTRAINT fkna9g2makro282w4rxai5k1ktf FOREIGN KEY (fk_usuario) REFERENCES public.usuarios(id);


 --
 -- Name: comentarios_equipo fkpbjt37tnds4u4l1fa8xan2ayx; Type: FK CONSTRAINT; Schema: public; Owner: mads


ALTER TABLE ONLY public.comentarios_equipo
    ADD CONSTRAINT fkpbjt37tnds4u4l1fa8xan2ayx FOREIGN KEY (usuario_id) REFERENCES public.usuarios(id);
