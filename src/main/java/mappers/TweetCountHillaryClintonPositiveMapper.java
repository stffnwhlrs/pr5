package mappers;

import events.TweetCount;
import events.TweetCountHillaryClintonPositive;
import events.TweetEvent;
import org.apache.flink.api.common.functions.MapFunction;

public class TweetCountHillaryClintonPositiveMapper implements MapFunction<TweetEvent, TweetCount> {
    @Override
    public TweetCountHillaryClintonPositive map(TweetEvent tweetEvent) {
        return new TweetCountHillaryClintonPositive(1);
    }
}
