package reducers;

import events.TweetCount;
import events.TweetCountTrumpNegative;
import events.TweetCountTrumpPositive;
import org.apache.flink.api.common.functions.ReduceFunction;

public class TweetCountTrumpPositiveReducer implements ReduceFunction<TweetCount> {
    @Override
    public TweetCount reduce(TweetCount anzahlTweetsEvent, TweetCount t1) {
        return new TweetCountTrumpPositive(anzahlTweetsEvent._sum + t1._sum );
    }
}
