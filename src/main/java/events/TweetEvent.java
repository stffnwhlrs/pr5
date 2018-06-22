package events;

public class TweetEvent {
    public  long _id;
    public String _text;
    public long _userId;
    public String _screenName;
    public String _name;
    public String _fullText;


    public TweetEvent(long id, String text, long userId, String screenName, String name, String fullText) {
        this._id = id;
        this._text = text;
        this._userId = userId;
        this._screenName = screenName;
        this._name = name;
        this._fullText = fullText;

    }

    public TweetEvent() {}
}