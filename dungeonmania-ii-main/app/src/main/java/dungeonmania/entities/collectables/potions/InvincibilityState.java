package dungeonmania.entities.collectables.potions;

import dungeonmania.battles.BattleStatistics;

public class InvincibilityState implements PotionState {
    private static final int HEALTH = 0;
    private static final int ATTACK = 0;
    private static final int DEFENSE = 0;
    private static final int ATTACK_MAGNIFIER = 1;
    private static final int DAMAGE_REDUCER = 1;
    private static final boolean IS_INVINCIBLE = true;
    private static final boolean IS_ENABLED = true;

    @Override
    public BattleStatistics applyBuff(BattleStatistics origin) {
        return BattleStatistics.applyBuff(origin, new BattleStatistics(HEALTH, ATTACK, DEFENSE,
                ATTACK_MAGNIFIER, DAMAGE_REDUCER, IS_INVINCIBLE, IS_ENABLED));
    }
}
