package fr.epsi.rpgbackend.repository;

import fr.epsi.rpgbackend.entity.Character;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CharacterRepository extends JpaRepository<Character, Long> {
    List<Character> findByPlayerName(String playerName);
}
