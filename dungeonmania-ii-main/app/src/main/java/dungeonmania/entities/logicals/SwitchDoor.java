package dungeonmania.entities.logicals;

import dungeonmania.entities.Entity;
import dungeonmania.entities.logicStrategy.LogicStrategy;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class SwitchDoor extends LogicalEntity {

    public SwitchDoor(Position position, LogicStrategy strategy) {
        super(position, strategy);
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return isActivated();
    }
}
