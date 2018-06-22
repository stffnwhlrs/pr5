package conditions;

import events.TweetEvent;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;

public class FilterNegative extends SimpleCondition<TweetEvent> {
    @Override
    public boolean filter(TweetEvent tweetEvent) {
        return tweetEvent._text.contains("bad") ||
                tweetEvent._text.contains("worse") ||
                tweetEvent._fullText.contains("bad") ||
                tweetEvent._fullText.contains("worse");
    }
}
