package dungeonmania.task2;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;

public class LogicBombs {
    @Test
    @Tag("2f-bombs-0")
    @DisplayName("test simple OR bomb")
    public void testOrBomb() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicBombs_simple_OR_bomb", "c_dryrun_logic");
        assertEquals(5, TestUtils.getEntities(res, "treasure").size());

        // Player moves boulder
        res = dmc.tick(Direction.RIGHT);
         // Check Bomb exploded
        assertEquals(0, TestUtils.getEntities(res, "bomb").size());
        assertEquals(0, TestUtils.getEntities(res, "treasure").size());
        assertEquals(0, TestUtils.getEntities(res, "wall").size());
        assertEquals(0, TestUtils.getEntities(res, "wire").size());
    }

    @Test
    @Tag("2f-bombs-1")
    @DisplayName("test simple AND bomb")
    public void testAndBomb() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicBombs_simple_AND_bomb", "c_dryrun_logic");
        assertEquals(4, TestUtils.getEntities(res, "treasure").size());

        // Player moves boulder
        res = dmc.tick(Direction.RIGHT);
         // Check Bomb not exploded
        assertEquals(1, TestUtils.getEntities(res, "bomb").size());
        assertEquals(4, TestUtils.getEntities(res, "treasure").size());

        // Player activates second switch
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        // Check Bomb exploded
        assertEquals(0, TestUtils.getEntities(res, "bomb").size());
        assertEquals(0, TestUtils.getEntities(res, "boulder").size());
        assertEquals(0, TestUtils.getEntities(res, "treasure").size());
        assertEquals(0, TestUtils.getEntities(res, "wall").size());
    }
}
