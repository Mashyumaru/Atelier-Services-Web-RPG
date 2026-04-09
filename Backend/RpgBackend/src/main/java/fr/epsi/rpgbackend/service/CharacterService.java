package fr.epsi.rpgbackend.service;

import fr.epsi.rpgbackend.dto.CharacterDto;
import fr.epsi.rpgbackend.dto.CharacterEquipmentDto;
import fr.epsi.rpgbackend.dto.CharacterSpellDto;
import fr.epsi.rpgbackend.entity.Character;
import fr.epsi.rpgbackend.entity.CharacterEquipment;
import fr.epsi.rpgbackend.entity.CharacterSpell;
import fr.epsi.rpgbackend.repository.CharacterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class CharacterService {

    private final CharacterRepository characterRepository;

    public CharacterService(CharacterRepository characterRepository) {
        this.characterRepository = characterRepository;
    }

    @Transactional(readOnly = true)
    public List<CharacterDto> findAll() {
        return characterRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public CharacterDto findById(Long id) {
        return characterRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new NoSuchElementException("Personnage introuvable : " + id));
    }

    public CharacterDto create(CharacterDto dto) {
        Character character = toEntity(dto);
        return toDto(characterRepository.save(character));
    }

    public CharacterDto update(Long id, CharacterDto dto) {
        Character character = characterRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Personnage introuvable : " + id));
        applyDto(dto, character);
        return toDto(characterRepository.save(character));
    }

    public void delete(Long id) {
        if (!characterRepository.existsById(id)) {
            throw new NoSuchElementException("Personnage introuvable : " + id);
        }
        characterRepository.deleteById(id);
    }

    // ---- Mapping ----

    private CharacterDto toDto(Character c) {
        CharacterDto dto = new CharacterDto();
        dto.setId(c.getId());
        dto.setName(c.getName());
        dto.setPlayerName(c.getPlayerName());
        dto.setRaceIndex(c.getRaceIndex());
        dto.setClassIndex(c.getClassIndex());
        dto.setLevel(c.getLevel());
        dto.setHpMax(c.getHpMax());
        dto.setHpCurrent(c.getHpCurrent());
        dto.setArmorClass(c.getArmorClass());
        dto.setSpeed(c.getSpeed());
        dto.setStatStr(c.getStatStr());
        dto.setStatDex(c.getStatDex());
        dto.setStatCon(c.getStatCon());
        dto.setStatInt(c.getStatInt());
        dto.setStatWis(c.getStatWis());
        dto.setStatCha(c.getStatCha());
        dto.setNotes(c.getNotes());

        dto.setEquipment(c.getEquipment().stream().map(e -> {
            CharacterEquipmentDto eq = new CharacterEquipmentDto();
            eq.setId(e.getId());
            eq.setItemIndex(e.getItemIndex());
            eq.setQuantity(e.getQuantity());
            eq.setEquipped(e.isEquipped());
            return eq;
        }).toList());

        dto.setSpells(c.getSpells().stream().map(s -> {
            CharacterSpellDto sp = new CharacterSpellDto();
            sp.setId(s.getId());
            sp.setSpellIndex(s.getSpellIndex());
            sp.setPrepared(s.isPrepared());
            return sp;
        }).toList());

        return dto;
    }

    private Character toEntity(CharacterDto dto) {
        Character c = new Character();
        applyDto(dto, c);
        return c;
    }

    private void applyDto(CharacterDto dto, Character c) {
        c.setName(dto.getName());
        c.setPlayerName(dto.getPlayerName());
        c.setRaceIndex(dto.getRaceIndex());
        c.setClassIndex(dto.getClassIndex());
        c.setLevel(dto.getLevel());
        c.setHpMax(dto.getHpMax());
        c.setHpCurrent(dto.getHpCurrent());
        c.setArmorClass(dto.getArmorClass());
        c.setSpeed(dto.getSpeed());
        c.setStatStr(dto.getStatStr());
        c.setStatDex(dto.getStatDex());
        c.setStatCon(dto.getStatCon());
        c.setStatInt(dto.getStatInt());
        c.setStatWis(dto.getStatWis());
        c.setStatCha(dto.getStatCha());
        c.setNotes(dto.getNotes());

        if (dto.getEquipment() != null) {
            c.getEquipment().clear();
            dto.getEquipment().forEach(eDto -> {
                CharacterEquipment eq = new CharacterEquipment();
                eq.setCharacter(c);
                eq.setItemIndex(eDto.getItemIndex());
                eq.setQuantity(eDto.getQuantity());
                eq.setEquipped(eDto.isEquipped());
                c.getEquipment().add(eq);
            });
        }

        if (dto.getSpells() != null) {
            c.getSpells().clear();
            dto.getSpells().forEach(sDto -> {
                CharacterSpell sp = new CharacterSpell();
                sp.setCharacter(c);
                sp.setSpellIndex(sDto.getSpellIndex());
                sp.setPrepared(sDto.isPrepared());
                c.getSpells().add(sp);
            });
        }
    }
}
