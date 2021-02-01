--
-- PostgreSQL database dump
--

-- Dumped from database version 13.1
-- Dumped by pg_dump version 13.1

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
-- Name: comments; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.comments (
    commentid integer NOT NULL,
    postid integer NOT NULL,
    userid integer NOT NULL,
    comment character varying(512) NOT NULL,
    date timestamp(6) with time zone NOT NULL
);


ALTER TABLE public.comments OWNER TO postgres;

--
-- Name: comments_commentid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.comments_commentid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.comments_commentid_seq OWNER TO postgres;

--
-- Name: comments_commentid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.comments_commentid_seq OWNED BY public.comments.commentid;


--
-- Name: likes; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.likes (
    postid integer NOT NULL,
    userid integer NOT NULL
);


ALTER TABLE public.likes OWNER TO postgres;

--
-- Name: posts; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.posts (
    postid integer NOT NULL,
    creatorid integer NOT NULL,
    title character varying(126) NOT NULL,
    descriptorid integer,
    created_at timestamp(6) without time zone NOT NULL,
    description character varying(512)
);


ALTER TABLE public.posts OWNER TO postgres;

--
-- Name: posts_postid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.posts_postid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.posts_postid_seq OWNER TO postgres;

--
-- Name: posts_postid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.posts_postid_seq OWNED BY public.posts.postid;


--
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    userid integer NOT NULL,
    username character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    email character varying(2555) NOT NULL
);


ALTER TABLE public.users OWNER TO postgres;

--
-- Name: users_userid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.users_userid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.users_userid_seq OWNER TO postgres;

--
-- Name: users_userid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.users_userid_seq OWNED BY public.users.userid;


--
-- Name: comments commentid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.comments ALTER COLUMN commentid SET DEFAULT nextval('public.comments_commentid_seq'::regclass);


--
-- Name: posts postid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.posts ALTER COLUMN postid SET DEFAULT nextval('public.posts_postid_seq'::regclass);


--
-- Name: users userid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users ALTER COLUMN userid SET DEFAULT nextval('public.users_userid_seq'::regclass);


--
-- Data for Name: comments; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.comments (commentid, postid, userid, comment, date) FROM stdin;
\.


--
-- Data for Name: likes; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.likes (postid, userid) FROM stdin;
\.


--
-- Data for Name: posts; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.posts (postid, creatorid, title, descriptorid, created_at, description) FROM stdin;
1	2	First post	\N	2021-02-01 00:00:00	LOOL who needs description?? OwU
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (userid, username, password, email) FROM stdin;
1	Mike	password1	MikeRogers@us.gov
2	Ilya	expertopinionisbestshowthateverexistedinwholeentireryofexistance	ilyaSHABASHABASHABA@mail.ru
4	crutoiChel2280000	hahahareshilmenyavzlomatanichegoneviedetpitushik	d.kosenko@gmail.com
3	MEGACHAD	dungeonmaster96	definetlynotvirgin@gmail.com
5	MEGACHAD	dungeonmaster96	definetlynotvirgin@gmail.com
6	MEGACHAD	dungeonmaster96	definetlynotvirgin@gmail.com
7	MEGACHAD	dungeonmaster96	definetlynotvirgin@gmail.com
8	MEGACHAD	dungeonmaster96	definetlynotvirgin@gmail.com
9	MEGACHAD	dungeonmaster96	definetlynotvirgin@gmail.com
10	MEGACHAD	dungeonmaster96	definetlynotvirgin@gmail.com
11	MEGACHAD	dungeonmaster96	definetlynotvirgin@gmail.com
12	MEGACHAD	dungeonmaster96	definetlynotvirgin@gmail.com
13	MEGACHAD	dungeonmaster96	definetlynotvirgin@gmail.com
14	MEGACHAD	dungeonmaster96	definetlynotvirgin@gmail.com
15	MEGACHAD	dungeonmaster96	definetlynotvirgin@gmail.com
16	MEGACHAD	dungeonmaster96	definetlynotvirgin@gmail.com
17	MEGACHAD	dungeonmaster96	definetlynotvirgin@gmail.com
\.


--
-- Name: comments_commentid_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.comments_commentid_seq', 1, false);


--
-- Name: posts_postid_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.posts_postid_seq', 1, false);


--
-- Name: users_userid_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.users_userid_seq', 17, true);


--
-- Name: comments comments_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.comments
    ADD CONSTRAINT comments_pkey PRIMARY KEY (commentid);


--
-- Name: posts posts_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.posts
    ADD CONSTRAINT posts_pkey PRIMARY KEY (postid);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (userid);


--
-- PostgreSQL database dump complete
--

