package fr.epsi.rpgbackend.controller;

import fr.epsi.rpgbackend.dto.CharacterDto;
import fr.epsi.rpgbackend.service.CharacterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/characters")
@Tag(name = "Characters", description = "Gestion des personnages de la campagne")
public class CharacterController {

    private final CharacterService characterService;

    public CharacterController(CharacterService characterService) {
        this.characterService = characterService;
    }

    @GetMapping
    @Operation(summary = "Lister tous les personnages")
    public List<CharacterDto> findAll() {
        return characterService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un personnage par son ID")
    public ResponseEntity<CharacterDto> findById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(characterService.findById(id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Créer un nouveau personnage")
    public CharacterDto create(@RequestBody CharacterDto dto) {
        return characterService.create(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un personnage")
    public ResponseEntity<CharacterDto> update(@PathVariable Long id, @RequestBody CharacterDto dto) {
        try {
            return ResponseEntity.ok(characterService.update(id, dto));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Supprimer un personnage")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            characterService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
