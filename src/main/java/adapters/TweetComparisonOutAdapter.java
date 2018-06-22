package adapters;

import events.TweetComparisonEvent;
import events.TweetEvent;
import org.apache.flink.api.common.functions.MapFunction;

public class TweetComparisonOutAdapter implements MapFunction<TweetComparisonEvent, String> {
    @Override
    public String map(TweetComparisonEvent tweetComparisonEvent) {
        return "{\"_id\": \"" + tweetComparisonEvent._id + "\", \"tp\": \"" + tweetComparisonEvent.tp + "\", \"tn\": \"" + tweetComparisonEvent.tn + "\", \"hcp\": \""+ tweetComparisonEvent.hcp + "\", \"hcn\": \""+ tweetComparisonEvent.hcn + "\"}";
    }
}
