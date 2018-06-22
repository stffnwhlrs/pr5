package events;

import java.util.List;

public class GreatPageAlert {
    public String _screenName;
    public List<String> _tweets;
    public List<String> _fullTweets;

    public GreatPageAlert(String screenName, List<String> tweets, List<String> fullTweets) {
        this._screenName = screenName;
        this._tweets = tweets;
        this._fullTweets = fullTweets;
    }

    public GreatPageAlert() {};

    @Override
    public String toString() {
        return "Great Page! Check out: @" + this._screenName + "!!" + this._tweets + this._fullTweets;
    }
}
