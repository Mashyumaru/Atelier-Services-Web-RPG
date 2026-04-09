package fr.epsi.rpgbackend.controller;

import fr.epsi.rpgbackend.dto.CharacterSpellDto;
import fr.epsi.rpgbackend.service.CharacterSpellService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/characters/{characterId}/spells")
@Tag(name = "Spells", description = "Gestion des sorts par personnage")
public class CharacterSpellController {

    private final CharacterSpellService service;

    public CharacterSpellController(CharacterSpellService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Lister les sorts d'un personnage")
    public List<CharacterSpellDto> findAll(@PathVariable Long characterId) {
        return service.findByCharacter(characterId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Ajouter un sort à un personnage")
    public CharacterSpellDto add(
            @PathVariable Long characterId,
            @RequestBody CharacterSpellDto dto) {
        return service.add(characterId, dto);
    }

    @PutMapping("/{spellId}")
    @Operation(summary = "Mettre à jour un sort (préparer/dépreparer)")
    public ResponseEntity<CharacterSpellDto> update(
            @PathVariable Long characterId,
            @PathVariable Long spellId,
            @RequestBody CharacterSpellDto dto) {
        try {
            return ResponseEntity.ok(service.update(characterId, spellId, dto));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{spellId}")
    @Operation(summary = "Supprimer un sort d'un personnage")
    public ResponseEntity<Void> delete(
            @PathVariable Long characterId,
            @PathVariable Long spellId) {
        try {
            service.delete(characterId, spellId);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
