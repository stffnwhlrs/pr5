package events;

import java.util.List;

public class FakeNewsAlert {
    public long _first;
    public long _second;
    public long _third;


    public FakeNewsAlert(long first, long second, long third) {
        this._first = first;
        this._second = second;
        this._third = third;
    }

    public FakeNewsAlert() {}

    @Override
    public String toString() {
        return " Fake News Alert! Anzahl der letzten negativen Tweets: " + this._first + ", " + this._second + ", " + this._third ;
    }
}
