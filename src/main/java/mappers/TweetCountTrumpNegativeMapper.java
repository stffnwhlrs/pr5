package mappers;

import events.TweetCount;
import events.TweetCountTrumpNegative;
import events.TweetEvent;
import org.apache.flink.api.common.functions.MapFunction;

public class TweetCountTrumpNegativeMapper implements MapFunction<TweetEvent, TweetCount> {
    @Override
    public TweetCountTrumpNegative map(TweetEvent tweetEvent) {
        return new TweetCountTrumpNegative(1);
    }
}
