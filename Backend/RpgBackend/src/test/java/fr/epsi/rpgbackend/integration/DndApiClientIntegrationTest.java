package fr.epsi.rpgbackend.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@EnabledIfEnvironmentVariable(named = "RUN_INTEGRATION_TESTS", matches = "true")
class DndApiClientIntegrationTest {

    private DndApiClient dndApiClient;

    @BeforeEach
    void setUp() {
        dndApiClient = new DndApiClient("https://www.dnd5eapi.co/api/2014");
    }

    @Test
    void testGetSpellIntegration() {
        // Act
        Map<?, ?> result = dndApiClient.getSpell("fireball");

        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey("name"));
        assertTrue(result.containsKey("index"));
        assertEquals("Fireball", result.get("name"));
        assertEquals("fireball", result.get("index"));
    }

    @Test
    void testGetMonsterIntegration() {
        // Act
        Map<?, ?> result = dndApiClient.getMonster("goblin");

        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey("name"));
        assertTrue(result.containsKey("index"));
        assertEquals("Goblin", result.get("name"));
        assertEquals("goblin", result.get("index"));
    }

    @Test
    void testGetEquipmentIntegration() {
        // Act
        Map<?, ?> result = dndApiClient.getEquipment("longsword");

        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey("name"));
        assertTrue(result.containsKey("index"));
        assertEquals("Longsword", result.get("name"));
        assertEquals("longsword", result.get("index"));
    }

    @Test
    void testGetRaceIntegration() {
        // Act
        Map<?, ?> result = dndApiClient.getRace("elf");

        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey("name"));
        assertTrue(result.containsKey("index"));
        assertEquals("Elf", result.get("name"));
        assertEquals("elf", result.get("index"));
    }

    @Test
    void testGetClassIntegration() {
        // Act
        Map<?, ?> result = dndApiClient.getClass("wizard");

        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey("name"));
        assertTrue(result.containsKey("index"));
        assertEquals("Wizard", result.get("name"));
        assertEquals("wizard", result.get("index"));
    }

    @Test
    void testListRacesIntegration() {
        // Act
        Map<?, ?> result = dndApiClient.listRaces();

        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey("count"));
        assertTrue(result.containsKey("results"));
        assertTrue((Integer) result.get("count") > 0);
    }

    @Test
    void testListClassesIntegration() {
        // Act
        Map<?, ?> result = dndApiClient.listClasses();

        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey("count"));
        assertTrue(result.containsKey("results"));
        assertTrue((Integer) result.get("count") > 0);
    }

    @Test
    void testListSpellsIntegration() {
        // Act
        Map<?, ?> result = dndApiClient.listSpells();

        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey("count"));
        assertTrue(result.containsKey("results"));
        assertTrue((Integer) result.get("count") > 0);
    }

    @Test
    void testListEquipmentIntegration() {
        // Act
        Map<?, ?> result = dndApiClient.listEquipment();

        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey("count"));
        assertTrue(result.containsKey("results"));
        assertTrue((Integer) result.get("count") > 0);
    }
}