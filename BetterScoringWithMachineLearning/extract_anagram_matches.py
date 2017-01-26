import psycopg2
import csv

conn = psycopg2.connect("dbname=anagrams user=postgres password=postgres")
cur = conn.cursor()
cur.execute(
    """
SELECT
  anagram_matches.id,
  t1.original_text AS t1_original_text,
  t2.original_text AS t2_original_text,
  anagram_matches.interesting_factor,
  anagram_matches.edit_distance_original_text,
  anagram_matches.edit_distance_stripped_text,
  anagram_matches.edit_distance_to_length_ratio,
  anagram_matches.hamming_distance_stripped_text,
  anagram_matches.longest_common_substring_length_stripped_text,
  anagram_matches.inverse_lcs_length_to_total_length_ratio,
  anagram_matches.word_count_difference,
  anagram_matches.different_word_count_to_total_word_count_ratio,
  anagram_matches.english_words_to_total_word_count_ratio,
  anagram_matches.total_words,
  anagram_matches.attempted_approval,
  anagram_matches.rejected,
  anagram_matches.auto_rejected,
  anagram_matches.date_created,
  anagram_matches.date_retweeted,
  anagram_matches.date_unretweeted,
  anagram_matches.date_posted_tumblr,
  anagram_matches.date_rejected,
  anagram_matches.tweet1_retweet_id,
  anagram_matches.tweet2_retweet_id,
  anagram_matches.tumblr_post_id
FROM anagram_matches
  INNER JOIN tweets t1 ON t1.id = anagram_matches.tweet1_id
  INNER JOIN tweets t2 ON t2.id = anagram_matches.tweet2_id
""")

anagramMatchRows = cur.fetchall()

cur.close()
conn.close()

print(anagramMatchRows[:3])

headers = [
    "id",
    "t1_original_text",
    "t2_original_text",
    "interesting_factor",
    "edit_distance_original_text",
    "edit_distance_stripped_text",
    "edit_distance_to_length_ratio",
    "hamming_distance_stripped_text",
    "longest_common_substring_length_stripped_text",
    "inverse_lcs_length_to_total_length_ratio",
    "word_count_difference",
    "different_word_count_to_total_word_count_ratio",
    "english_words_to_total_word_count_ratio",
    "total_words",
    "attempted_approval",
    "rejected",
    "auto_rejected",
    "date_created",
    "date_retweeted",
    "date_unretweeted",
    "date_posted_tumblr",
    "date_rejected",
    "tweet1_retweet_id",
    "tweet2_retweet_id",
    "tumblr_post_id",
]

with open('anagram_matches.csv', 'w', encoding='utf-8', newline='') as csvFile:
    anagramWriter = csv.writer(csvFile)
    anagramWriter.writerow(headers)
    for anagramMatch in anagramMatchRows:
        anagramWriter.writerow(anagramMatch)

parsedRows = []
with open('anagram_matches.csv', 'r', encoding='utf-8', newline='') as csvFile:
    reader = csv.reader(csvFile)
    for row in reader:
        parsedRows.append(row)

print(parsedRows[1:3])
