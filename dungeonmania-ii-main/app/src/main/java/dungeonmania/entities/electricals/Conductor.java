package dungeonmania.entities.electricals;


import dungeonmania.entities.Switch;

public interface Conductor {
    public void update(Switch source, boolean active);
    public void deactivate();
    public boolean areSourcesLive();
}
