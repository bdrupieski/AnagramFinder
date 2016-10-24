
-- good matches

SELECT
  A.INVERSE_LCS_LENGTH_TO_TOTAL_LENGTH_RATIO                            AS LCS_RATIO,
  A.EDIT_DISTANCE_TO_LENGTH_RATIO                                       AS EDIT_RATIO,
  A.DIFFERENT_WORD_COUNT_TO_TOTAL_WORD_COUNT_RATIO                      AS COUNT_RATIO,
  A.IS_SAME_REARRANGED                                                  AS SAME_REARRANGED,
  A.INTERESTING_FACTOR                                                  AS INTERESTING,
  T1.ORIGINAL_TEXT                                                      AS T1_ORIG,
  T2.ORIGINAL_TEXT                                                      AS T2_ORIG,
  CONCAT('http://twitter.com/', T1.USER_NAME, '/status/', T1.STATUS_ID) AS TWEET1_URL,
  CONCAT('http://twitter.com/', T2.USER_NAME, '/status/', T2.STATUS_ID) AS TWEET2_URL,
  T1.CREATED_AT                                                         AS T1_CREATED,
  T2.CREATED_AT                                                         AS T2_CREATED
FROM
  ANAGRAM_MATCHES A
  INNER JOIN TWEETS T1 ON A.TWEET1_ID = T1.ID
  INNER JOIN TWEETS T2 ON A.TWEET2_ID = T2.ID
ORDER BY
  A.INTERESTING_FACTOR DESC;

-- recent good matches

SELECT
  A.INVERSE_LCS_LENGTH_TO_TOTAL_LENGTH_RATIO                            AS LCS_RATIO,
  A.EDIT_DISTANCE_TO_LENGTH_RATIO                                       AS EDIT_RATIO,
  A.DIFFERENT_WORD_COUNT_TO_TOTAL_WORD_COUNT_RATIO                      AS COUNT_RATIO,
  A.IS_SAME_REARRANGED                                                  AS SAME_REARRANGED,
  A.INTERESTING_FACTOR                                                  AS INTERESTING,
  T1.ORIGINAL_TEXT                                                      AS T1_ORIG,
  T2.ORIGINAL_TEXT                                                      AS T2_ORIG,
  CONCAT('http://twitter.com/', T1.USER_NAME, '/status/', T1.STATUS_ID) AS TWEET1_URL,
  CONCAT('http://twitter.com/', T2.USER_NAME, '/status/', T2.STATUS_ID) AS TWEET2_URL,
  T1.CREATED_AT                                                         AS T1_CREATED,
  T2.CREATED_AT                                                         AS T2_CREATED
FROM
  anagram_matches A
  INNER JOIN TWEETS T1 ON A.TWEET1_ID = T1.ID
  INNER JOIN TWEETS T2 ON A.TWEET2_ID = T2.ID
WHERE
  A.inverse_lcs_length_to_total_length_ratio > 0.4 AND
  A.different_word_count_to_total_word_count_ratio > 0.4 AND
  A.edit_distance_to_length_ratio > 0.4
ORDER BY
  A.date_created desc;