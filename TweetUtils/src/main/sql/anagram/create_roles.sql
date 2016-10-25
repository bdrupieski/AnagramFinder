CREATE ROLE anagrams_insertselect WITH LOGIN ENCRYPTED PASSWORD 'xxxxx';
GRANT INSERT ON ALL TABLES IN SCHEMA public TO anagrams_insertselect;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO anagrams_insertselect;
GRANT SELECT ON ALL SEQUENCES IN SCHEMA public TO anagrams_insertselect;

CREATE ROLE anagrams_readonly WITH LOGIN ENCRYPTED PASSWORD 'xxxxx';
GRANT SELECT ON ALL TABLES IN SCHEMA public TO anagrams_readonly;
