package dungeonmania.goals;

import org.json.JSONArray;
import org.json.JSONObject;

public class GoalFactory {

    public static Goal createGoal(JSONObject jsonGoal, JSONObject config) {
        return createGoal(jsonGoal, config, null);
    }

    private static Goal createGoal(JSONObject jsonGoal, JSONObject config, Goal parent) {
        Goal goal = null;
        String goalType = jsonGoal.getString("goal");

        switch (goalType) {
            case "AND":
                goal = createAndGoal(jsonGoal, config);
                break;
            case "OR":
                goal = createOrGoal(jsonGoal, config);
                break;
            case "exit":
                goal = new ExitGoal();
                break;
            case "boulders":
                goal = new BouldersGoal();
                break;
            case "treasure":
                int treasureGoal = config.optInt("treasure_goal", 1);
                goal = new TreasureGoal(treasureGoal);
                break;
            case "enemies":
                int enemyGoal = config.optInt("enemy_goal", 1);
                goal = new EnemyGoal(enemyGoal);
                break;
            default:
                throw new IllegalArgumentException("Unknown goal type: " + goalType);
        }

        return goal;
    }

    private static Goal createAndGoal(JSONObject jsonGoal, JSONObject config) {
        JSONArray subgoals = jsonGoal.getJSONArray("subgoals");
        Goal goal1 = createGoal(subgoals.getJSONObject(0), config);
        Goal goal2 = createGoal(subgoals.getJSONObject(1), config);
        return new AndGoal(goal1, goal2);
    }

    private static Goal createOrGoal(JSONObject jsonGoal, JSONObject config) {
        JSONArray subgoals = jsonGoal.getJSONArray("subgoals");
        Goal goal1 = createGoal(subgoals.getJSONObject(0), config);
        Goal goal2 = createGoal(subgoals.getJSONObject(1), config);
        return new OrGoal(goal1, goal2);
    }
}
