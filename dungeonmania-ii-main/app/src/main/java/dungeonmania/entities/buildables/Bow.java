package dungeonmania.entities.buildables;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;

public class Bow extends Buildable {
    private int durability;

    private static final int HEALTH = 0;
    private static final int ATTACK = 0;
    private static final int DEFENSE = 0;
    private static final int ATTACK_MAGNIFIER = 2;
    private static final int DAMAGE_REDUCER = 1;
    private static final int ZERO_DURABILITY = 0;

    public Bow(int durability) {
        super(null);
        this.durability = durability;
    }

    @Override
    public void use(Game game) {
        durability--;
        if (durability <= ZERO_DURABILITY) {
            game.getPlayer().remove(this);
        }
    }

    @Override
    public BattleStatistics applyBuff(BattleStatistics origin) {
        return BattleStatistics.applyBuff(origin,
                new BattleStatistics(HEALTH, ATTACK, DEFENSE, ATTACK_MAGNIFIER, DAMAGE_REDUCER));
    }

    @Override
    public int getDurability() {
        return durability;
    }
}
