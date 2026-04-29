package fr.epsi.rpgbackend.service;

import fr.epsi.rpgbackend.dto.CharacterEquipmentDto;
import fr.epsi.rpgbackend.entity.Character;
import fr.epsi.rpgbackend.entity.CharacterEquipment;
import fr.epsi.rpgbackend.repository.CharacterEquipmentRepository;
import fr.epsi.rpgbackend.repository.CharacterRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
class CharacterEquipmentServiceTest {

    @Mock
    private CharacterEquipmentRepository equipmentRepository;

    @Mock
    private CharacterRepository characterRepository;

    @InjectMocks
    private CharacterEquipmentService service;

    @Test
    void findByCharacter_returnsMappedDtos() {
        Character character = new Character();
        character.setId(1L);

        CharacterEquipment equipment = new CharacterEquipment();
        equipment.setId(2L);
        equipment.setCharacter(character);
        equipment.setItemIndex("shield");
        equipment.setQuantity(1);
        equipment.setEquipped(true);

        when(equipmentRepository.findByCharacterId(1L)).thenReturn(List.of(equipment));

        List<CharacterEquipmentDto> result = service.findByCharacter(1L);

        assertEquals(1, result.size());
        assertEquals("shield", result.getFirst().getItemIndex());
        assertTrue(result.getFirst().isEquipped());
    }

    @Test
    void add_throwsWhenCharacterDoesNotExist() {
        when(characterRepository.findById(5L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.add(5L, buildDto()));
        verify(equipmentRepository, never()).save(any());
    }

    @Test
    void update_throwsWhenEquipmentDoesNotBelongToCharacter() {
        Character owner = new Character();
        owner.setId(99L);

        CharacterEquipment equipment = new CharacterEquipment();
        equipment.setId(2L);
        equipment.setCharacter(owner);

        when(equipmentRepository.findById(2L)).thenReturn(Optional.of(equipment));

        assertThrows(NoSuchElementException.class, () -> service.update(1L, 2L, buildDto()));
    }

    @Test
    void delete_removesEquipmentWhenFound() {
        Character owner = new Character();
        owner.setId(1L);

        CharacterEquipment equipment = new CharacterEquipment();
        equipment.setId(2L);
        equipment.setCharacter(owner);

        when(equipmentRepository.findById(2L)).thenReturn(Optional.of(equipment));

        service.delete(1L, 2L);

        verify(equipmentRepository).delete(equipment);
    }

    private CharacterEquipmentDto buildDto() {
        CharacterEquipmentDto dto = new CharacterEquipmentDto();
        dto.setItemIndex("shield");
        dto.setQuantity(1);
        dto.setEquipped(true);
        return dto;
    }
}

