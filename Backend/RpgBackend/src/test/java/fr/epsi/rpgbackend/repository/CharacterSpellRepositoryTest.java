package fr.epsi.rpgbackend.repository;

import fr.epsi.rpgbackend.entity.Character;
import fr.epsi.rpgbackend.entity.CharacterSpell;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class CharacterSpellRepositoryTest {

    @Autowired
    private CharacterRepository characterRepository;

    @Autowired
    private CharacterSpellRepository spellRepository;

    @Test
    void findByCharacterIdAndIsPrepared_filtersByCharacterAndPreparedFlag() {
        Character first = characterRepository.save(buildCharacter("Merlin", "Alice"));
        Character second = characterRepository.save(buildCharacter("Morgana", "Bob"));

        spellRepository.save(buildSpell(first, "fireball", true));
        spellRepository.save(buildSpell(first, "shield", false));
        spellRepository.save(buildSpell(second, "invisibility", true));

        List<CharacterSpell> prepared = spellRepository.findByCharacterIdAndIsPrepared(first.getId(), true);

        assertEquals(1, prepared.size());
        assertEquals("fireball", prepared.getFirst().getSpellIndex());
    }

    @Test
    void findByCharacterId_returnsAllSpellsForCharacter() {
        Character character = characterRepository.save(buildCharacter("Gandalf", "Sophie"));

        spellRepository.save(buildSpell(character, "light", true));
        spellRepository.save(buildSpell(character, "misty-step", true));

        List<CharacterSpell> spells = spellRepository.findByCharacterId(character.getId());

        assertEquals(2, spells.size());
    }

    private Character buildCharacter(String name, String playerName) {
        Character character = new Character();
        character.setName(name);
        character.setPlayerName(playerName);
        character.setRaceIndex("human");
        character.setClassIndex("sorcerer");
        character.setLevel(3);
        character.setHpMax(16);
        character.setHpCurrent(16);
        character.setArmorClass(12);
        character.setSpeed(30);
        return character;
    }

    private CharacterSpell buildSpell(Character character, String spellIndex, boolean prepared) {
        CharacterSpell spell = new CharacterSpell();
        spell.setCharacter(character);
        spell.setSpellIndex(spellIndex);
        spell.setPrepared(prepared);
        return spell;
    }
}

