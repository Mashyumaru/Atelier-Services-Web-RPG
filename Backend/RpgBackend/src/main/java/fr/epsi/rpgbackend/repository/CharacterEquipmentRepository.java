package fr.epsi.rpgbackend.repository;

import fr.epsi.rpgbackend.entity.CharacterEquipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CharacterEquipmentRepository extends JpaRepository<CharacterEquipment, Long> {
    List<CharacterEquipment> findByCharacterId(Long characterId);
}
