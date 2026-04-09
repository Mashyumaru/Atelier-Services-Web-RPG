package fr.epsi.rpgbackend.service;

import fr.epsi.rpgbackend.dto.CharacterSpellDto;
import fr.epsi.rpgbackend.entity.Character;
import fr.epsi.rpgbackend.entity.CharacterSpell;
import fr.epsi.rpgbackend.repository.CharacterRepository;
import fr.epsi.rpgbackend.repository.CharacterSpellRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class CharacterSpellService {

    private final CharacterSpellRepository spellRepository;
    private final CharacterRepository characterRepository;

    public CharacterSpellService(CharacterSpellRepository spellRepository,
                                 CharacterRepository characterRepository) {
        this.spellRepository = spellRepository;
        this.characterRepository = characterRepository;
    }

    @Transactional(readOnly = true)
    public List<CharacterSpellDto> findByCharacter(Long characterId) {
        return spellRepository.findByCharacterId(characterId).stream()
                .map(this::toDto)
                .toList();
    }

    public CharacterSpellDto add(Long characterId, CharacterSpellDto dto) {
        Character character = characterRepository.findById(characterId)
                .orElseThrow(() -> new NoSuchElementException("Personnage introuvable : " + characterId));
        CharacterSpell sp = new CharacterSpell();
        sp.setCharacter(character);
        sp.setSpellIndex(dto.getSpellIndex());
        sp.setPrepared(dto.isPrepared());
        return toDto(spellRepository.save(sp));
    }

    public CharacterSpellDto update(Long characterId, Long spellId, CharacterSpellDto dto) {
        CharacterSpell sp = spellRepository.findById(spellId)
                .filter(s -> s.getCharacter().getId().equals(characterId))
                .orElseThrow(() -> new NoSuchElementException("Sort introuvable : " + spellId));
        sp.setSpellIndex(dto.getSpellIndex());
        sp.setPrepared(dto.isPrepared());
        return toDto(spellRepository.save(sp));
    }

    public void delete(Long characterId, Long spellId) {
        CharacterSpell sp = spellRepository.findById(spellId)
                .filter(s -> s.getCharacter().getId().equals(characterId))
                .orElseThrow(() -> new NoSuchElementException("Sort introuvable : " + spellId));
        spellRepository.delete(sp);
    }

    private CharacterSpellDto toDto(CharacterSpell s) {
        CharacterSpellDto dto = new CharacterSpellDto();
        dto.setId(s.getId());
        dto.setSpellIndex(s.getSpellIndex());
        dto.setPrepared(s.isPrepared());
        return dto;
    }
}
