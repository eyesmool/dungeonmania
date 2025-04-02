package dungeonmania.goals;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Exit;
import dungeonmania.entities.Player;
import dungeonmania.util.Position;

import java.util.List;

public class ExitGoal extends Goal {

    public ExitGoal() {
        super("exit");
    }

    @Override
    public boolean achieved(Game game) {
        if (game.getPlayer() == null) return false;
        Player character = game.getPlayer();
        Position pos = character.getPosition();
        List<Exit> exits = game.getMap().getEntities(Exit.class);
        if (exits == null || exits.isEmpty()) return false;
        return exits.stream().map(Entity::getPosition).anyMatch(pos::equals);
    }

    @Override
    public String toString(Game game) {
        return achieved(game) ? "" : ":exit";
    }
}
