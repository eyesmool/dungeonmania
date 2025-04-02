package dungeonmania.goals;

import dungeonmania.Game;

public class TreasureGoal extends Goal {
    private int target;

    public TreasureGoal(int target) {
        super("treasure");
        this.target = target;
    }

    @Override
    public boolean achieved(Game game) {
        return game.getCollectedTreasureCount() >= target;
    }

    @Override
    public String toString(Game game) {
        return achieved(game) ? "" : ":treasure";
    }
}
