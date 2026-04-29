package fr.epsi.rpgbackend.service;

import fr.epsi.rpgbackend.dto.CharacterSpellDto;
import fr.epsi.rpgbackend.entity.Character;
import fr.epsi.rpgbackend.entity.CharacterSpell;
import fr.epsi.rpgbackend.repository.CharacterRepository;
import fr.epsi.rpgbackend.repository.CharacterSpellRepository;
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
class CharacterSpellServiceTest {

    @Mock
    private CharacterSpellRepository spellRepository;

    @Mock
    private CharacterRepository characterRepository;

    @InjectMocks
    private CharacterSpellService service;

    @Test
    void findByCharacter_returnsMappedDtos() {
        Character character = new Character();
        character.setId(1L);

        CharacterSpell spell = new CharacterSpell();
        spell.setId(3L);
        spell.setCharacter(character);
        spell.setSpellIndex("magic-missile");
        spell.setPrepared(true);

        when(spellRepository.findByCharacterId(1L)).thenReturn(List.of(spell));

        List<CharacterSpellDto> result = service.findByCharacter(1L);

        assertEquals(1, result.size());
        assertEquals("magic-missile", result.getFirst().getSpellIndex());
        assertTrue(result.getFirst().isPrepared());
    }

    @Test
    void add_throwsWhenCharacterDoesNotExist() {
        when(characterRepository.findById(5L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.add(5L, buildDto()));
        verify(spellRepository, never()).save(any());
    }

    @Test
    void update_throwsWhenSpellDoesNotBelongToCharacter() {
        Character owner = new Character();
        owner.setId(99L);

        CharacterSpell spell = new CharacterSpell();
        spell.setId(2L);
        spell.setCharacter(owner);

        when(spellRepository.findById(2L)).thenReturn(Optional.of(spell));

        assertThrows(NoSuchElementException.class, () -> service.update(1L, 2L, buildDto()));
    }

    @Test
    void delete_removesSpellWhenFound() {
        Character owner = new Character();
        owner.setId(1L);

        CharacterSpell spell = new CharacterSpell();
        spell.setId(2L);
        spell.setCharacter(owner);

        when(spellRepository.findById(2L)).thenReturn(Optional.of(spell));

        service.delete(1L, 2L);

        verify(spellRepository).delete(spell);
    }

    private CharacterSpellDto buildDto() {
        CharacterSpellDto dto = new CharacterSpellDto();
        dto.setSpellIndex("magic-missile");
        dto.setPrepared(true);
        return dto;
    }
}

