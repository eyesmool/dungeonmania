package dungeonmania.task2;

import dungeonmania.DungeonManiaController;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EnemyGoalsTest {
    @Test
    @Tag("2a-1")
    @DisplayName("Test player battles zombie and zombie dies")
    public void testSimpleEnemyGoal() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_enemyGoalTest_one", "c_enemyGoalTest_one");
        List<EntityResponse> entities = res.getEntities();
        assertEquals(1, TestUtils.countEntityOfType(entities, "player"));
        assertEquals(1, TestUtils.countEntityOfType(entities, "zombie_toast"));

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        for (int i = 0; i < 3; i++) {
            res = dmc.tick(Direction.RIGHT);
            // Check if there is a battle - if there is one of the player or zombie is dead
            int battlesHeld = res.getBattles().size();
            if (battlesHeld != 0) {
                break;
            }
        }
        entities = res.getEntities();
        assertTrue(TestUtils.countEntityOfType(entities, "zombie") == 0);
        // assert goal is met
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @Tag("2a-2")
    @DisplayName("Test player destroys zombie spawner and completes goal")
    public void testDestroyZombieSpawnerGoal() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_enemyGoalTest_oneSpawner", "c_enemyGoalTest_oneSpawner");

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        assertEquals(1, TestUtils.getEntities(res, "zombie_toast_spawner").size());
        String spawnerId = TestUtils.getEntities(res, "zombie_toast_spawner").get(0).getId();

        // cardinally adjacent: true, has sword: false
        assertThrows(InvalidActionException.class, () -> dmc.interact(spawnerId));
        assertEquals(1, TestUtils.getEntities(res, "zombie_toast_spawner").size());

        // pick up sword
        res = dmc.tick(Direction.DOWN);
        assertEquals(1, TestUtils.getInventory(res, "sword").size());

        // cardinally adjacent: false, has sword: true
        assertThrows(InvalidActionException.class, () -> dmc.interact(spawnerId));
        assertEquals(1, TestUtils.getEntities(res, "zombie_toast_spawner").size());

        // move right
        res = dmc.tick(Direction.RIGHT);

        // cardinally adjacent: true, has sword: true, but invalid_id
        assertThrows(IllegalArgumentException.class, () -> dmc.interact("random_invalid_id"));
        // cardinally adjacent: true, has sword: true
        res = assertDoesNotThrow(() -> dmc.interact(spawnerId));
        assertEquals(1, TestUtils.countType(res, "zombie_toast_spawner"));
        // assert goal is met
        assertEquals("", TestUtils.getGoals(res));
    }

@Test
    @Tag("2a-2")
    @DisplayName("Test player destroys zombie spawner and zombie completes goal")
    public void testSpawnerAndZombieGoal() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(
            "d_enemyGoalTest_oneSpawnerOneZombie", "c_enemyGoalTest_oneSpawnerOneZombie"
        );
         // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));
        List<EntityResponse> entities = res.getEntities();

        assertEquals(1, TestUtils.getEntities(res, "zombie_toast_spawner").size());
        String spawnerId = TestUtils.getEntities(res, "zombie_toast_spawner").get(0).getId();

        for (int i = 0; i < 3; i++) {
            res = dmc.tick(Direction.RIGHT);
            // Check if there is a battle - if there is one of the player or zombie is dead
            int battlesHeld = res.getBattles().size();
            if (battlesHeld != 0) {
                break;
            }
        }
        entities = res.getEntities();
        // zombie is dead :(
        assertTrue(TestUtils.countEntityOfType(entities, "zombie_toast") == 0);

        // assert goal still not achieved
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        // pick up sword
        res = dmc.tick(Direction.UP);
        assertEquals(1, TestUtils.getInventory(res, "sword").size());

        // interact with spawner
        res = assertDoesNotThrow(() -> dmc.interact(spawnerId));

        // assert goal is met after destroying 1 enemy and spawner
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @Tag("2a-3")
    @DisplayName("Multiple enemies and spawner")
    public void testMultipleSpawnerAndZombieGoal() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(
            "d_enemyGoalTest_multipleEnemiesAndSpawners", "c_enemyGoalTest_multipleEnemiesAndSpawners"
        );
         // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));
        List<EntityResponse> entities = res.getEntities();

        assertEquals(2, TestUtils.getEntities(res, "zombie_toast_spawner").size());
        String spawner1Id = TestUtils.getEntities(res, "zombie_toast_spawner").get(0).getId();
        String spawner2Id = TestUtils.getEntities(res, "zombie_toast_spawner").get(1).getId();


        for (int i = 0; i < 5; i++) {
            res = dmc.tick(Direction.RIGHT);
            // Check if there is a battle - if there is one of the player or zombie is dead
            int battlesHeld = res.getBattles().size();
            if (battlesHeld != 0) {
                break;
            }
        }
        entities = res.getEntities();
        // zombies are dead :(
        assertTrue(TestUtils.countEntityOfType(entities, "zombie_toast") == 0);

        // assert goal still not achieved
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        // pick up sword
        res = dmc.tick(Direction.UP);
        assertEquals(1, TestUtils.getInventory(res, "sword").size());

        // interact with spawner
        res = assertDoesNotThrow(() -> dmc.interact(spawner1Id));
        res = assertDoesNotThrow(() -> dmc.interact(spawner2Id));

        // assert goal is met after destroying 1 enemy and spawner
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @Tag("2a-4")
    @DisplayName("Enemies goal AND Exit")
    public void testEnemyGoalAndExit() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(
            "d_enemyGoalTest_enemyGoalAndExit", "c_enemyGoalTest_enemyGoalAndExit"
        );
         // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));
        List<EntityResponse> entities = res.getEntities();

        assertEquals(2, TestUtils.getEntities(res, "zombie_toast_spawner").size());
        String spawner1Id = TestUtils.getEntities(res, "zombie_toast_spawner").get(0).getId();
        String spawner2Id = TestUtils.getEntities(res, "zombie_toast_spawner").get(1).getId();


        for (int i = 0; i < 5; i++) {
            res = dmc.tick(Direction.RIGHT);
            // Check if there is a battle - if there is one of the player or zombie is dead
            int battlesHeld = res.getBattles().size();
            if (battlesHeld != 0) {
                break;
            }
        }
        entities = res.getEntities();
        // zombieS are dead :(
        assertTrue(TestUtils.countEntityOfType(entities, "zombie_toast") == 0);

        // assert goal still not achieved
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        // pick up sword
        res = dmc.tick(Direction.UP);
        assertEquals(1, TestUtils.getInventory(res, "sword").size());

        // interact with spawner
        res = assertDoesNotThrow(() -> dmc.interact(spawner1Id));
        res = assertDoesNotThrow(() -> dmc.interact(spawner2Id));


        // still have to accomplish exit
        assertTrue(TestUtils.getGoals(res).contains(":exit"));

        // head to the exit amigos!
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);

        // assert goal is met after destroying 1 enemy and spawner
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @Tag("2a-5")
    @DisplayName("Enemies goal OR Exit")
    public void testEnemyGoalOrExit() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(
            "d_enemyGoalTest_enemyGoalOrExit", "c_enemyGoalTest_enemyGoalOrExit"
        );
         // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));
        assertTrue(TestUtils.getGoals(res).contains(":exit"));
        List<EntityResponse> entities = res.getEntities();

        assertEquals(2, TestUtils.getEntities(res, "zombie_toast_spawner").size());
        String spawner1Id = TestUtils.getEntities(res, "zombie_toast_spawner").get(0).getId();
        String spawner2Id = TestUtils.getEntities(res, "zombie_toast_spawner").get(1).getId();


        for (int i = 0; i < 5; i++) {
            res = dmc.tick(Direction.RIGHT);
            // Check if there is a battle - if there is one of the player or zombie is dead
            int battlesHeld = res.getBattles().size();
            if (battlesHeld != 0) {
                break;
            }
        }
        entities = res.getEntities();
        // zombieS are dead :(
        assertTrue(TestUtils.countEntityOfType(entities, "zombie_toast") == 0);

        // assert goal still not achieved
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        // pick up sword
        res = dmc.tick(Direction.UP);
        assertEquals(1, TestUtils.getInventory(res, "sword").size());

        // interact with spawner
        res = assertDoesNotThrow(() -> dmc.interact(spawner1Id));
        res = assertDoesNotThrow(() -> dmc.interact(spawner2Id));

        // shouldn't have to do exit because enemies_goal is done!
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @Tag("2a-6")
    @DisplayName("Interacting with same dungeon spawner doesn't accomplish goal")
    public void testInteractSameDungeonSpawner() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(
            "d_enemyGoalTest_multipleEnemiesAndSpawners", "c_enemyGoalTest_multipleEnemiesAndSpawners"
        );
         // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));
        List<EntityResponse> entities = res.getEntities();

        assertEquals(2, TestUtils.getEntities(res, "zombie_toast_spawner").size());
        String spawner1Id = TestUtils.getEntities(res, "zombie_toast_spawner").get(0).getId();


        for (int i = 0; i < 5; i++) {
            res = dmc.tick(Direction.RIGHT);
            // Check if there is a battle - if there is one of the player or zombie is dead
            int battlesHeld = res.getBattles().size();
            if (battlesHeld != 0) {
                break;
            }
        }
        entities = res.getEntities();
        // zombies are dead :(
        assertTrue(TestUtils.countEntityOfType(entities, "zombie_toast") == 0);

        // assert goal still not achieved
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        // pick up sword
        res = dmc.tick(Direction.UP);
        assertEquals(1, TestUtils.getInventory(res, "sword").size());

        // interact with SAME spawner
        res = assertDoesNotThrow(() -> dmc.interact(spawner1Id));
        res = assertDoesNotThrow(() -> dmc.interact(spawner1Id));

        // assert goal is not met after attempting to interact with the same spawner
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));
    }
}
