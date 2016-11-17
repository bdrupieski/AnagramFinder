-- Table: public.match_queue

-- DROP TABLE public.match_queue;

CREATE TABLE public.match_queue
(
    id integer NOT NULL DEFAULT nextval('match_queue_id_seq'::regclass),
    match_id integer NOT NULL,
    order_as_shown boolean NOT NULL DEFAULT true,
    date_queued timestamp without time zone NOT NULL DEFAULT now(),
    date_posted timestamp without time zone,
    status text COLLATE pg_catalog."default" NOT NULL DEFAULT 'pending'::text,
    message text COLLATE pg_catalog."default",
    date_error timestamp without time zone,
    CONSTRAINT match_queue_pkey PRIMARY KEY (id),
    CONSTRAINT match_queue_match_id_fkey FOREIGN KEY (match_id)
        REFERENCES public.anagram_matches (id) MATCH SIMPLE
        ON UPDATE RESTRICT
        ON DELETE RESTRICT
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.match_queue
    OWNER to postgres;

GRANT INSERT, SELECT, UPDATE, DELETE ON TABLE public.match_queue TO anagrams_readwrite;

GRANT ALL ON TABLE public.match_queue TO postgres;

-- Index: match_queue_status_index

-- DROP INDEX public.match_queue_status_index;

CREATE INDEX match_queue_status_index
    ON public.match_queue USING btree
    (status COLLATE pg_catalog."default")
    TABLESPACE pg_default;
