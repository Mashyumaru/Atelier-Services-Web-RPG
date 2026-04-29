package fr.epsi.rpgbackend.repository;

import fr.epsi.rpgbackend.entity.Character;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class CharacterRepositoryTest {

    @Autowired
    private CharacterRepository characterRepository;

    @Test
    void findByPlayerName_returnsOnlyMatchingCharacters() {
        characterRepository.save(buildCharacter("Legolas", "Alice"));
        characterRepository.save(buildCharacter("Gimli", "Bob"));
        characterRepository.save(buildCharacter("Boromir", "Alice"));

        List<Character> result = characterRepository.findByPlayerName("Alice");

        assertEquals(2, result.size());
    }

    private Character buildCharacter(String name, String playerName) {
        Character character = new Character();
        character.setName(name);
        character.setPlayerName(playerName);
        character.setRaceIndex("human");
        character.setClassIndex("fighter");
        character.setLevel(1);
        character.setHpMax(12);
        character.setHpCurrent(12);
        character.setArmorClass(10);
        character.setSpeed(30);
        return character;
    }
}

