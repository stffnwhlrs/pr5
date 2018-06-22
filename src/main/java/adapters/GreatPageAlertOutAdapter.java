package adapters;

import events.GreatPageAlert;
import org.apache.flink.api.common.functions.MapFunction;

public class GreatPageAlertOutAdapter implements MapFunction<GreatPageAlert, String> {
    @Override
    public String map(GreatPageAlert greatPageAlert) {
        return greatPageAlert.toString();
    }
}
