CREATE TABLE characters (
                            id BIGSERIAL PRIMARY KEY,
                            name VARCHAR(100) NOT NULL,
                            player_name VARCHAR(100), -- Optionnel : pour savoir à qui appartient le perso

    -- Liens vers l'API D&D 5e
                            race_index VARCHAR(50) NOT NULL,  -- Ex: 'elf', 'human'
                            class_index VARCHAR(50) NOT NULL, -- Ex: 'wizard', 'fighter'

    -- Statistiques de base
                            level INT DEFAULT 1 CHECK (level >= 1 AND level <= 20),
                            hp_max INT NOT NULL,
                            hp_current INT NOT NULL,
                            armor_class INT NOT NULL,
                            speed INT NOT NULL,

    -- Les 6 Caractéristiques (Ability Scores)
                            stat_str INT DEFAULT 10, -- Force
                            stat_dex INT DEFAULT 10, -- Dextérité
                            stat_con INT DEFAULT 10, -- Constitution
                            stat_int INT DEFAULT 10, -- Intelligence
                            stat_wis INT DEFAULT 10, -- Sagesse
                            stat_cha INT DEFAULT 10, -- Charisme

    -- Métadonnées utiles pour le MJ
                            notes TEXT, -- Notes secrètes du MJ sur ce perso
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE character_equipment (
                                     id BIGSERIAL PRIMARY KEY,
                                     character_id BIGINT NOT NULL,
                                     item_index VARCHAR(50) NOT NULL, -- Ex: 'longsword', 'potion-of-healing'
                                     quantity INT DEFAULT 1 CHECK (quantity > 0),
                                     equipped BOOLEAN DEFAULT false,  -- Est-ce qu'il le porte sur lui ?

                                     CONSTRAINT fk_character_equipment
                                         FOREIGN KEY (character_id)
                                             REFERENCES characters(id)
                                             ON DELETE CASCADE -- Si on supprime le perso, son inventaire est supprimé
);

CREATE TABLE character_spells (
                                  id BIGSERIAL PRIMARY KEY,
                                  character_id BIGINT NOT NULL,
                                  spell_index VARCHAR(50) NOT NULL, -- Ex: 'fireball', 'magic-missile'
                                  is_prepared BOOLEAN DEFAULT true, -- Le sort est-il préparé pour la journée ?

                                  CONSTRAINT fk_character_spells
                                      FOREIGN KEY (character_id)
                                          REFERENCES characters(id)
                                          ON DELETE CASCADE,

    -- Un personnage ne peut pas apprendre deux fois le même sort
                                  CONSTRAINT unique_character_spell UNIQUE (character_id, spell_index)
);