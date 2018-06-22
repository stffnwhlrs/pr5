import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.endpoint.StreamingEndpoint;
import org.apache.flink.streaming.connectors.twitter.TwitterSource;

import java.io.Serializable;
import java.util.List;

public class TweetFilter implements TwitterSource.EndpointInitializer, Serializable {

    private List<String>_terms;

    TweetFilter(List<String> terms) {
        _terms = terms;
    }

    @Override
    public StreamingEndpoint createEndpoint() {
        StatusesFilterEndpoint endpoint = new StatusesFilterEndpoint();
        endpoint.trackTerms(_terms);
        return endpoint;
    }
}