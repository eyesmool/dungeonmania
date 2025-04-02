package dungeonmania.goals;

import dungeonmania.Game;
import dungeonmania.entities.Switch;

public class BouldersGoal extends Goal {

    public BouldersGoal() {
        super("boulders");
    }

    @Override
    public boolean achieved(Game game) {
        return game.getMap().getEntities(Switch.class).stream().allMatch(Switch::isActivated);
    }

    @Override
    public String toString(Game game) {
        return achieved(game) ? "" : ":boulders";
    }
}
