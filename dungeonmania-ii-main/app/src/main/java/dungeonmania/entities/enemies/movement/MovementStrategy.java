package dungeonmania.entities.enemies.movement;

import dungeonmania.entities.enemies.Enemy;
import dungeonmania.Game;

public interface MovementStrategy {
    public void move(Game game, Enemy enemy);
}
