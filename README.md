airquality
==========

ElasicSearch based air focusing on air quailty tweets. House submission for Big Mountain Data Conference competition Oct 2014.

You can go to utahairquality.life to see the code in action.

You'll need something to to injest the tweets. The core project uses logstash http://logstash.net/.

This is a sample logstash config for tweet injection.
```javascript
input {
    twitter {
        consumer_key => "xxxxxxxxxxxxxxxxxxxxx"
        consumer_secret => "yyyyyyyyyyyyyyyyyyyyyyyyyyyy"
        oauth_token => "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
        oauth_token_secret => "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb"
        keywords => ["air quality", "beautiful day", "inversion", "bad air", "dirty air", "unhealthy air", "smog", "smoggy"]
        full_tweet => true
        codec => json
    }
}

filter {
}

output {
    elasticsearch {
        cluster => "elasticsearch"
        host => "localhost"
        index => "airquality"
        index_type => "status"
    }
}
```
