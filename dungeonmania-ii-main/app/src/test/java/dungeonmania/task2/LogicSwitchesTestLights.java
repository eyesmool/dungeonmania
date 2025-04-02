package dungeonmania.task2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class LogicSwitchesTestLights {
    private boolean boulderAt(DungeonResponse res, int x, int y) {
        Position pos = new Position(x, y);
        return TestUtils.getEntitiesStream(res, "boulder").anyMatch(it -> it.getPosition().equals(pos));
    }
    @Test
    @Tag("2f-0")
    @DisplayName("Dryrun")
    public void dryRunLogicSwitches() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_dryrun_logic", "c_dryrun_logic");
        assertTrue(TestUtils.countType(res, "switch_door") == 1);
        assertTrue(TestUtils.countType(res, "bomb") == 1);
        assertTrue(TestUtils.countType(res, "wire") == 1);
        assertTrue(TestUtils.countType(res, "light_bulb_off") >= 1);
        res = dmc.tick(Direction.RIGHT);
        assertTrue(TestUtils.countType(res, "light_bulb_on") == 1);
    }
    @Test
    @Tag("2f-1")
    @DisplayName("Test creation of new entities")
    public void testCreationOfNewEntities() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicTest_simpleConnection", "c_logicTest_simpleConnection");
        assertEquals(1, TestUtils.getEntities(res, "boulder").size());
        assertEquals(1, TestUtils.getEntities(res, "switch").size());
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_off").size());
        assertEquals(1, TestUtils.getEntities(res, "wire").size());
        assertEquals(1, TestUtils.getEntities(res, "switch_door").size());
    }

    @Test
    @Tag("2f-2")
    @DisplayName("Test simple conduct with two wires OR strategy")
    public void testSimpleConduct() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicTest_simpleConductTwoWires", "c_logicTest_simpleConnection");
        assertEquals(1, TestUtils.getEntities(res, "boulder").size());
        assertEquals(1, TestUtils.getEntities(res, "switch").size());
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_off").size());
        assertEquals(2, TestUtils.getEntities(res, "wire").size());

        // Player moves boulder
        res = dmc.tick(Direction.RIGHT);
        assertTrue(boulderAt(res, 2, 0));
        assertEquals(new Position(1, 0), TestUtils.getPlayer(res).get().getPosition());

        assertTrue(TestUtils.countType(res, "light_bulb_on") == 1);
        assertEquals(0, TestUtils.getEntities(res, "light_bulb_off").size());
    }

    @Test
    @Tag("2f-3")
    @DisplayName("Light turns off if switch deactvated OR strategy")
    public void testSimpleSwitchOffLightOff() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicTest_simpleConductTwoWires", "c_logicTest_simpleConnection");
        assertEquals(1, TestUtils.getEntities(res, "boulder").size());
        assertEquals(1, TestUtils.getEntities(res, "switch").size());
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_off").size());
        assertEquals(2, TestUtils.getEntities(res, "wire").size());

        // Player moves boulder
        res = dmc.tick(Direction.RIGHT);
        assertTrue(boulderAt(res, 2, 0));

        assertEquals(1, TestUtils.getEntities(res, "light_bulb_on").size());
        assertEquals(0, TestUtils.getEntities(res, "light_bulb_off").size());

        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.UP);

        assertEquals(0, TestUtils.getEntities(res, "light_bulb_on").size());
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_off").size());
    }

    @Test
    @Tag("2f-4")
    @DisplayName("Tests a winding path of wires")
    public void testWindingPath() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicTest_windingPath", "c_logicTest_simpleConnection");
        // Player moves boulder
        res = dmc.tick(Direction.RIGHT);
        assertTrue(boulderAt(res, 2, 4));
        // Light bulb on
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_on").size());
        assertEquals(0, TestUtils.getEntities(res, "light_bulb_off").size());
        res = dmc.tick(Direction.RIGHT);
        // Light bulb off
        assertEquals(0, TestUtils.getEntities(res, "light_bulb_on").size());
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_off").size());
    }

    @Test
    @Tag("2f-5")
    @DisplayName("Test simple AND logic strategy")
    public void testAndLogicStrategy() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicTest_testSimpleAnd", "c_logicTest_simpleConnection");
        // move boulder right
        res = dmc.tick(Direction.RIGHT);

        // Light bulb off because one not all adjacent positions on
        assertEquals(0, TestUtils.getEntities(res, "light_bulb_on").size());
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_off").size());

        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);

        // Light bulb on because all adjacent positions on
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_on").size());
        assertEquals(0, TestUtils.getEntities(res, "light_bulb_off").size());
    }

    @Test
    @Tag("2f-6")
    @DisplayName("Test simple AND logic strategy light turns off if switch deactivated")
    public void testAndLogicStrategyTurnsOff() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicTest_AndOnThenOff", "c_logicTest_simpleConnection");

        // move boulder right
        res = dmc.tick(Direction.RIGHT);

        // Light bulb off because one not all adjacent positions on
        assertEquals(0, TestUtils.getEntities(res, "light_bulb_on").size());
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_off").size());

        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);

        // Light bulb on because one all adjacent positions on
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_on").size());
        assertEquals(0, TestUtils.getEntities(res, "light_bulb_off").size());

        // move boulder off switch
        res = dmc.tick(Direction.RIGHT);

        // Light bulb off because one not all adjacent positions on
        assertEquals(0, TestUtils.getEntities(res, "light_bulb_on").size());
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_off").size());

    }

    @Test
    @Tag("2f-7")
    @DisplayName("Test simple XOR logic strategy light turns on")
    public void testXORLogicStrategyTurnsOn() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicTest_XOROn", "c_logicTest_simpleConnection");

        // move boulder right
        res = dmc.tick(Direction.RIGHT);

        // Light bulb on because XOR condition satisfied
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_on").size());
        assertEquals(0, TestUtils.getEntities(res, "light_bulb_off").size());

        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);

        // Light bulb off because XOR condition NOT satisfied
        assertEquals(0, TestUtils.getEntities(res, "light_bulb_on").size());
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_off").size());
    }

    @Test
    @Tag("2f-8")
    @DisplayName("Test simple XOR logic strategy light turns on and then off")
    public void testXORLogicStrategyTurnsOff() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicTest_XOROnThenOff", "c_logicTest_simpleConnection");

        // move boulder right
        res = dmc.tick(Direction.RIGHT);

        // Light bulb on because XOR condition satisfied
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_on").size());
        assertEquals(0, TestUtils.getEntities(res, "light_bulb_off").size());

        // move boulder onto switch
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);

        // Light bulb off because XOR condition NOT satisfied
        assertEquals(0, TestUtils.getEntities(res, "light_bulb_on").size());
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_off").size());
    }

    @Test
    @Tag("2f-9")
    @DisplayName("Test simple COAND logic strategy light turns on")
    public void testCOANDStrategyOn() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicTest_CO_AND_Simple", "c_logicTest_simpleConnection");

        // move boulder right
        res = dmc.tick(Direction.RIGHT);

        // Light bulb on because CO_AND condition satisfied
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_on").size());
        assertEquals(0, TestUtils.getEntities(res, "light_bulb_off").size());
    }

    @Test
    @Tag("2f-10")
    @DisplayName("Bad attempt to trigger COAND")
    public void testCOANDBadAttempt() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicTest_CO_AND_Bad", "c_logicTest_simpleConnection");

        // move boulder right
        res = dmc.tick(Direction.RIGHT);

        // Light bulb off because CO_AND condition NOT satisfied
        assertEquals(0, TestUtils.getEntities(res, "light_bulb_on").size());
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_off").size());
    }

    @Test
    @Tag("2f-11")
    @DisplayName("Bomb go boooom and destroys wire breaking circuit and light goes off")
    public void testBombDestroysWire() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicTest_BombDestroysWire", "c_logicTest_simpleConnection");

        // move boulder right
        res = dmc.tick(Direction.RIGHT);

        // Light bulb on because condition satisfied
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_on").size());
        assertEquals(0, TestUtils.getEntities(res, "light_bulb_off").size());

        // move boulder onto bomb switch :o
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);

        // Check Bomb exploded
        assertEquals(0, TestUtils.getEntities(res, "bomb").size());
        assertEquals(1, TestUtils.getEntities(res, "switch").size());
        assertEquals(3, TestUtils.getEntities(res, "wire").size());

         // Light bulb off because CO_AND condition NOT satisfied
         assertEquals(0, TestUtils.getEntities(res, "light_bulb_on").size());
         assertEquals(1, TestUtils.getEntities(res, "light_bulb_off").size());
    }

    @Test
    @Tag("2f-12")
    @DisplayName("Bomb go boooom and destroys SWITCH breaking circuit and light goes off")
    public void testBombDestroysSwitch() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicTest_bombDestroysSwitch", "c_logicTest_simpleConnection");

        // move boulder right
        res = dmc.tick(Direction.RIGHT);

        // Light bulb on because condition satisfied
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_on").size());
        assertEquals(0, TestUtils.getEntities(res, "light_bulb_off").size());

        // move boulder onto bomb switch :o
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);

        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);


        // Check Bomb exploded
        assertEquals(0, TestUtils.getEntities(res, "bomb").size());
        assertEquals(0, TestUtils.getEntities(res, "switch").size());
        assertEquals(1, TestUtils.getEntities(res, "wire").size());
        assertEquals(0, TestUtils.getEntities(res, "boulder").size());


         // Light bulb off because CO_AND condition NOT satisfied
         assertEquals(0, TestUtils.getEntities(res, "light_bulb_on").size());
         assertEquals(1, TestUtils.getEntities(res, "light_bulb_off").size());
    }
}
