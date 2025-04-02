package dungeonmania.goals;

import dungeonmania.Game;

public class OrGoal extends CompositeGoal {

    public OrGoal(Goal goal1, Goal goal2) {
        super("OR", goal1, goal2);
    }

    @Override
    public boolean achieved(Game game) {
        return getGoal1().achieved(game) || getGoal2().achieved(game);
    }

    @Override
    public String toString(Game game) {
        return achieved(game) ? "" : "(" + getGoal1().toString(game) + " OR " + getGoal2().toString(game) + ")";
    }
}
