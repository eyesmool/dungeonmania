package dungeonmania.entities.overlappable;

import dungeonmania.entities.Entity;
import dungeonmania.map.GameMap;

public interface Overlappable {
        public void onOverlap(GameMap map, Entity entity);
}
