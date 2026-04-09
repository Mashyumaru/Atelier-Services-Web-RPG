package fr.epsi.rpgbackend.service;

import fr.epsi.rpgbackend.dto.CharacterEquipmentDto;
import fr.epsi.rpgbackend.entity.Character;
import fr.epsi.rpgbackend.entity.CharacterEquipment;
import fr.epsi.rpgbackend.repository.CharacterEquipmentRepository;
import fr.epsi.rpgbackend.repository.CharacterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class CharacterEquipmentService {

    private final CharacterEquipmentRepository equipmentRepository;
    private final CharacterRepository characterRepository;

    public CharacterEquipmentService(CharacterEquipmentRepository equipmentRepository,
                                     CharacterRepository characterRepository) {
        this.equipmentRepository = equipmentRepository;
        this.characterRepository = characterRepository;
    }

    @Transactional(readOnly = true)
    public List<CharacterEquipmentDto> findByCharacter(Long characterId) {
        return equipmentRepository.findByCharacterId(characterId).stream()
                .map(this::toDto)
                .toList();
    }

    public CharacterEquipmentDto add(Long characterId, CharacterEquipmentDto dto) {
        Character character = characterRepository.findById(characterId)
                .orElseThrow(() -> new NoSuchElementException("Personnage introuvable : " + characterId));
        CharacterEquipment eq = new CharacterEquipment();
        eq.setCharacter(character);
        eq.setItemIndex(dto.getItemIndex());
        eq.setQuantity(dto.getQuantity());
        eq.setEquipped(dto.isEquipped());
        return toDto(equipmentRepository.save(eq));
    }

    public CharacterEquipmentDto update(Long characterId, Long equipmentId, CharacterEquipmentDto dto) {
        CharacterEquipment eq = equipmentRepository.findById(equipmentId)
                .filter(e -> e.getCharacter().getId().equals(characterId))
                .orElseThrow(() -> new NoSuchElementException("Équipement introuvable : " + equipmentId));
        eq.setItemIndex(dto.getItemIndex());
        eq.setQuantity(dto.getQuantity());
        eq.setEquipped(dto.isEquipped());
        return toDto(equipmentRepository.save(eq));
    }

    public void delete(Long characterId, Long equipmentId) {
        CharacterEquipment eq = equipmentRepository.findById(equipmentId)
                .filter(e -> e.getCharacter().getId().equals(characterId))
                .orElseThrow(() -> new NoSuchElementException("Équipement introuvable : " + equipmentId));
        equipmentRepository.delete(eq);
    }

    private CharacterEquipmentDto toDto(CharacterEquipment e) {
        CharacterEquipmentDto dto = new CharacterEquipmentDto();
        dto.setId(e.getId());
        dto.setItemIndex(e.getItemIndex());
        dto.setQuantity(e.getQuantity());
        dto.setEquipped(e.isEquipped());
        return dto;
    }
}
