CREATE SEQUENCE public.processed_counts_id_seq
INCREMENT 1
START 2
MINVALUE 1
MAXVALUE 9223372036854775807
CACHE 1;

ALTER SEQUENCE public.processed_counts_id_seq
OWNER TO postgres;

GRANT SELECT, UPDATE ON SEQUENCE public.processed_counts_id_seq TO anagrams_readwrite;

GRANT ALL ON SEQUENCE public.processed_counts_id_seq TO postgres;
