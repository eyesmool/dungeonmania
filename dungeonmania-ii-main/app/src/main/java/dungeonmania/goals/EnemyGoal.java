package dungeonmania.goals;

import java.util.List;

import dungeonmania.Game;
import dungeonmania.entities.Player;
import dungeonmania.entities.enemies.ZombieToastSpawner;
import dungeonmania.map.GameMap;

public class EnemyGoal extends Goal {
    private int enemyGoal;

    public EnemyGoal(int enemyGoal) {
        super("enemies");
        this.enemyGoal = enemyGoal;
    }

    @Override
    public boolean achieved(Game game) {
        Player player = game.getPlayer();
        return (player.getNumKills() == enemyGoal) && allSpawnersDestroyed(game);
    }

    @Override
    public String toString(Game game) {
        return achieved(game) ? "" : ":enemies";
    }

    private boolean allSpawnersDestroyed(Game game) {
        GameMap map = game.getMap();
        List<ZombieToastSpawner> listOfSpawners = map.getEntities(ZombieToastSpawner.class);
        return listOfSpawners.stream().allMatch(ZombieToastSpawner::isDestroyed);
    }
}
