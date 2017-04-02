CREATE SEQUENCE public.match_queue_id_seq
    INCREMENT 1
    START 41
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE public.match_queue_id_seq
    OWNER TO postgres;

GRANT SELECT, UPDATE ON SEQUENCE public.match_queue_id_seq TO anagrams_readwrite;

GRANT ALL ON SEQUENCE public.match_queue_id_seq TO postgres;
