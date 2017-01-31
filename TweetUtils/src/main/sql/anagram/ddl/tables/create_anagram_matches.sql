-- Table: public.anagram_matches

-- DROP TABLE public.anagram_matches;

CREATE TABLE public.anagram_matches
(
    id integer NOT NULL DEFAULT nextval('anagram_matches_id_seq'::regclass),
    tweet1_id uuid NOT NULL,
    tweet2_id uuid NOT NULL,
    edit_distance_original_text integer NOT NULL,
    edit_distance_stripped_text integer NOT NULL,
    hamming_distance_stripped_text integer NOT NULL,
    longest_common_substring_length_stripped_text integer NOT NULL,
    word_count_difference integer NOT NULL,
    total_words integer NOT NULL,
    inverse_lcs_length_to_total_length_ratio real NOT NULL,
    edit_distance_to_length_ratio real NOT NULL,
    different_word_count_to_total_word_count_ratio real NOT NULL,
    is_same_rearranged integer NOT NULL,
    interesting_factor real NOT NULL,
    rejected boolean NOT NULL DEFAULT false,
    tweet1_retweet_id bigint,
    tweet2_retweet_id bigint,
    date_retweeted timestamp without time zone,
    date_created timestamp without time zone NOT NULL DEFAULT now(),
    date_unretweeted timestamp without time zone,
    attempted_approval boolean NOT NULL DEFAULT false,
    auto_rejected boolean NOT NULL DEFAULT false,
    date_rejected timestamp without time zone,
    date_posted_tumblr timestamp without time zone,
    tumblr_post_id bigint,
    english_words_to_total_word_count_ratio double precision,
    date_unposted_tumblr timestamp without time zone,
    unretweeted_from_cleanup boolean NOT NULL DEFAULT false,
    unretweeted_manually boolean NOT NULL DEFAULT false,
    unposted_tumblr_manually boolean NOT NULL DEFAULT false,
    unretweeted_legacy_cleanup boolean NOT NULL DEFAULT false,
    CONSTRAINT anagram_matches_pkey PRIMARY KEY (id),
    CONSTRAINT anagram_matches_tweet1_id_fkey FOREIGN KEY (tweet1_id)
    REFERENCES public.tweets (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
    CONSTRAINT anagram_matches_tweet2_id_fkey FOREIGN KEY (tweet2_id)
    REFERENCES public.tweets (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.anagram_matches
    OWNER to postgres;

GRANT INSERT, SELECT, UPDATE, DELETE ON TABLE public.anagram_matches TO anagrams_readwrite;

GRANT ALL ON TABLE public.anagram_matches TO postgres;

-- Index: date_created_index

-- DROP INDEX public.date_created_index;

CREATE INDEX date_created_index
    ON public.anagram_matches USING btree
    (date_created)
TABLESPACE pg_default;

-- Index: interesting_factor_index

-- DROP INDEX public.interesting_factor_index;

CREATE INDEX interesting_factor_index
    ON public.anagram_matches USING btree
    (interesting_factor)
TABLESPACE pg_default;
