# Atelier-Services-Web-RPG
Projet Service Web

MARCHAND Aurélien - RENARD Théo


# Prochaines étapes : 
* **Liaison du backend et frontend**
* **Mise en place de test-CICD**
* **Clean Code**
* **Ajout BDD NoSQL**

---

#Commande de lancement

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
