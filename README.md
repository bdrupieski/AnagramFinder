This project finds anagrams on Twitter. 

It consists of two Java applications.

**TweetStream** connects to the Twitter firehose and 
publishes tweets using a publish-subscribe pattern.

**AnagramListener** listens to the tweets published by TweetStream and
saves them to a PostgreSQL database. For each new incoming tweet
AnagramListener searches the database for other tweets that would
form an anagram with the incoming tweet. If it finds an match,
it scores the match and saves it if it meets a score threshold.

I wrote up the initial process of developing a way to score anagram
matches in a blog post 
[here](http://blog.briandrupieski.com/finding-anagrams-on-twitter).

I'm currently working on a Node.js Express web app to review 
and retweet matches. The repository is
[here](https://github.com/bdrupieski/AnagramReviewer).

