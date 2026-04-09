package fr.epsi.rpgbackend.controller;

import fr.epsi.rpgbackend.dto.CharacterEquipmentDto;
import fr.epsi.rpgbackend.service.CharacterEquipmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/characters/{characterId}/equipment")
@Tag(name = "Equipment", description = "Gestion de l'équipement par personnage")
public class CharacterEquipmentController {

    private final CharacterEquipmentService service;

    public CharacterEquipmentController(CharacterEquipmentService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Lister l'équipement d'un personnage")
    public List<CharacterEquipmentDto> findAll(@PathVariable Long characterId) {
        return service.findByCharacter(characterId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Ajouter un équipement à un personnage")
    public CharacterEquipmentDto add(
            @PathVariable Long characterId,
            @RequestBody CharacterEquipmentDto dto) {
        return service.add(characterId, dto);
    }

    @PutMapping("/{equipmentId}")
    @Operation(summary = "Mettre à jour un équipement")
    public ResponseEntity<CharacterEquipmentDto> update(
            @PathVariable Long characterId,
            @PathVariable Long equipmentId,
            @RequestBody CharacterEquipmentDto dto) {
        try {
            return ResponseEntity.ok(service.update(characterId, equipmentId, dto));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{equipmentId}")
    @Operation(summary = "Supprimer un équipement")
    public ResponseEntity<Void> delete(
            @PathVariable Long characterId,
            @PathVariable Long equipmentId) {
        try {
            service.delete(characterId, equipmentId);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
