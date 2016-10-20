CREATE TABLE TWEETS (
  ID                   UUID      NOT NULL PRIMARY KEY,
  STATUS_ID            BIGINT    NOT NULL,
  CREATED_AT           TIMESTAMP NOT NULL,
  ORIGINAL_TEXT        TEXT      NOT NULL,
  STRIPPED_SORTED_TEXT TEXT      NOT NULL,
  USER_ID              BIGINT    NOT NULL,
  USER_NAME            TEXT      NOT NULL
);

CREATE INDEX STRIPPED_SORTED_TEXT_INDEX
  ON TWEETS (STRIPPED_SORTED_TEXT);

CREATE TABLE ANAGRAM_MATCHES (
  ID                                             SERIAL                              NOT NULL PRIMARY KEY,
  TWEET1_ID                                      UUID                                NOT NULL REFERENCES TWEETS (ID) ON UPDATE RESTRICT ON DELETE RESTRICT,
  TWEET2_ID                                      UUID                                NOT NULL REFERENCES TWEETS (ID) ON UPDATE RESTRICT ON DELETE RESTRICT,
  EDIT_DISTANCE_ORIGINAL_TEXT                    INTEGER                             NOT NULL,
  EDIT_DISTANCE_STRIPPED_TEXT                    INTEGER                             NOT NULL,
  HAMMING_DISTANCE_STRIPPED_TEXT                 INTEGER                             NOT NULL,
  LONGEST_COMMON_SUBSTRING_LENGTH_STRIPPED_TEXT  INTEGER                             NOT NULL,
  WORD_COUNT_DIFFERENCE                          INTEGER                             NOT NULL,
  TOTAL_WORDS                                    INTEGER                             NOT NULL,
  INVERSE_LCS_LENGTH_TO_TOTAL_LENGTH_RATIO       REAL                                NOT NULL,
  EDIT_DISTANCE_TO_LENGTH_RATIO                  REAL                                NOT NULL,
  DIFFERENT_WORD_COUNT_TO_TOTAL_WORD_COUNT_RATIO REAL                                NOT NULL,
  IS_SAME_REARRANGED                             INTEGER                             NOT NULL,
  INTERESTING_FACTOR                             REAL                                NOT NULL,
  REJECTED                                       BOOLEAN DEFAULT FALSE               NOT NULL,
  TWEET1_RETWEET_ID                              BIGINT                              NULL,
  TWEET2_RETWEET_ID                              BIGINT                              NULL,
  DATE_RETWEETED                                 TIMESTAMP                           NULL,
  DATE_CREATED                                   TIMESTAMP DEFAULT current_timestamp NOT NULL
);

CREATE INDEX INTERESTING_FACTOR_INDEX
  ON ANAGRAM_MATCHES (INTERESTING_FACTOR);
CREATE INDEX DATE_CREATED_INDEX
  ON ANAGRAM_MATCHES (date_created);
