package conditions;

import events.TweetEvent;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;

public class FilterHillaryClinton extends SimpleCondition<TweetEvent> {
    @Override
    public boolean filter(TweetEvent tweetEvent) {
        return tweetEvent._text.contains("Hillary Clinton") ||
                tweetEvent._fullText.contains("Hillary Clinton");
    }
}
