-- Table: public.tweets

-- DROP TABLE public.tweets;

CREATE TABLE public.tweets
(
    id uuid NOT NULL,
    status_id bigint NOT NULL,
    created_at timestamp without time zone NOT NULL,
    original_text text COLLATE pg_catalog."default" NOT NULL,
    stripped_sorted_text text COLLATE pg_catalog."default" NOT NULL,
    user_id bigint NOT NULL,
    user_name text COLLATE pg_catalog."default" NOT NULL,
    date_existence_last_checked timestamp without time zone NOT NULL DEFAULT now(),
    CONSTRAINT tweets_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.tweets
    OWNER to postgres;

GRANT INSERT, SELECT, UPDATE, DELETE ON TABLE public.tweets TO anagrams_readwrite;

GRANT ALL ON TABLE public.tweets TO postgres;

-- Index: stripped_sorted_text_index

-- DROP INDEX public.stripped_sorted_text_index;

CREATE INDEX stripped_sorted_text_index
    ON public.tweets USING btree
    (stripped_sorted_text COLLATE pg_catalog."default")
    TABLESPACE pg_default;
