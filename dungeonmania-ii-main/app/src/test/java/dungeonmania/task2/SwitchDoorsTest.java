package dungeonmania.task2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class SwitchDoorsTest {
    @Test
    @Tag("2f-switch_doors-0")
    @DisplayName("test simple switch_door OR")
    public void testSwitchDoorOR() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_switchDoor_OR", "c_dryrun_logic");
        assertEquals(1, TestUtils.getEntities(res, "switch_door").size());

        EntityResponse initPlayer = TestUtils.getPlayer(res).get();

        // create the expected result
        EntityResponse expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(4, 3),
                false);

        // move player upward
        DungeonResponse actualDungonRes = dmc.tick(Direction.UP);
        EntityResponse actualPlayer = TestUtils.getPlayer(actualDungonRes).get();

        // assert after movement
        assertTrue(TestUtils.entityResponsesEqual(expectedPlayer, actualPlayer));

        // activate switch
        actualDungonRes = dmc.tick(Direction.DOWN);
        actualDungonRes = dmc.tick(Direction.LEFT);
        actualDungonRes = dmc.tick(Direction.UP);

        // go through switch door
        actualDungonRes = dmc.tick(Direction.RIGHT);
        actualDungonRes = dmc.tick(Direction.UP);
        EntityResponse throughAttempt = TestUtils.getPlayer(actualDungonRes).get();

        // create the expected result
        EntityResponse throughDoor = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(4, 2),
                false);

        // assert after movement
        assertTrue(TestUtils.entityResponsesEqual(throughAttempt, throughDoor));
    }

    @Test
    @Tag("2f-switch_doors-1")
    @DisplayName("test simple switch_door OR, deactivate DOOR can't go through")
    public void testSwitchDoorORDeactivate() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_switchDoor_OR", "c_dryrun_logic");
        assertEquals(1, TestUtils.getEntities(res, "switch_door").size());

        EntityResponse initPlayer = TestUtils.getPlayer(res).get();

        // create the expected result
        EntityResponse expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(4, 3),
                false);

        // move player upward
        DungeonResponse actualDungonRes = dmc.tick(Direction.UP);
        EntityResponse actualPlayer = TestUtils.getPlayer(actualDungonRes).get();

        // assert after movement
        assertTrue(TestUtils.entityResponsesEqual(expectedPlayer, actualPlayer));

        // activate switch
        actualDungonRes = dmc.tick(Direction.DOWN);
        actualDungonRes = dmc.tick(Direction.LEFT);
        actualDungonRes = dmc.tick(Direction.UP);

        // go through switch door
        actualDungonRes = dmc.tick(Direction.RIGHT);
        actualDungonRes = dmc.tick(Direction.UP);
        EntityResponse throughAttempt = TestUtils.getPlayer(actualDungonRes).get();

        // create the expected result
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(4, 2),
                false);

        // assert after movement
        assertTrue(TestUtils.entityResponsesEqual(throughAttempt, expectedPlayer));

        // move boulder off switch
        actualDungonRes = dmc.tick(Direction.DOWN);
        actualDungonRes = dmc.tick(Direction.LEFT);
        actualDungonRes = dmc.tick(Direction.UP);
        actualDungonRes = dmc.tick(Direction.RIGHT);


        // player can't go through door now
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(3, 2),
                false);

        throughAttempt = TestUtils.getPlayer(actualDungonRes).get();
        // assert after movement
        System.out.println(TestUtils.getPlayerPos(actualDungonRes));
        assertTrue(TestUtils.entityResponsesEqual(throughAttempt, expectedPlayer));

    }
}
