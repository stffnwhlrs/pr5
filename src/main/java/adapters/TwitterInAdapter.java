package adapters;

import events.TweetEvent;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.JsonNode;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.util.Collector;

import java.io.IOException;

public class TwitterInAdapter implements FlatMapFunction<String, TweetEvent> {

    private transient ObjectMapper jsonParser;

    @Override
    public void flatMap(String value, Collector<TweetEvent> out) throws IOException {

        if (jsonParser == null) {
            jsonParser = new ObjectMapper();
        }

        JsonNode jsonNode = jsonParser.readValue(value, JsonNode.class);

        if (jsonNode.has("user") &&
                jsonNode.has("id_str") &&
                jsonNode.has("text")) {
            long id = Long.parseLong(jsonNode.get("id_str").asText());
            String text = jsonNode.get("text").asText();
            long userId = Long.parseLong(jsonNode.get("user").get("id_str").asText());
            String screenName = jsonNode.get("user").get("screen_name").asText();
            String name = jsonNode.get("user").get("name").asText();
            String fullText = "";

            if (jsonNode.has("extended_tweet")) {
                fullText = jsonNode.get("extended_tweet").get("full_text").asText();
            }

            out.collect(new TweetEvent(id,text, userId, screenName, name, fullText));
        }
    }
}