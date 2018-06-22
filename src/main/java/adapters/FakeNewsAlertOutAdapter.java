package adapters;

import events.FakeNewsAlert;
import org.apache.flink.api.common.functions.MapFunction;

public class FakeNewsAlertOutAdapter implements MapFunction<FakeNewsAlert, String> {
    @Override
    public String map(FakeNewsAlert fakeNewsAlert) {
        return fakeNewsAlert.toString();
    }
}
