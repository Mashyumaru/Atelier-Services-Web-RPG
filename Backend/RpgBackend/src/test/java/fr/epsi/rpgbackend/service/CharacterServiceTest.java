package fr.epsi.rpgbackend.service;

import fr.epsi.rpgbackend.dto.CharacterDto;
import fr.epsi.rpgbackend.dto.CharacterEquipmentDto;
import fr.epsi.rpgbackend.dto.CharacterSpellDto;
import fr.epsi.rpgbackend.entity.Character;
import fr.epsi.rpgbackend.entity.CharacterEquipment;
import fr.epsi.rpgbackend.entity.CharacterSpell;
import fr.epsi.rpgbackend.repository.CharacterRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CharacterServiceTest {

    @Mock
    private CharacterRepository characterRepository;

    @InjectMocks
    private CharacterService characterService;

    @Test
    void findById_mapsEntityWithCollectionsToDto() {
        Character character = new Character();
        character.setId(1L);
        character.setName("Aragorn");
        character.setPlayerName("Paul");
        character.setRaceIndex("human");
        character.setClassIndex("ranger");
        character.setLevel(5);
        character.setHpMax(45);
        character.setHpCurrent(40);
        character.setArmorClass(16);
        character.setSpeed(30);
        character.setStatStr(16);
        character.setStatDex(14);
        character.setStatCon(14);
        character.setStatInt(10);
        character.setStatWis(12);
        character.setStatCha(13);
        character.setNotes("Chef des Dunedain");

        CharacterEquipment equipment = new CharacterEquipment();
        equipment.setId(11L);
        equipment.setCharacter(character);
        equipment.setItemIndex("longsword");
        equipment.setQuantity(1);
        equipment.setEquipped(true);
        character.getEquipment().add(equipment);

        CharacterSpell spell = new CharacterSpell();
        spell.setId(21L);
        spell.setCharacter(character);
        spell.setSpellIndex("cure-wounds");
        spell.setPrepared(true);
        character.getSpells().add(spell);

        when(characterRepository.findById(1L)).thenReturn(Optional.of(character));

        CharacterDto result = characterService.findById(1L);

        assertEquals(1L, result.getId());
        assertEquals("Aragorn", result.getName());
        assertEquals(1, result.getEquipment().size());
        assertEquals("longsword", result.getEquipment().getFirst().getItemIndex());
        assertEquals(1, result.getSpells().size());
        assertEquals("cure-wounds", result.getSpells().getFirst().getSpellIndex());
    }

    @Test
    void create_mapsDtoToEntityBeforeSave() {
        CharacterDto input = buildCharacterDto();

        when(characterRepository.save(any(Character.class))).thenAnswer(invocation -> {
            Character saved = invocation.getArgument(0);
            saved.setId(42L);
            return saved;
        });

        CharacterDto result = characterService.create(input);

        ArgumentCaptor<Character> captor = ArgumentCaptor.forClass(Character.class);
        verify(characterRepository).save(captor.capture());
        Character persisted = captor.getValue();

        assertEquals("Gandalf", persisted.getName());
        assertEquals("staff", persisted.getEquipment().getFirst().getItemIndex());
        assertEquals("fireball", persisted.getSpells().getFirst().getSpellIndex());
        assertEquals(42L, result.getId());
        assertEquals("Gandalf", result.getName());
    }

    @Test
    void update_throwsWhenCharacterIsMissing() {
        when(characterRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> characterService.update(99L, buildCharacterDto()));
        verify(characterRepository, never()).save(any());
    }

    @Test
    void delete_callsRepositoryWhenCharacterExists() {
        when(characterRepository.existsById(10L)).thenReturn(true);

        characterService.delete(10L);

        verify(characterRepository).deleteById(10L);
    }

    @Test
    void delete_throwsWhenCharacterIsMissing() {
        when(characterRepository.existsById(10L)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> characterService.delete(10L));
        verify(characterRepository, never()).deleteById(anyLong());
    }

    private CharacterDto buildCharacterDto() {
        CharacterEquipmentDto equipmentDto = new CharacterEquipmentDto();
        equipmentDto.setItemIndex("staff");
        equipmentDto.setQuantity(1);
        equipmentDto.setEquipped(true);

        CharacterSpellDto spellDto = new CharacterSpellDto();
        spellDto.setSpellIndex("fireball");
        spellDto.setPrepared(true);

        CharacterDto dto = new CharacterDto();
        dto.setName("Gandalf");
        dto.setPlayerName("Sophie");
        dto.setRaceIndex("maiar");
        dto.setClassIndex("wizard");
        dto.setLevel(12);
        dto.setHpMax(80);
        dto.setHpCurrent(70);
        dto.setArmorClass(13);
        dto.setSpeed(30);
        dto.setStatStr(10);
        dto.setStatDex(14);
        dto.setStatCon(12);
        dto.setStatInt(20);
        dto.setStatWis(18);
        dto.setStatCha(16);
        dto.setNotes("You shall not pass");
        dto.setEquipment(List.of(equipmentDto));
        dto.setSpells(List.of(spellDto));
        return dto;
    }
}

