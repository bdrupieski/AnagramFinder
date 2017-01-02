This project finds anagrams on Twitter. It consists of two Java applications.

**TweetStream** connects to the Twitter firehose and 
publishes tweets on a local TCP socket using ZeroMQ.

**AnagramListener** listens to the tweets published by TweetStream and
saves them to a PostgreSQL database. For each new incoming tweet
AnagramListener searches the database for other tweets that would
form an anagram with the incoming tweet. If it finds an match,
it scores the match and saves it if it meets a score threshold.

If you're interested in learning more about how this is done or the
scoring methodology used to rank anagrams I wrote 
[a blog post about it here](http://blog.briandrupieski.com/finding-anagrams-on-twitter).

I also wrote a 
[web app using Node.js](https://github.com/bdrupieski/AnagramReviewer) to 
review and retweet anagrams found by this project.

