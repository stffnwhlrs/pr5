package conditions;

import events.TweetEvent;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;

public class FilterPositive extends SimpleCondition<TweetEvent> {
    @Override
    public boolean filter(TweetEvent tweetEvent) {
        return tweetEvent._text.contains("good") ||
                tweetEvent._text.contains("great") ||
                tweetEvent._fullText.contains("good") ||
                tweetEvent._fullText.contains("great");
    }
}
