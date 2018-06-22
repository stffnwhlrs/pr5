package reducers;

import events.TweetCount;
import events.TweetCountHillaryClintonPositive;
import org.apache.flink.api.common.functions.ReduceFunction;

public class TweetCountHillaryClintonPositiveReducer implements ReduceFunction<TweetCount> {
    @Override
    public TweetCount reduce(TweetCount anzahlTweetsEvent, TweetCount t1) {
        return new TweetCountHillaryClintonPositive(anzahlTweetsEvent._sum + t1._sum );
    }
}
