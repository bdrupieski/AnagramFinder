import csv
from typing import List


class AnagramMatch:
    def __init__(self, anagram_match_row):
        super().__init__()
        self.id = anagram_match_row[0]  # type: str
        self.t1_original_text = anagram_match_row[1]  # type: str
        self.t2_original_text = anagram_match_row[2]  # type: str
        self.interesting_factor = float(anagram_match_row[3])  # type: float
        self.edit_distance_original_text = int(anagram_match_row[4])  # type: int
        self.edit_distance_stripped_text = int(anagram_match_row[5])  # type: int
        self.edit_distance_to_length_ratio = float(anagram_match_row[6])  # type: float
        self.hamming_distance_stripped_text = int(anagram_match_row[7])  # type: int
        self.longest_common_substring_length_stripped_text = int(anagram_match_row[8])  # type: int
        self.inverse_lcs_length_to_total_length_ratio = float(anagram_match_row[9])  # type: float
        self.word_count_difference = int(anagram_match_row[10])  # type: int
        self.different_word_count_to_total_word_count_ratio = float(anagram_match_row[11])  # type: float
        self.english_words_to_total_word_count_ratio = float(anagram_match_row[12])  # type: float
        self.total_words = int(anagram_match_row[13])  # type: int
        self.attempted_approval = bool(anagram_match_row[14])  # type: bool
        self.rejected = bool(anagram_match_row[15])  # type: bool
        self.auto_rejected = bool(anagram_match_row[16])  # type: bool
        self.date_created = anagram_match_row[17]  # type: str
        self.date_retweeted = anagram_match_row[18]  # type: str
        self.date_unretweeted = anagram_match_row[19]  # type: str
        self.date_posted_tumblr = anagram_match_row[20]  # type: str
        self.date_rejected = anagram_match_row[21]  # type: str
        self.tweet1_retweet_id = anagram_match_row[22]  # type: str
        self.tweet2_retweet_id = anagram_match_row[23]  # type: str
        self.tumblr_post_id = anagram_match_row[24]  # type: str

        self.posted = self.attempted_approval is True and \
            self.date_retweeted is not None and \
            self.date_unretweeted is None  # type: bool

    def __repr__(self, *args, **kwargs):
        return "[(" + self.t1_original_text + "), (" + self.t2_original_text + ")]"


def get_matches_from_csv() -> List[AnagramMatch]:
    match_rows = []
    with open('anagram_matches.csv', 'r', encoding='utf-8', newline='') as csvFile:
        reader = csv.reader(csvFile)
        next(reader)  # skip header
        for row in reader:
            match_rows.append(row)
    matches = list(map(lambda x: AnagramMatch(x), match_rows))
    return matches
