package fr.epsi.rpgbackend.repository;

import fr.epsi.rpgbackend.entity.CharacterSpell;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CharacterSpellRepository extends JpaRepository<CharacterSpell, Long> {
    List<CharacterSpell> findByCharacterId(Long characterId);
    List<CharacterSpell> findByCharacterIdAndIsPrepared(Long characterId, boolean isPrepared);
}
