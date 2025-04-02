package dungeonmania.entities.logicStrategy;

import java.util.List;

import dungeonmania.entities.Entity;
import dungeonmania.entities.electricals.CurrentEmitter;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class ORLogicStrategy extends LogicStrategy {

    @Override
    public boolean isStratSatisfied(Entity entity, GameMap map) {
        List<Position> adjacentPositions = entity.getPosition().getCardinallyAdjacentPositions();
            // Check if any adjacent position has an active wire
            return adjacentPositions.stream()
                .flatMap(pos -> map.getEntities(pos).stream())
                .filter(e -> e instanceof CurrentEmitter)
                .map(e -> (CurrentEmitter) e)
                .anyMatch(CurrentEmitter::isLive);
    }


}
