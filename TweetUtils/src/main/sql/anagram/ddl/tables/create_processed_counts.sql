-- Table: public.processed_counts

-- DROP TABLE public.processed_counts;

CREATE TABLE public.processed_counts
(
  id integer NOT NULL DEFAULT nextval('processed_counts_id_seq'::regclass),
  date_created timestamp without time zone NOT NULL DEFAULT now(),
  seconds_since_previous_reset double precision NOT NULL,
  received_status_count_since_previous_reset integer NOT NULL,
  status_met_filter_count_since_previous_reset integer NOT NULL,
  tweet_met_filter_count_since_previous_reset integer NOT NULL,
  saved_tweet_count_since_previous_reset integer NOT NULL,
  saved_anagram_count_since_previous_reset integer NOT NULL,
  now timestamp without time zone NOT NULL,
  previous_reset timestamp without time zone NOT NULL,
  CONSTRAINT processed_counts_pkey PRIMARY KEY (id)
)
WITH (
OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.processed_counts
  OWNER to postgres;

GRANT INSERT, SELECT, UPDATE, DELETE ON TABLE public.processed_counts TO anagrams_readwrite;

GRANT ALL ON TABLE public.processed_counts TO postgres;
