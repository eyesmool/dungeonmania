package dungeonmania.entities.electricals;

import java.time.LocalTime;

public interface CurrentEmitter {
    public boolean isLive();
    public LocalTime getTimeOn();
}
