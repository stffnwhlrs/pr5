package mappers;

import events.TweetCount;
import events.TweetCountTrumpNegative;
import events.TweetCountTrumpPositive;
import events.TweetEvent;
import org.apache.flink.api.common.functions.MapFunction;

public class TweetCountTrumpPositiveMapper implements MapFunction<TweetEvent, TweetCount> {
    @Override
    public TweetCountTrumpPositive map(TweetEvent tweetEvent) {
        return new TweetCountTrumpPositive(1);
    }
}
