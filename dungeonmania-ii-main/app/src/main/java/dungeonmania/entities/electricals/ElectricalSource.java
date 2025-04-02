package dungeonmania.entities.electricals;


public interface ElectricalSource {
    public void notifyConductors();

    public void registerConductor(Conductor conductor);

    public void removeConductor(Conductor conductor);

}
