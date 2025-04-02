package dungeonmania.goals;

import dungeonmania.Game;

public abstract class Goal {
    private String type;

    public Goal(String type) {
        this.type = type;
    }

    /**
     * @return true if the goal has been achieved, false otherwise
     */
    public abstract boolean achieved(Game game);

    public abstract String toString(Game game);

    public String getType() {
        return type;
    }
}
