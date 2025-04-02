package dungeonmania.entities.enemies.movement;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import dungeonmania.Game;
import dungeonmania.entities.enemies.Enemy;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class RandomStrategy implements MovementStrategy {

    @Override
    public void move(Game game, Enemy enemy) {
        GameMap map = game.getMap();
        Position nextPos;
        Random randGen = new Random();
        Position position = enemy.getPosition();
        List<Position> pos = position.getCardinallyAdjacentPositions();
        pos = pos.stream().filter(p -> map.canMoveTo(enemy, p)).collect(Collectors.toList());
        if (pos.size() == 0) {
            nextPos = enemy.getPosition();
            map.moveTo(enemy, nextPos);
        } else {
            nextPos = pos.get(randGen.nextInt(pos.size()));
            map.moveTo(enemy, nextPos);
        }
    }
}
