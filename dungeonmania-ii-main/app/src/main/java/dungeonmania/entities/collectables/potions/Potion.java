package dungeonmania.entities.collectables.potions;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.BattleItem;
import dungeonmania.entities.Entity;
import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public abstract class Potion extends Entity implements InventoryItem, BattleItem {
    private int duration;
    private PotionState state;

    public Potion(Position position, int duration, PotionState state) {
        super(position);
        this.duration = duration;
        this.state = state;
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true;
    }

    @Override
    public void use(Game game) {
        return;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public BattleStatistics applyBuff(BattleStatistics origin) {
        return state.applyBuff(origin);
    }

    @Override
    public int getDurability() {
        return 1;
    }
}
