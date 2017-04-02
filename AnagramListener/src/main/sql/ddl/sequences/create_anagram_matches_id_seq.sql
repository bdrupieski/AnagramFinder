CREATE SEQUENCE public.anagram_matches_id_seq
    INCREMENT 1
    START 159798
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE public.anagram_matches_id_seq
    OWNER TO postgres;

GRANT SELECT, UPDATE ON SEQUENCE public.anagram_matches_id_seq TO anagrams_readwrite;

GRANT ALL ON SEQUENCE public.anagram_matches_id_seq TO postgres;
