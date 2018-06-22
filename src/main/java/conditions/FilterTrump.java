package conditions;

import events.TweetEvent;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;

public class FilterTrump extends SimpleCondition<TweetEvent> {
    @Override
    public boolean filter(TweetEvent tweetEvent) {
        return tweetEvent._text.contains("Trump") ||
                tweetEvent._fullText.contains("Trump");
    }
}
