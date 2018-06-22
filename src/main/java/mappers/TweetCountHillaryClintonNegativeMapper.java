package mappers;

import events.TweetCount;
import events.TweetCountHillaryClintonNegative;
import events.TweetCountTrumpNegative;
import events.TweetEvent;
import org.apache.flink.api.common.functions.MapFunction;

public class TweetCountHillaryClintonNegativeMapper implements MapFunction<TweetEvent, TweetCount> {
    @Override
    public TweetCountHillaryClintonNegative map(TweetEvent tweetEvent) {
        return new TweetCountHillaryClintonNegative(1);
    }
}
