
import adapters.*;
import conditions.FilterNegative;
import conditions.FilterPositive;
import conditions.FilterHillaryClinton;
import conditions.FilterTrump;
import events.*;
import mappers.TweetCountHillaryClintonNegativeMapper;
import mappers.TweetCountHillaryClintonPositiveMapper;
import mappers.TweetCountTrumpNegativeMapper;
import mappers.TweetCountTrumpPositiveMapper;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternFlatSelectFunction;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.AllWindowFunction;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer08;
import org.apache.flink.streaming.connectors.twitter.TwitterSource;
import org.apache.flink.util.Collector;
import reducers.TweetCountHillaryClintonNegativeReducer;
import reducers.TweetCountHillaryClintonPositiveReducer;
import reducers.TweetCountTrumpNegativeReducer;
import reducers.TweetCountTrumpPositiveReducer;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class Main {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // Einstellungen für die Twitter Verbindung
        Properties props = new Properties();
        props.setProperty(TwitterSource.CONSUMER_KEY, "qzNlqrFckPFTsp6bQchKlhI3o");
        props.setProperty(TwitterSource.CONSUMER_SECRET, "mQMxCb2mb9NBkxZ28ozV9HV2oyEwfkVdSYDJgOPa6suGKPvBDZ");
        props.setProperty(TwitterSource.TOKEN, "1006501086100942848-uvX2b6VJq0y5rzZLmNlKQSadbqRJGY");
        props.setProperty(TwitterSource.TOKEN_SECRET, "0O4CvsXeKUof13lkPFaLIwdG8pD7MtlOWq6ES9gyULR6A");

        // Twitter Source erzeugen und nur Tweets erhalten die im Tweetfilter spezifiziert sind.
        TwitterSource twitterSource = new TwitterSource(props);
        TweetFilter customFilterInitializer = new TweetFilter(Arrays.asList("Trump", "Hillary Clinton"));
        twitterSource.setCustomEndpointInitializer(customFilterInitializer);
        DataStream<String> twitterStream = env.addSource(twitterSource);

        // In-Adapter
        DataStream<TweetEvent> tweetStream = twitterStream
                .flatMap(new TwitterInAdapter());


        // ****************** Count All ********************************
        DataStream<String> countAllDataStream = tweetStream
                .windowAll(TumblingProcessingTimeWindows.of(Time.seconds(30)))
                .apply(new AllWindowFunction<TweetEvent, String, TimeWindow>() {
                    @Override
                    public void apply(TimeWindow timeWindow, Iterable<TweetEvent> events, Collector<String> out) throws Exception {
                        long sum = 0;

                        for (TweetEvent event : events) {
                            sum += 1;
                        }
                        out.collect("Empfange Tweets in den letzten 30 Sekunden: " + sum);
                    }
                });
        // ****************** Count All ********************************


        // ****************** Filter Trump ********************************
        // Pattern für Trump
        Pattern<TweetEvent, ?> trumpPattern = Pattern.<TweetEvent>begin("start")
                .where(new FilterTrump());

        // Pattern anwenden auf Stream
        PatternStream<TweetEvent> tweetTrumpPatternStream = CEP.pattern(tweetStream, trumpPattern);

        // Neuer Stream für Ergebnis
        DataStream<TweetEvent> tweetsFilteredTrumpDataStream = tweetTrumpPatternStream.select(
                new PatternSelectFunction<TweetEvent, TweetEvent>() {
                    @Override
                    public TweetEvent select(Map<String, List<TweetEvent>> pattern) throws Exception {
                        return (TweetEvent) pattern.get("start").get(0);
                    }
                }
        );
        // ****************** /Filter Trump ********************************

        // ****************** Filter Hillary Clinton ********************************
        Pattern<TweetEvent, ?> hillaryClintonPattern = Pattern.<TweetEvent>begin("start")
                .where(new FilterHillaryClinton());

        // Pattern anwenden auf Stream
        PatternStream<TweetEvent> tweetsHillaryClintonPatternStream = CEP.pattern(tweetStream, hillaryClintonPattern);

        // Neuer Stream für Ergebnis
        DataStream<TweetEvent> tweetsFilteredHillaryClintonDataStream = tweetsHillaryClintonPatternStream.select(
                new PatternSelectFunction<TweetEvent, TweetEvent>() {
                    @Override
                    public TweetEvent select(Map<String, List<TweetEvent>> pattern) throws Exception {
                        return pattern.get("start").get(0);
                    }
                }
        );
        // ****************** /Filter Hillary Clinton ********************************

        // ****************** Filter Trump Positive Negative ********************************
        Pattern<TweetEvent, ?> trumpPositivePattern = Pattern.<TweetEvent>begin("start")
                .where(new FilterPositive());

        PatternStream<TweetEvent> tweetsTrumpPositivePatternStream = CEP.pattern(tweetsFilteredTrumpDataStream, trumpPositivePattern);

        DataStream<TweetEvent> tweetsFilteredTrumpPositiveDataStream = tweetsTrumpPositivePatternStream.select(
                new PatternSelectFunction<TweetEvent, TweetEvent>() {
                    @Override
                    public TweetEvent select(Map<String, List<TweetEvent>> pattern) throws Exception {
                        return pattern.get("start").get(0);
                    }
                }
        );

        Pattern<TweetEvent, ? > trumpNegativePattern = Pattern.<TweetEvent>begin("start")
                .where(new FilterNegative());

        PatternStream<TweetEvent> tweetsTrumpNegativePatternStream = CEP.pattern(tweetsFilteredTrumpDataStream, trumpNegativePattern);

        DataStream<TweetEvent> tweetsFilteredTrumpNegativeDataStream = tweetsTrumpNegativePatternStream.select(
                new PatternSelectFunction<TweetEvent, TweetEvent>() {
                    @Override
                    public TweetEvent select(Map<String, List<TweetEvent>> pattern) throws Exception {
                        return pattern.get("start").get(0);
                    }
                }
        );
        // ****************** /Filter Trump Positive/Negative ********************************

        // ****************** Filter Hillary Clinton Positive/Negative ********************************
        Pattern<TweetEvent, ?> hillaryClintonPositivePattern = Pattern.<TweetEvent>begin("start")
                .where(new FilterPositive());

        PatternStream<TweetEvent> tweetsHillaryClintonPositivePatternStream = CEP.pattern(tweetsFilteredHillaryClintonDataStream, hillaryClintonPositivePattern);

        DataStream<TweetEvent> tweetsFilteredHillaryClintonPositiveDataStream = tweetsHillaryClintonPositivePatternStream.select(
                new PatternSelectFunction<TweetEvent, TweetEvent>() {
                    @Override
                    public TweetEvent select(Map<String, List<TweetEvent>> pattern) throws Exception {
                        return pattern.get("start").get(0);
                    }
                }
        );

        Pattern<TweetEvent, ? > hillaryClintonNegativePattern = Pattern.<TweetEvent>begin("start")
                .where(new FilterNegative());

        PatternStream<TweetEvent> tweetsHillaryClintonNegativePatternStream = CEP.pattern(tweetsFilteredHillaryClintonDataStream, hillaryClintonNegativePattern);

        DataStream<TweetEvent> tweetsFilteredHillaryClintonNegativeDataStream = tweetsHillaryClintonNegativePatternStream.select(
                new PatternSelectFunction<TweetEvent, TweetEvent>() {
                    @Override
                    public TweetEvent select(Map<String, List<TweetEvent>> pattern) throws Exception {
                        return (TweetEvent) pattern.get("start").get(0);
                    }
                }
        );
        // ****************** /Filter Trump Positive/Negative ********************************


        // ****************** Counts ********************************

        DataStream<TweetCount> tweetsCountTrumpPositiveDataStream = tweetsFilteredTrumpPositiveDataStream
                .map( new TweetCountTrumpPositiveMapper())
                .keyBy("_key")
                .window(TumblingProcessingTimeWindows.of(Time.seconds(10)))
                .reduce(new TweetCountTrumpPositiveReducer());

        DataStream<TweetCount> tweetsCountTrumpNegativeDataStream = tweetsFilteredTrumpNegativeDataStream
                .map(new TweetCountTrumpNegativeMapper())
                .keyBy("_key")
                .window(TumblingProcessingTimeWindows.of(Time.seconds(10)))
                .reduce(new TweetCountTrumpNegativeReducer());

        DataStream<TweetCount> tweetsCountHillaryClintonPositiveDataStream = tweetsFilteredHillaryClintonPositiveDataStream
                .map(new TweetCountHillaryClintonPositiveMapper())
                .keyBy("_key")
                .window(TumblingProcessingTimeWindows.of(Time.seconds(10)))
                .reduce(new TweetCountHillaryClintonPositiveReducer());

        DataStream<TweetCount> tweetsCountHillaryClintonNegativeDataStream = tweetsFilteredHillaryClintonNegativeDataStream
                .map(new TweetCountHillaryClintonNegativeMapper())
                .keyBy("_key")
                .window(TumblingProcessingTimeWindows.of(Time.seconds(10)))
                .reduce(new TweetCountHillaryClintonNegativeReducer());

        // ****************** /Counts ********************************


        // ****************** Count Aggregation ********************************
        DataStream<TweetCount> aggregatedTweetCountDataStream = tweetsCountTrumpPositiveDataStream
                .union(tweetsCountTrumpNegativeDataStream,
                        tweetsCountHillaryClintonPositiveDataStream,
                        tweetsCountHillaryClintonNegativeDataStream);
        // ****************** /Count Aggregation ********************************

        // ****************** /Count Aggregation ********************************

        // ****************** Count Aggregation ********************************
        DataStream<TweetComparisonEvent> comparisonDataStream = aggregatedTweetCountDataStream
                .windowAll(TumblingProcessingTimeWindows.of(Time.seconds(30)))
                .apply(new AllWindowFunction<TweetCount, TweetComparisonEvent, TimeWindow>() {
                    @Override
                    public void apply(TimeWindow timeWindow, Iterable<TweetCount> events,
                                      Collector<TweetComparisonEvent> out) throws Exception {
                        long tp = 0;
                        long tn = 0;
                        long hcp = 0;
                        long hcn = 0;

                        for (TweetCount event : events) {
                            if ( event instanceof TweetCountTrumpPositive) {
                                tp += event._sum;
                            } else if ( event instanceof TweetCountTrumpNegative) {
                                tn += event._sum;
                            } else if ( event instanceof TweetCountHillaryClintonPositive) {
                                hcp += event._sum;
                            } else if ( event instanceof TweetCountHillaryClintonNegative) {
                                hcn += event._sum;
                            }
                        }
                        out.collect(new TweetComparisonEvent(String.valueOf(tp), String.valueOf(tn), String.valueOf(hcp), String.valueOf(hcn)));
                    }

                });
        // ****************** Great Page Alert ********************************

        Pattern<TweetEvent, ?> greatPagePattern = Pattern.<TweetEvent>begin("first")
                .where(new FilterPositive())
                .next("second")
                .where(new FilterPositive())
                .next("third")
                .where(new FilterPositive())
                .within(Time.minutes(30));

        PatternStream<TweetEvent> greatPagePatternStream = CEP.pattern(
                tweetsFilteredTrumpPositiveDataStream.keyBy("_userId"), greatPagePattern);

        DataStream<GreatPageAlert> greatPageAlertDataStream = greatPagePatternStream.select(
                new PatternSelectFunction<TweetEvent, GreatPageAlert>() {
                    @Override
                    public GreatPageAlert select(Map<String, List<TweetEvent>> pattern) throws Exception {
                        TweetEvent first = pattern.get("first").get(0);
                        TweetEvent second = pattern.get("second").get(0);
                        TweetEvent third = pattern.get("third").get(0);

                        List <String> tweets = Arrays.asList(first._text, second._text, third._text);
                        List <String> fullTweets = Arrays.asList(first._fullText, second._fullText, third._fullText);

                        return new GreatPageAlert(first._screenName, tweets, fullTweets);
                    }
                }
        );
        // ****************** /Great Page Alert ********************************


        // ****************** Fake News Alert ********************************

        Pattern<TweetCount, ? > fakeNewsPattern = Pattern.<TweetCount>begin("first")
                .next("second")
                .next("third");

        PatternStream<TweetCount> fakeNewsPatternStream = CEP.pattern(tweetsCountTrumpNegativeDataStream, fakeNewsPattern);

        DataStream<FakeNewsAlert> fakeNewsAlertDataStream = fakeNewsPatternStream.flatSelect(
                new PatternFlatSelectFunction<TweetCount, FakeNewsAlert>() {
                    @Override
                    public void flatSelect(Map<String, List<TweetCount>> pattern, Collector<FakeNewsAlert> out) throws Exception {
                        TweetCount first = pattern.get("first").get(0);
                        TweetCount second = pattern.get("second").get(0);
                        TweetCount third = pattern.get("third").get(0);

                        if ((first._sum < second._sum) && (second._sum < third._sum)) {
                            out.collect(new FakeNewsAlert(first._sum, second._sum, third._sum));
                        }
                    }
                }
        );
        // ****************** /Fake News Alert ********************************


        // ****************** Out-Adapter ********************************
        // Out Adapter  und Behandlung pure Tweets
        DataStream<String> tweetOutStream = tweetsFilteredTrumpPositiveDataStream
                // .union(tweetsFilteredHillaryClintonDataStream)
                .map(new TweetOutAdapter());
        // tweetOutStream.print();

        //Out Adapter GreatPageAlert
        DataStream<String> greatPageAlertOutStream = greatPageAlertDataStream
                .map(new GreatPageAlertOutAdapter());
        // greatPageAlertOutStream.print();

        //Out Adapter FakeNewsAlert
        DataStream<String> fakeNewsAlertOutStream = fakeNewsAlertDataStream
                .map(new FakeNewsAlertOutAdapter());

        DataStream<String> comparisonOutStream= comparisonDataStream
                .map(new TweetComparisonOutAdapter());
        // ****************** /Out-Adapter ********************************

        greatPageAlertOutStream.print();
        fakeNewsAlertOutStream.print();
        countAllDataStream.print();
        comparisonOutStream.print();

        greatPageAlertOutStream.addSink(new FlinkKafkaProducer08<String>("localhost:9092", "great-page-alert", new SimpleStringSchema()));
        fakeNewsAlertOutStream.addSink(new FlinkKafkaProducer08<String>("localhost:9092", "fake-news-alert", new SimpleStringSchema()));
        countAllDataStream.addSink(new FlinkKafkaProducer08<String>("localhost:9092", "count-all-tweets", new SimpleStringSchema()));
        comparisonOutStream.addSink(new FlinkKafkaProducer08<String>("localhost:9092", "tweet-comparison", new SimpleStringSchema()));

        env.execute();
    }

}

