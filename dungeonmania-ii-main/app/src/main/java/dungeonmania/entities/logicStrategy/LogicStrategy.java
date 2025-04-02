package dungeonmania.entities.logicStrategy;
import dungeonmania.entities.Entity;
import dungeonmania.map.GameMap;

public abstract class LogicStrategy {
    public abstract boolean isStratSatisfied(Entity entity, GameMap map);
}
