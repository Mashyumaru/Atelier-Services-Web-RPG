# 🐉 Projet : Dashboard de Maître du Jeu (GM Screen)

Ce document définit les spécifications, l'architecture et les intégrations pour l'application de gestion de campagnes de JDR.

---

## 🎯 1. Analyse du Besoin

Le projet vise à créer un **outil centralisé pour le Maître du Jeu (MJ)** afin de fluidifier la gestion des parties et d'augmenter l'immersion des joueurs.

### Fonctionnalités principales :
* **Gestion des personnages :** Suivi des points de vie (HP), du niveau, de l'inventaire et des sorts préparés.
* **Encyclopédie interactive :** Recherche instantanée de sorts, monstres et objets via le SRD (System Reference Document).
* **Immersion dynamique :** Génération de météo locale basée sur des données réelles et intégration d'un soundboard musical.
* **Architecture légère :** Utilisation de l'API externe comme source de données de référence pour éviter une base de données trop lourde.

---

## 🏗️ 2. Architecture du Backend (Spring Boot)

L'application utilise une architecture **N-Tier (en couches)** pour garantir la maintenabilité et la séparation des responsabilités.

### Structure des dossiers :
* **`controller/`** : Définit les points d'entrée de l'API. Reçoit les requêtes du dashboard (ex: `GET /api/characters`).
* **`service/`** : Contient la logique métier. C'est ici que l'on fusionne les données de la base SQL et les données des API externes.
* **`integration/`** (ou `client/`) : Gère la communication HTTP vers les API externes (D&D 5e, OpenWeather, YouTube).
* **`repository/`** : Gère les accès à la base de données PostgreSQL via Spring Data JPA.
* **`entity/`** : Représente les tables SQL sous forme d'objets Java (ex: `Character`, `Campaign`).
* **`dto/`** : Objets de transfert pour envoyer uniquement les données nécessaires au frontend.

### Flux de données :
1. Le **Frontend** demande une fiche perso.
2. Le **Controller** appelle le **Service**.
3. Le **Service** récupère l'état dynamique (HP, index des sorts) via le **Repository** (PostgreSQL).
4. Le **Service** complète les informations (descriptions, images) via les **Clients d'intégration** (API externes).
5. Le **Service** renvoie un **DTO** complet au **Controller**, qui l'envoie au **Frontend**.

---

## 🔌 3. API Externes Utilisées

| API | Rôle dans le projet | Authentification |
| :--- | :--- | :--- |
| **D&D 5e API** | Encyclopédie des sorts, monstres, races et équipements. | Aucune (Publique) |
| **OpenWeatherMap** | Génération de météo dynamique pour l'ambiance du monde. | Clé API (Secrète) |
| **YouTube API** | Recherche de musiques d'ambiance et soundboard intégré. | Clé API Google (Secrète) |

---

## 🗄️ 4. Modèle de Données (SQL)

La base de données **PostgreSQL** stocke uniquement les données propres à l'utilisateur et à sa session de jeu :

* **`characters`** : ID, nom, niveau, HP actuels, caractéristiques (force, dex, etc.), index de race/classe.
* **`character_equipment`** : Lien entre un personnage et un `item_index` de l'API D&D.
* **`character_spells`** : Lien entre un personnage et un `spell_index` de l'API D&D.
