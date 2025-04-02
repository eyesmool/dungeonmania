package dungeonmania.entities.logicStrategy;


import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.entities.Entity;
import dungeonmania.entities.electricals.CurrentEmitter;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class ANDLogicStrategy extends LogicStrategy {


    @Override
    public boolean isStratSatisfied(Entity entity, GameMap map) {
        List<Position> adjacentPositions = entity.getPosition().getCardinallyAdjacentPositions();

        // Collect all active adjacent conductors
        List<CurrentEmitter> activeAdjacentEmitters = adjacentPositions.stream()
            .flatMap(pos -> map.getEntities(pos).stream())
            .filter(e -> e instanceof CurrentEmitter)
            .map(e -> (CurrentEmitter) e)
            .filter(CurrentEmitter::isLive)
            .collect(Collectors.toList());

        List<CurrentEmitter> adjacentEmitters = adjacentPositions.stream()
        .flatMap(pos -> map.getEntities(pos).stream())
        .filter(e -> e instanceof CurrentEmitter)
        .map(e -> (CurrentEmitter) e)
        .filter(e -> e instanceof CurrentEmitter)
        .collect(Collectors.toList());

        // Check if there are at least 2 active adjacent conductors and all of them are live
        return activeAdjacentEmitters.size() >= 2 && adjacentEmitters.stream().allMatch(CurrentEmitter::isLive);
    }
}

