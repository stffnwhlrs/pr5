package reducers;

import events.TweetCount;
import events.TweetCountHillaryClintonNegative;
import events.TweetCountHillaryClintonPositive;
import org.apache.flink.api.common.functions.ReduceFunction;

public class TweetCountHillaryClintonNegativeReducer implements ReduceFunction<TweetCount> {
    @Override
    public TweetCount reduce(TweetCount anzahlTweetsEvent, TweetCount t1) {
        return new TweetCountHillaryClintonNegative(anzahlTweetsEvent._sum + t1._sum );
    }
}
