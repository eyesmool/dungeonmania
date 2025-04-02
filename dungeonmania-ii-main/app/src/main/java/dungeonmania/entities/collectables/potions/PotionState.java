package dungeonmania.entities.collectables.potions;

import dungeonmania.battles.BattleStatistics;

public interface PotionState {
    BattleStatistics applyBuff(BattleStatistics origin);
}
