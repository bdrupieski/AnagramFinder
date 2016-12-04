SELECT
  anagram_matches.inverse_lcs_length_to_total_length_ratio                      AS lcs_ratio,
  anagram_matches.edit_distance_original_text                                   AS edit_ratio,
  anagram_matches.different_word_count_to_total_word_count_ratio                AS diff_word_count_ratio,
  anagram_matches.is_same_rearranged                                            AS same_rearranged,
  anagram_matches.interesting_factor                                            AS interesting,
  tweet1.original_text                                                          AS t1_text,
  tweet2.original_text                                                          AS t2_text,
  CONCAT('http://twitter.com/', tweet1.user_name, '/status/', tweet1.status_id) AS t1_url,
  CONCAT('http://twitter.com/', tweet2.user_name, '/status/', tweet2.status_id) AS t2_url,
  tweet1.created_at                                                             AS t1_created,
  tweet2.created_at                                                             AS t2_created
FROM
  anagram_matches
  INNER JOIN tweets tweet1 ON anagram_matches.tweet1_id = tweet1.id
  INNER JOIN tweets tweet2 ON anagram_matches.tweet2_id = tweet2.id
ORDER BY
  anagram_matches.interesting_factor DESC;
