package dungeonmania.task2;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.mvp.TestUtils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AssassinTest {
    @Test
    @Tag("2b-1")
    @DisplayName("Test assassin in line with player moves towards them")
    public void simpleMovement() {
        //                                  Wall    Wall   Wall    Wall    Wall    Wall
        // P1       P2      P3      P4      A4      A3      A2      A1      .      Wall
        //                                  Wall    Wall   Wall    Wall    Wall    Wall
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_assassinTest_simpleMovement", "c_assassinTest_simpleMovement");

        assertEquals(new Position(8, 1), getAssPos(res));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(7, 1), getAssPos(res));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(6, 1), getAssPos(res));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(5, 1), getAssPos(res));
    }

    @Test
    @Tag("2b-2")
    @DisplayName("Test assassin stops if they cannot move any closer to the player")
    public void stopMovement() {
        //                  Wall     Wall    Wall
        // P1       P2      Wall      M1     Wall
        //                  Wall     Wall    Wall
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_assassinTest_stopMovement", "c_assassinTest_stopMovement");

        Position startingPos = getAssPos(res);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(startingPos, getAssPos(res));
    }

    @Test
    @Tag("2b-3")
    @DisplayName("Test assassins can not move through closed doors")
    public void doorMovement() {
        //                  Wall     Door    Wall
        // P1       P2      Wall      M1     Wall
        // Key              Wall     Wall    Wall
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_assassinTest_doorMovement", "c_assassinTest_doorMovement");

        Position startingPos = getAssPos(res);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(startingPos, getAssPos(res));
    }

    @Test
    @Tag("2b-4")
    @DisplayName("Test assassin moves around a wall to get to the player")
    public void evadeWall() {
        //                  Wall      M2
        // P1       P2      Wall      M1
        //                  Wall      M2
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_assassinTest_evadeWall", "c_assassinTest_evadeWall");

        res = dmc.tick(Direction.RIGHT);
        assertTrue(new Position(4, 1).equals(getAssPos(res)) || new Position(4, 3).equals(getAssPos(res)));
    }

    private Position getAssPos(DungeonResponse res) {
        return TestUtils.getEntities(res, "assassin").get(0).getPosition();
    }
}
