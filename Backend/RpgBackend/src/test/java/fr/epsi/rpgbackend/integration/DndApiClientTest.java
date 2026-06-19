package fr.epsi.rpgbackend.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DndApiClientTest {

    private DndApiClient dndApiClient;

    @BeforeEach
    void setUp() {
        // Create a real DndApiClient with the test URL
        dndApiClient = new DndApiClient("https://www.dnd5eapi.co/api/2014");
    }

    @Test
    void testGetSpell() {
        // Arrange
        String spellIndex = "fireball";

        // Act
        Map<?, ?> result = dndApiClient.getSpell(spellIndex);

        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey("name"));
        assertTrue(result.containsKey("index"));
    }

    @Test
    void testGetMonster() {
        // Arrange
        String monsterIndex = "goblin";

        // Act
        Map<?, ?> result = dndApiClient.getMonster(monsterIndex);

        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey("name"));
        assertTrue(result.containsKey("index"));
    }

    @Test
    void testGetEquipment() {
        // Arrange
        String itemIndex = "longsword";

        // Act
        Map<?, ?> result = dndApiClient.getEquipment(itemIndex);

        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey("name"));
        assertTrue(result.containsKey("index"));
    }

    @Test
    void testGetRace() {
        // Arrange
        String raceIndex = "elf";

        // Act
        Map<?, ?> result = dndApiClient.getRace(raceIndex);

        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey("name"));
        assertTrue(result.containsKey("index"));
    }

    @Test
    void testGetClass() {
        // Arrange
        String classIndex = "wizard";

        // Act
        Map<?, ?> result = dndApiClient.getClass(classIndex);

        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey("name"));
        assertTrue(result.containsKey("index"));
    }

    @Test
    void testListRaces() {
        // Act
        Map<?, ?> result = dndApiClient.listRaces();

        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey("results"));
    }

    @Test
    void testListClasses() {
        // Act
        Map<?, ?> result = dndApiClient.listClasses();

        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey("results"));
    }

    @Test
    void testListSpells() {
        // Act
        Map<?, ?> result = dndApiClient.listSpells();

        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey("results"));
    }

    @Test
    void testListEquipment() {
        // Act
        Map<?, ?> result = dndApiClient.listEquipment();

        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey("results"));
    }
}
