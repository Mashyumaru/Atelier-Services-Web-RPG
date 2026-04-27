# Atelier-Services-Web-RPG
Projet Service Web

MARCHAND Aurélien - RENARD Théo


# Prochaines étapes : 
* **Liaison du backend et frontend**
* **Mise en place de test-CICD**
* **Clean Code**
* **Ajout BDD NoSQL**

---
# Description du projet 

## Besoins

Le but de ce projet est de mettre en place un tableau de bord complet pour les Maîtres de jeux de Donjons et Dragons. Pour cela, nous mettons en place un Dashboard avec différents métriques utiles pour les sessions :
* statistiques des joueurs
* blocs notes
* inventaire

## Web Services

De plus nous aportons une dimensions interractive pour les MJ avec différents appels afin d'améliorer l'expérience utilisation : 

* utilisation de https://www.dnd5eapi.co/api pour les sorts, les équipements et toutes les informations utiles pour le MJ en lien avec D&D 5e éditions.
* utilsation de l'api Youtube pour ajouter des musiques d'ambiance
* utilisaton de l'api OpenWeather pour rendre la météo des parties réaliste en utilisant la météo de régions existantes

---

# Commandes de lancement

## Backend : 

Se placer à la racine et taper :

```bash
# se placer dans le dossier du backend
cd ./backend/RpgBackend

# run le projet 
./mvnw spring-boot:run 
```

url du swagger : http://localhost:8080/swagger-ui/index.html#/

Prérequis pour la base de données : 
*  PostreSQL
*  Exécuter le bdd.sql pour mettre en place les bonnes tables
*  Mettre a jours le application properties avec les bonnes informations (user/pwd)

## FrontEnd : 

```bash
# se placer dans le dossier rpg
cd ./rpg

# télécharger les dépendences
npm install

# lancer le front
npm run dev
```

url du FrontEnd : http://localhost:5173/
