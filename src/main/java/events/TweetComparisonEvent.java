package events;

import org.bson.Document;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class TweetComparisonEvent {
    public String _id;
    public String tp;
    public String tn;
    public String hcp;
    public String hcn;

    public TweetComparisonEvent(String tp, String tn, String hcp, String hcn) {
        this._id = Timestamp.valueOf(LocalDateTime.now()).toString();
        this.tp = String.valueOf(tp);
        this.tn = String.valueOf(tn);
        this.hcp = String.valueOf(hcp);
        this.hcn = String.valueOf(hcn);
    }

    public TweetComparisonEvent() {}

    public Document getTweetComparisonEventAsDocument() {
        Document tweetComparisonEventDocument = new Document("_id", this._id.toString())
                .append("trump_positive", this.tp)
                .append("trump_negative", this.tn)
                .append("hillary_clinton_positive", this.hcp)
                .append("hillary_clinton_negative", this.hcn);

                return tweetComparisonEventDocument;
    }

    @Override
    public String toString() {
        return "TweetComparisonEvent Object: {" +
                "_id=" + this._id +
                ", tp='" + this.tp + '\'' +
                ", tn='" + this.tn + '\'' +
                ", hcp='" + this.hcp + '\'' +
                ", hcn=" + this.hcn +
                '}';
    }
}
