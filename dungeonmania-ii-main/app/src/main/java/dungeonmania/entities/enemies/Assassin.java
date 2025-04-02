package dungeonmania.entities.enemies;

import java.util.Random;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Interactable;
import dungeonmania.entities.Player;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.potions.InvincibilityPotion;
import dungeonmania.entities.collectables.potions.InvisibilityPotion;
import dungeonmania.entities.enemies.movement.AllyStrategy;
import dungeonmania.entities.enemies.movement.MovementStrategy;
import dungeonmania.entities.enemies.movement.PursueStrategy;
import dungeonmania.entities.enemies.movement.RandomStrategy;
import dungeonmania.entities.enemies.movement.RetreatStrategy;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Assassin extends Enemy implements Interactable {
    public static final int DEFAULT_BRIBE_AMOUNT = 1;
    public static final int DEFAULT_BRIBE_RADIUS = 1;
    public static final double DEFAULT_BRIBE_FAIL_RATE = 0.3;
    public static final double DEFAULT_ATTACK = 5.0;
    public static final double DEFAULT_HEALTH = 10.0;

    private int bribeAmount = Assassin.DEFAULT_BRIBE_AMOUNT;
    private int bribeRadius = Assassin.DEFAULT_BRIBE_RADIUS;
    private double bribeFailRate = Assassin.DEFAULT_BRIBE_FAIL_RATE;

    private double allyAttack;
    private double allyDefence;
    private boolean allied = false;
    private boolean isAdjacentToPlayer = false;

    public Assassin(Position position, double health, double attack, int bribeAmount, int bribeRadius,
        double bribeFailRate, double allyAttack, double allyDefence) {
        super(position, health, attack);
        this.bribeAmount = bribeAmount;
        this.bribeRadius = bribeRadius;
        this.bribeFailRate = bribeFailRate;
        this.allyAttack = allyAttack;
        this.allyDefence = allyDefence;
    }

    public boolean isAllied() {
        return allied;
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (allied)
            return;
        super.onOverlap(map, entity);
    }

    private boolean canBeBribed(Player player) {
        return bribeRadius >= 0 && player.countEntityOfType(Treasure.class) >= bribeAmount;
    }

    public boolean bribeFailed() {
        Random random = new Random();
        double randomValue = random.nextDouble();
        return randomValue >= bribeFailRate;
    }

    private void bribe(Player player) {
        for (int i = 0; i < bribeAmount; i++) {
            player.use(Treasure.class);
        }
    }

    @Override
    public void interact(Player player, Game game) {
        allied = bribeFailed();
        bribe(player);
        if (!isAdjacentToPlayer && Position.isAdjacent(player.getPosition(), getPosition()))
            isAdjacentToPlayer = true;
    }

    @Override
    public void move(Game game) {
        GameMap map = game.getMap();
        if (allied) {
            // Ally with player
            MovementStrategy moveStrategy = new AllyStrategy();
            moveStrategy.move(game, this);
        } else if (map.getPlayer().getEffectivePotion() instanceof InvisibilityPotion) {
            // Move random
            MovementStrategy moveStrategy = new RandomStrategy();
            moveStrategy.move(game, this);
        } else if (map.getPlayer().getEffectivePotion() instanceof InvincibilityPotion) {
            // Retreat from player
            MovementStrategy moveStrategy = new RetreatStrategy();
            moveStrategy.move(game, this);
        } else {
            // Follow the player
            MovementStrategy moveStrategy = new PursueStrategy();
            moveStrategy.move(game, this);
        }
    }

    @Override
    public boolean isInteractable(Player player) {
        return !allied && canBeBribed(player);
    }

    @Override
    public BattleStatistics getBattleStatistics() {
        if (!allied)
            return super.getBattleStatistics();
        return new BattleStatistics(0, allyAttack, allyDefence, 1, 1);
    }

    public boolean isAdjacentToPlayer() {
        return isAdjacentToPlayer;
    }

    public void setIsAdjacentToPlayer(boolean isAdjacentToPlayer) {
        this.isAdjacentToPlayer = isAdjacentToPlayer;
    }
}
