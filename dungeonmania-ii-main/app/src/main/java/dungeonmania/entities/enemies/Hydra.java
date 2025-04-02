package dungeonmania.entities.enemies;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.collectables.potions.InvincibilityPotion;
import dungeonmania.entities.enemies.movement.MovementStrategy;
import dungeonmania.entities.enemies.movement.RetreatStrategy;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Hydra extends Enemy {
    public static final double DEFAULT_HEALTH = 5.0;
    public static final double DEFAULT_ATTACK = 6.0;
    public static final double DEFAULT_HEALTH_INCREASE_RATE = 0.3;
    public static final double DEFAULT_HEALTH_INCREASE_AMOUNT = 1.0;

    private Random randGen = new Random();
    private double healthIncreaseRate = Hydra.DEFAULT_HEALTH_INCREASE_RATE;
    private double healthIncreaseAmount = Hydra.DEFAULT_HEALTH_INCREASE_AMOUNT;

    public Hydra(Position position, double health, double attack, double healthIncreaseRate,
        double healthIncreaseAmount) {
        super(position, health, attack);
        this.healthIncreaseRate = healthIncreaseRate;
        this.healthIncreaseAmount = healthIncreaseAmount;
    }

    public boolean healthIncreased() {
        Random random = new Random();
        double randomValue = random.nextDouble();
        return randomValue >= healthIncreaseRate;
    }

    public void increaseHealth() {
        BattleStatistics stats = this.getBattleStatistics();
        stats.setHealth(stats.getHealth() + healthIncreaseAmount);
    }

    @Override
    public void move(Game game) {
        Position nextPos;
        GameMap map = game.getMap();
        if (map.getPlayer().getEffectivePotion() instanceof InvincibilityPotion) {
            MovementStrategy moveStrategy = new RetreatStrategy();
            moveStrategy.move(game, this);
        } else {
            List<Position> pos = getPosition().getCardinallyAdjacentPositions();
            pos = pos.stream().filter(p -> map.canMoveTo(this, p)).collect(Collectors.toList());
            if (pos.size() == 0) {
                nextPos = getPosition();
            } else {
                nextPos = pos.get(randGen.nextInt(pos.size()));
            }
            map.moveTo(this, nextPos);
        }
    }
}
