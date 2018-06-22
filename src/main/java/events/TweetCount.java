package events;

public abstract class TweetCount {
    public String _key;
    public long _sum;

    public TweetCount(String key, long sum) {
        this._key = key;
        this._sum = sum;
    }

    public TweetCount() {}

    @Override
    public String toString() {
        return "TweetCount. key: " + this._key + ", sum: " + this._sum;
    }
}
