package dungeonmania.entities.logicStrategy;


import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.entities.Entity;
import dungeonmania.entities.electricals.CurrentEmitter;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class COANDLogicStrategy extends LogicStrategy {

    @Override
    public boolean isStratSatisfied(Entity entity, GameMap map) {
        List<Position> adjacentPositions = entity.getPosition().getCardinallyAdjacentPositions();
        return adjacentPositions.stream()
                .flatMap(pos -> map.getEntities(pos).stream())
                .filter(e -> e instanceof CurrentEmitter)
                .map(e -> (CurrentEmitter) e)
                .allMatch(CurrentEmitter::isLive)
                &&
                adjConductorsSynchronised(entity, map);
    }

   private boolean adjConductorsSynchronised(Entity entity, GameMap map) {
        List<Position> adjacentPositions = entity.getPosition().getCardinallyAdjacentPositions();
        return adjacentPositions.stream()
            .flatMap(pos -> map.getEntities(pos).stream())
            .filter(e -> e instanceof CurrentEmitter)
            .map(e -> (CurrentEmitter) e)
            .collect(Collectors.collectingAndThen(Collectors.toList(), list -> {
                for (CurrentEmitter c : list) {
                    System.out.println(c.getClass().toString() + c.getTimeOn());
                }
                if (list.isEmpty()) {
                    return false;
                }
                LocalTime firstConductorTime = list.get(0).getTimeOn();
                return list.stream().allMatch(
                    conductor -> conductor
                    .getTimeOn().truncatedTo(ChronoUnit.SECONDS)
                    .equals(firstConductorTime.truncatedTo(ChronoUnit.SECONDS))
                );
            }));
    }

}
