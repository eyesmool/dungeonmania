package dungeonmania.entities;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dungeonmania.entities.collectables.Bomb;
import dungeonmania.entities.destroyable.Destroyable;
import dungeonmania.entities.electricals.Conductor;
import dungeonmania.entities.electricals.CurrentEmitter;
import dungeonmania.entities.electricals.ElectricalSource;
import dungeonmania.entities.electricals.Wire;
import dungeonmania.entities.overlappable.Overlappable;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Switch extends Entity implements Overlappable, ElectricalSource, CurrentEmitter, Destroyable {
    private boolean activated;
    private List<Bomb> bombs = new ArrayList<>();
    private List<Conductor> conductors = new ArrayList<>();
    private LocalTime timeOn = null;


    public Switch(Position position) {
        super(position.asLayer(Entity.ITEM_LAYER));
    }

    public void subscribe(Bomb b) {
        bombs.add(b);
    }

    public void subscribe(Bomb bomb, GameMap map) {
        bombs.add(bomb);
        if (activated) {
            bombs.stream().forEach(b -> b.notify(map));
        }
    }

    public void unsubscribe(Bomb b) {
        bombs.remove(b);
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true;
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (entity instanceof Boulder) {
            activated = true;
            timeOn = LocalTime.now();
            bombs.stream().forEach(b -> b.notify(map));
            notifyConductors();
        }
    }

    public void onMovedAway(GameMap map, Entity entity) {
        if (entity instanceof Boulder) {
            activated = false;
            timeOn = null;
        }
    }

    public boolean isActivated() {
        return activated;
    }

    @Override
    public void notifyConductors() {
        if (isActivated()) {
            conductors.stream().forEach(o -> o.update(this, true));
        } else if (!isActivated()) {
            conductors.stream().forEach(o -> o.update(this, false));
        }
    }

    @Override
    public void registerConductor(Conductor conductor) {
        if (!conductors.contains(conductor)) {
            conductors.add(conductor);
        }
    }

    @Override
    public void removeConductor(Conductor conductor) {
        int i = conductors.indexOf(conductor);
        if (i != -1) {
            conductors.remove(i);
            for (int j = i; j < conductors.size(); j++) {
                conductors.get(j).deactivate();
                conductors.remove(j);
            }
        }
    }

    public void updateCircuits(GameMap map, Switch sw) {
        // Initialize a set to track visited conductors
        Set<Conductor> visitedConductors = new HashSet<>();
        // Get all cardinally adjacent positions to the switch
        List<Position> adjacentPositions = sw.getPosition().getCardinallyAdjacentPositions();
        // Perform DFS for each adjacent position
        for (Position pos : adjacentPositions) {
            // Get all entities at the current position
            List<Entity> entitiesAtPosition = map.getEntities(pos);
            for (Entity entity : entitiesAtPosition) {
                // Check if the entity is a Conductor
                if (entity instanceof Wire) {
                    Wire conductor = (Wire) entity;
                    Position conductorPos = entity.getPosition();
                    // Start DFS from this conductor
                    dfsRegisterConductors(sw, conductor, conductorPos, visitedConductors, map);
                }
            }
        }
    }

    /**
     * Perform a DFS to register all conductors connected to the starting conductor.
     *
     * @param conductor The starting conductor for DFS.
     * @param visitedConductors A set to track visited conductors.
     */
    private void dfsRegisterConductors(
        Switch sw, Conductor conductor, Position conductorPos, Set<Conductor> visitedConductors, GameMap map
    ) {
        // If this conductor has already been visited, return
        if (visitedConductors.contains(conductor)) {
            return;
        }
        // Mark this conductor as visited
        visitedConductors.add(conductor);
        // Register the conductor with the switch
        sw.registerConductor(conductor);
        // Get all cardinally adjacent positions to this conductor
        List<Position> adjacentPositions = conductorPos.getCardinallyAdjacentPositions();
        // Check all adjacent positions
        for (Position pos : adjacentPositions) {
            // Get all entities at the current position
            List<Entity> entitiesAtPosition = map.getEntities(pos);
            for (Entity entity : entitiesAtPosition) {
                // Check if the adjacent entity is a Conductor
                if (entity instanceof Wire) {
                    Wire adjacentConductor = (Wire) entity;

                    // Register the adjacent conductor with the current conductor
                    sw.registerConductor(adjacentConductor);
                    Position adjConductorPos = entity.getPosition();
                    // Perform DFS on the adjacent conductor
                    dfsRegisterConductors(sw, adjacentConductor, adjConductorPos, visitedConductors, map);
                }
            }
        }
    }

    @Override
    public boolean isLive() {
        return activated;
    }

    @Override
    public LocalTime getTimeOn() {
        return timeOn;
    }

    @Override
    public void onDestroy(GameMap gameMap) {
        activated = false;
        notifyConductors();
        conductors.stream().forEach(c -> c.update(this, false));
    }
}
