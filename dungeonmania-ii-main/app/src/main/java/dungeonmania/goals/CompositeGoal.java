package dungeonmania.goals;

public abstract class CompositeGoal extends Goal {
    private Goal goal1;
    private Goal goal2;

    public CompositeGoal(String type, Goal goal1, Goal goal2) {
        super(type);
        this.goal1 = goal1;
        this.goal2 = goal2;
    }

    public Goal getGoal1() {
        return goal1;
    }

    public Goal getGoal2() {
        return goal2;
    }
}
