package dungeonmania.entities.enemies.movement;

import dungeonmania.Game;
import dungeonmania.entities.Player;
import dungeonmania.entities.enemies.Enemy;
import dungeonmania.entities.enemies.Mercenary;
import dungeonmania.entities.enemies.Assassin;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;


public class AllyStrategy implements MovementStrategy {

    public void move(Game game, Enemy enemy) {
        Position nextPos;
        GameMap map = game.getMap();
        Player player = game.getPlayer();
        if (enemy instanceof Mercenary) {
            Mercenary mercenary = (Mercenary) enemy;
            boolean isAdjacentToPlayer = mercenary.isAdjacentToPlayer();
            nextPos = isAdjacentToPlayer ? player.getPreviousDistinctPosition()
                        : map.dijkstraPathFind(enemy.getPosition(), player.getPosition(), enemy);
                if (!isAdjacentToPlayer && Position.isAdjacent(player.getPosition(), nextPos))
                   mercenary.setIsAdjacentToPlayer(true);
                map.moveTo(enemy, nextPos);
        } else if (enemy instanceof Assassin) {
            Assassin assassin = (Assassin) enemy;
            boolean isAdjacentToPlayer = assassin.isAdjacentToPlayer();
            nextPos = isAdjacentToPlayer ? player.getPreviousDistinctPosition()
                        : map.dijkstraPathFind(enemy.getPosition(), player.getPosition(), enemy);
                if (!isAdjacentToPlayer && Position.isAdjacent(player.getPosition(), nextPos))
                   assassin.setIsAdjacentToPlayer(true);
                map.moveTo(enemy, nextPos);
        }
    }
}
