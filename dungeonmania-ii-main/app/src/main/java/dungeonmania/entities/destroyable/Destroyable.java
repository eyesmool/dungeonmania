package dungeonmania.entities.destroyable;

import dungeonmania.map.GameMap;

public interface Destroyable {
        public abstract void onDestroy(GameMap gameMap);
}
