package dungeonmania.entities.electricals;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Switch;
import dungeonmania.entities.destroyable.Destroyable;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Wire extends Entity implements Conductor, CurrentEmitter, Destroyable {
    private boolean isLive = false;
    private LocalTime timeOn = null;
    private List<Switch> sources = new ArrayList<>();

    public Wire(Position position) {
        super(position);
    }

    @Override
    public void update(Switch source, boolean state) {
        if (state && timeOn == null) {
            timeOn = LocalTime.now();
            sources.add(source);
            this.isLive = state;
        } else if (!state && !areSourcesLive()) {
            timeOn = null;
            sources.remove(source);
            this.isLive = state;
        }
    }

    public boolean isLive() {
        return isLive;
    }

    @Override
    public LocalTime getTimeOn() {
        return timeOn;
    }

    public void setTimeOn(LocalTime timeOn) {
        this.timeOn = timeOn;
    }

    @Override
    public boolean areSourcesLive() {
        return sources.stream()
            .anyMatch(Switch::isActivated);
    }

    public List<Switch> getSources() {
        return sources;
    }

    @Override
    public void onDestroy(GameMap gameMap) {
        List<Switch> sources = getSources();
        sources.stream().forEach(s -> s.removeConductor(this));
    }

    @Override
    public void deactivate() {
        isLive = false;
    }

}
