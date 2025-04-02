package dungeonmania.entities.logicStrategy;

import org.json.JSONObject;



public class LogicFactory {

    public static LogicStrategy createLogic(JSONObject jsonLogic, JSONObject config) {
        return createLogicStrategy(jsonLogic, config);
    }

    public static LogicStrategy createLogicStrategy(JSONObject jsonLogic, JSONObject config) {
        LogicStrategy strategy = null;
        String logicType = jsonLogic.getString("logic");
        switch (logicType) {
            case "and":
                strategy = new ANDLogicStrategy();
                break;
            case "or":
                strategy = new ORLogicStrategy();
                break;
            case "xor":
                strategy = new XORLogicStrategy();
                break;
            case "co_and":
                strategy = new COANDLogicStrategy();
                break;
            default:
                throw new IllegalArgumentException("Unknown logic type: " + logicType);
        }
        return strategy;
    }
}
