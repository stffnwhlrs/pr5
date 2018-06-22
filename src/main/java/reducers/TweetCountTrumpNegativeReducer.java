package reducers;

import events.TweetCount;
import events.TweetCountTrumpNegative;
import org.apache.flink.api.common.functions.ReduceFunction;

public class TweetCountTrumpNegativeReducer implements ReduceFunction<TweetCount> {
    @Override
    public TweetCount reduce(TweetCount anzahlTweetsEvent, TweetCount t1) {
        return new TweetCountTrumpNegative(anzahlTweetsEvent._sum + t1._sum );
    }
}
