package adapters;

import events.TweetEvent;
import org.apache.flink.api.common.functions.MapFunction;

public class TweetOutAdapter implements MapFunction<TweetEvent, String> {
    @Override
    public String map(TweetEvent tweetEvent) {
        return "id: " + tweetEvent._id +
                "; text: " + tweetEvent._text +
                "; userId: " + tweetEvent._userId +
                "; screenName: " + tweetEvent._screenName +
                "; name: " + tweetEvent._name +
                "; fullText: " +  tweetEvent._fullText;
    }
}
