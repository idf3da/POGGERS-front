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

--
-- Name: trigger_set_timestamp(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.trigger_set_timestamp() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
  NEW.updated_at = NOW();
    RETURN NEW;
    END;
    $$;


ALTER FUNCTION public.trigger_set_timestamp() OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: comments; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.comments (
    commentid integer NOT NULL,
    postid integer NOT NULL,
    userid integer NOT NULL,
    comment character varying(1024) NOT NULL,
    date timestamp with time zone DEFAULT now() NOT NULL
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
    created_at timestamp with time zone DEFAULT now() NOT NULL,
    edited_at timestamp with time zone DEFAULT now() NOT NULL,
    description character varying(1024)
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
-- Name: test_date; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.test_date (
    id integer NOT NULL,
    content text,
    created_at timestamp with time zone DEFAULT now() NOT NULL,
    updated_at timestamp with time zone DEFAULT now() NOT NULL,
    completed_at timestamp with time zone
);


ALTER TABLE public.test_date OWNER TO postgres;

--
-- Name: test_date_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.test_date_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.test_date_id_seq OWNER TO postgres;

--
-- Name: test_date_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.test_date_id_seq OWNED BY public.test_date.id;


--
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    userid integer NOT NULL,
    username character varying(32) NOT NULL,
    password character varying(256) NOT NULL,
    email character varying(255) NOT NULL
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
-- Name: test_date id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.test_date ALTER COLUMN id SET DEFAULT nextval('public.test_date_id_seq'::regclass);


--
-- Name: users userid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users ALTER COLUMN userid SET DEFAULT nextval('public.users_userid_seq'::regclass);


--
-- Data for Name: comments; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.comments (commentid, postid, userid, comment, date) FROM stdin;
1	56749	1	LOOOOOOOOL DATS SO CRINGE BROOOOOOOOOOOOO	2021-02-10 20:01:44.775518+03
2	56749	1	LOOOOOOOOL DATS SO CRINGE BROOOOOOOOOOOOO	2021-02-10 20:12:06.400431+03
3	1	56749	LOOOOOOOOL DATS SO CRINGE BROOOOOOOOOOOOO	2021-02-10 20:15:19.165461+03
4	1	56749	LOOOOOOOOL DATS SO CRINGE BROOOOOOOOOOOOO	2021-02-10 20:15:40.403857+03
5	1	56749	LOOOOOOOOL DATS SO CRINGE BROOOOOOOOOOOOO	2021-02-10 20:15:41.118306+03
6	1	56749	LOOOOOOOOL DATS SO CRINGE BROOOOOOOOOOOOO	2021-02-10 20:15:41.738157+03
7	1	56749	LOOOOOOOOL DATS SO CRINGE BROOOOOOOOOOOOO	2021-02-10 20:15:42.312429+03
8	1	56749	LOOOOOOOOL DATS SO CRINGE BROOOOOOOOOOOOO	2021-02-10 20:15:42.614984+03
9	1	56749	LOOOOOOOOL DATS SO CRINGE BROOOOOOOOOOOOO	2021-02-10 20:15:42.931691+03
10	1	56749	LOOOOOOOOL DATS SO CRINGE BROOOOOOOOOOOOO	2021-02-10 20:15:44.707711+03
\.


--
-- Data for Name: likes; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.likes (postid, userid) FROM stdin;
\.


--
-- Data for Name: posts; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.posts (postid, creatorid, title, descriptorid, created_at, edited_at, description) FROM stdin;
1	1	Title1	404	2021-02-08 10:43:28.041693+03	2021-02-08 10:43:28.041693+03	Some description
2	1	My post	404	2021-02-08 13:53:19.330559+03	2021-02-08 13:53:19.330559+03	Description
3	1	My post	404	2021-02-08 14:02:07.321492+03	2021-02-08 14:02:07.321492+03	Description
5	1	My post	404	2021-02-10 18:19:58.456863+03	2021-02-10 18:19:58.456863+03	Description
6	1	My post	404	2021-02-10 18:21:02.423684+03	2021-02-10 18:21:02.423684+03	Description
7	56749	My post	404	2021-02-10 18:54:21.952102+03	2021-02-10 18:54:21.952102+03	Description
\.


--
-- Data for Name: test_date; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.test_date (id, content, created_at, updated_at, completed_at) FROM stdin;
1	date-test	2021-02-08 10:24:40.882328+03	2021-02-08 10:24:40.882328+03	\N
2	date-test	2021-02-08 10:25:04.273901+03	2021-02-08 10:25:04.273901+03	\N
3	date-test	2021-02-08 10:25:05.472523+03	2021-02-08 10:25:05.472523+03	\N
4	date-test	2021-02-08 10:25:06.098757+03	2021-02-08 10:25:06.098757+03	\N
5	date-test	2021-02-08 10:25:06.707633+03	2021-02-08 10:25:06.707633+03	\N
6	date-test	2021-02-08 10:25:07.368974+03	2021-02-08 10:25:07.368974+03	\N
7	date-test	2021-02-08 10:25:07.965627+03	2021-02-08 10:25:07.965627+03	\N
8	date-test	2021-02-08 10:25:08.825026+03	2021-02-08 10:25:08.825026+03	\N
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (userid, username, password, email) FROM stdin;
1	Ilya	expertopinionisbestshowthateverexistedinwholeentireryofexistance	ilyaSHABASHABASHABA@mail.ru
2374	User1944287726	password1944287726	email1944287726@mail.ru
2	Mike	password1	MikeRogers@us.gov
3	crutoiChel2280000	hahahareshilmenyavzlomatanichegoneviedetpitushik	kosenko@mail.ru
107	crutoi_chel_ps_ne_dima	hahahavzlomatmenyareshil?apososipetushok	kosenko1337@mail.ru
108	User89609105	password89609105	email89609105@mail.ru
\.


--
-- Name: comments_commentid_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.comments_commentid_seq', 10, true);


--
-- Name: posts_postid_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.posts_postid_seq', 7, true);


--
-- Name: test_date_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.test_date_id_seq', 8, true);


--
-- Name: users_userid_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.users_userid_seq', 56770, true);


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
-- Name: test_date test_date_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.test_date
    ADD CONSTRAINT test_date_pkey PRIMARY KEY (id);


--
-- Name: users users_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_email_key UNIQUE (email);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (userid);


--
-- Name: users users_username_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_username_key UNIQUE (username);


--
-- Name: test_date set_timestamp; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER set_timestamp BEFORE UPDATE ON public.test_date FOR EACH ROW EXECUTE FUNCTION public.trigger_set_timestamp();


--
-- Name: comments set_timestamp_on_comments; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER set_timestamp_on_comments BEFORE UPDATE ON public.comments FOR EACH ROW EXECUTE FUNCTION public.trigger_set_timestamp();


--
-- Name: posts set_timestamp_on_posts; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER set_timestamp_on_posts BEFORE UPDATE ON public.posts FOR EACH ROW EXECUTE FUNCTION public.trigger_set_timestamp();


--
-- PostgreSQL database dump complete
--

