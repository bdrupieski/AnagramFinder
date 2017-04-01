This project finds anagrams on Twitter.

It uses Twitter4J to stream tweets from Twitter. If tweets meet some basic filters
they're saved to a Postgres database and checked to see if they form an anagram
with any previously encountered tweets.

If you're interested in learning more about how this is done or the
scoring methodology used to rank anagrams I wrote 
[a blog post about it here](http://blog.briandrupieski.com/finding-anagrams-on-twitter).

I also wrote a 
[web app using Node.js](https://github.com/bdrupieski/AnagramReviewer) to 
review and retweet anagrams found by this project.

