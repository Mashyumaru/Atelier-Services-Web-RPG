package fr.epsi.rpgbackend.repository;

import fr.epsi.rpgbackend.entity.Character;
import fr.epsi.rpgbackend.entity.CharacterEquipment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class CharacterEquipmentRepositoryTest {

    @Autowired
    private CharacterRepository characterRepository;

    @Autowired
    private CharacterEquipmentRepository equipmentRepository;

    @Test
    void findByCharacterId_returnsOnlyEquipmentOfCharacter() {
        Character first = characterRepository.save(buildCharacter("Arwen", "Alice"));
        Character second = characterRepository.save(buildCharacter("Elrond", "Bob"));

        equipmentRepository.save(buildEquipment(first, "longbow", 1, true));
        equipmentRepository.save(buildEquipment(first, "rope", 2, false));
        equipmentRepository.save(buildEquipment(second, "staff", 1, true));

        List<CharacterEquipment> result = equipmentRepository.findByCharacterId(first.getId());

        assertEquals(2, result.size());
    }

    private Character buildCharacter(String name, String playerName) {
        Character character = new Character();
        character.setName(name);
        character.setPlayerName(playerName);
        character.setRaceIndex("elf");
        character.setClassIndex("wizard");
        character.setLevel(2);
        character.setHpMax(14);
        character.setHpCurrent(14);
        character.setArmorClass(11);
        character.setSpeed(30);
        return character;
    }

    private CharacterEquipment buildEquipment(Character character, String itemIndex, int quantity, boolean equipped) {
        CharacterEquipment equipment = new CharacterEquipment();
        equipment.setCharacter(character);
        equipment.setItemIndex(itemIndex);
        equipment.setQuantity(quantity);
        equipment.setEquipped(equipped);
        return equipment;
    }
}

