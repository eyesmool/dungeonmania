package dungeonmania.entities.logicals;

import dungeonmania.entities.Entity;
import dungeonmania.entities.logicStrategy.LogicStrategy;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public abstract class LogicalEntity extends Entity {
        private LogicStrategy strategy;
        private boolean isActivated;

        public LogicalEntity(Position position, LogicStrategy strategy) {
                super(position);
                this.strategy = strategy;
        }

        public LogicalEntity(Position position) {
                super(position);
        }
        public LogicStrategy getStrategy() {
                return strategy;
        }

        public boolean logicConditionsMet(GameMap map) {
                isActivated = strategy.isStratSatisfied(this, map);
                return isActivated;
        }

        public boolean isActivated() {
                return isActivated;
        }
        public void setActivated(boolean isActivated) {
                this.isActivated = isActivated;
        }

}
