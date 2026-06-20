# Déploiement en Production - RPG Dashboard

## Solution simplifiée pour déploiement rapide

Ce guide explique comment déployer le projet RPG Dashboard en production en utilisant des scripts simples plutôt qu'un docker-compose complexe.

## Prérequis

- Docker et Docker Compose installés
- Ports disponibles : 8080 (backend), 5173 (frontend), 5432 (PostgreSQL), 6379 (Redis)
- 4GB de RAM minimum

## Structure de déploiement

```
.
├── start_production.sh      # Script de démarrage
├── stop_production.sh       # Script d'arrêt
├── Backend/RpgBackend/      # Backend Spring Boot
│   └── docker-compose.yml   # Configuration backend + DB
└── rpg/                     # Frontend React
    └── docker-compose.yml    # Configuration frontend
```

## Déploiement rapide

### 1. Préparation

```bash
# Cloner le projet (si ce n'est pas déjà fait)
git clone https://github.com/votre-repo/Atelier-Services-Web-RPG.git
cd Atelier-Services-Web-RPG

# Rendre les scripts exécutables
chmod +x start_production.sh stop_production.sh
```

### 2. Démarrage

```bash
# Lancer tous les services
./start_production.sh
```

Le script va :
1. Vérifier que les ports sont disponibles
2. Lancer PostgreSQL avec docker-compose du backend
3. Lancer Redis
4. Lancer le backend Spring Boot
5. Lancer le frontend React
6. Vérifier que tous les services sont prêts

### 3. Accès aux services

- **Backend (Swagger UI)** : http://localhost:8080/swagger-ui/
- **Frontend** : http://localhost:5173
- **Base de données** : localhost:5432 (user: postgres, password: root)
- **Redis** : localhost:6379

### 4. Arrêt

```bash
# Arrêter tous les services
./stop_production.sh
```

## Configuration pour la production

### Backend (Backend/RpgBackend/docker-compose.yml)

```yaml
services:
  db:
    image: postgres:16-alpine
    environment:
      POSTGRES_DB: rpg
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: root  # ⚠️ À changer pour la production
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
      - ./bdd.sql:/docker-entrypoint-initdb.d/01-bdd.sql:ro

  app:
    build:
      context: .
      dockerfile: Dockerfile
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://db:5432/rpg
      SPRING_DATASOURCE_USERNAME: postgres
      SPRING_DATASOURCE_PASSWORD: root  # ⚠️ À changer pour la production
      SPRING_CACHE_TYPE: redis
      SPRING_CACHE_REDIS_HOST: host.docker.internal  # Pour accéder à Redis depuis le backend
      SPRING_CACHE_REDIS_PORT: 6379
    ports:
      - "8080:8080"
    depends_on:
      db:
        condition: service_healthy

volumes:
  postgres_data:
```

### Frontend (rpg/docker-compose.yml)

```yaml
services:
  frontend:
    build:
      context: .
      dockerfile: Dockerfile
    ports:
      - "5173:5173"
    environment:
      - NODE_ENV=production
```

## Configuration avancée

### Variables d'environnement

Pour la production, créez un fichier `.env` dans chaque dossier :

**Backend/.env**
```
POSTGRES_PASSWORD=votre_mot_de_passe_secure
SPRING_DATASOURCE_PASSWORD=votre_mot_de_passe_secure
JWT_SECRET=votre_secret_jwt_complexe
```

**Frontend/.env**
```
VITE_API_URL=http://votre-domaine.com/api
VITE_ENV=production
```

### Sécurité

1. **Changer les mots de passe** par défaut (PostgreSQL, Redis)
2. **Configurer HTTPS** avec un reverse proxy (Nginx, Traefik)
3. **Activer l'authentification** pour ToolJet si utilisé
4. **Configurer un firewall** pour limiter l'accès aux ports

### Reverse Proxy avec Nginx

Exemple de configuration pour Nginx :

```nginx
server {
    listen 80;
    server_name rpg.votre-domaine.com;
    
    location / {
        proxy_pass http://localhost:5173;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
    
    location /api/ {
        proxy_pass http://localhost:8080/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
    
    location /swagger-ui/ {
        proxy_pass http://localhost:8080/swagger-ui/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

### Sauvegardes

Pour sauvegarder la base de données :

```bash
# Sauvegarde
docker exec rpg-db pg_dump -U postgres rpg > backup_$(date +%Y%m%d).sql

# Restauration
docker exec -i rpg-db psql -U postgres rpg < backup.sql
```

## Monitoring

### Voir les logs

```bash
# Logs du backend
docker logs -f rpg-backend-app

# Logs du frontend
docker logs -f rpg-frontend

# Logs de la base de données
docker logs -f rpg-db
```

### Surveillance des ressources

```bash
# Voir l'utilisation des ressources
docker stats

# Voir les conteneurs en cours
docker ps
```

## Mise à jour

Pour mettre à jour l'application :

```bash
# Arrêter les services
./stop_production.sh

# Mettre à jour le code
git pull origin main

# Reconstruire et redémarrer
./start_production.sh
```

## Résolution de problèmes

### Problème de port déjà utilisé

```bash
# Trouver le processus utilisant le port
sudo lsof -i :8080

# Tuer le processus
kill -9 PID
```

### Problème de connexion à la base de données

```bash
# Vérifier que la base de données est en cours d'exécution
docker ps | grep rpg-db

# Tester la connexion
docker exec -it rpg-db psql -U postgres -d rpg
```

### Problème de cache Redis

```bash
# Vérifier que Redis est en cours d'exécution
docker ps | grep rpg-redis

# Tester la connexion
docker exec -it rpg-redis-prod redis-cli ping
```

## Déploiement sur AWS

Pour un déploiement sur AWS, vous pouvez :

1. **Utiliser EC2** : Une instance t3.medium ou t3.large suffit
2. **Configurer les groupes de sécurité** pour ouvrir les ports nécessaires
3. **Utiliser RDS** pour PostgreSQL au lieu du conteneur
4. **Utiliser ElastiCache** pour Redis
5. **Configurer un Load Balancer** pour le trafic HTTP

Exemple de commande pour déployer sur une instance EC2 :

```bash
# Se connecter à l'instance EC2
ssh -i votre-cle.pem ubuntu@votre-ip-ec2

# Installer Docker
sudo apt update
sudo apt install -y docker.io docker-compose
sudo usermod -aG docker ubuntu

# Cloner le projet
git clone https://github.com/votre-repo/Atelier-Services-Web-RPG.git
cd Atelier-Services-Web-RPG

# Lancer les services
./start_production.sh
```

## Conclusion

Cette approche simplifiée permet de :

✅ **Déployer rapidement** en production
✅ **Gérer facilement** les services individuellement
✅ **Mettre à jour** sans tout redémarrer
✅ **Surveiller** chaque service séparément
✅ **Scaler** plus facilement (ex: backend et frontend sur des machines différentes)

Pour des besoins plus avancés (scaling automatique, haute disponibilité), envisagez d'utiliser Kubernetes ou AWS ECS.
