# Déploiement Production - RPG Dashboard

## Solution ultra-simple pour déploiement rapide

Cette documentation explique comment déployer rapidement le projet en production avec une approche minimaliste.

## Prérequis

- Docker installé
- Ports disponibles: 8080, 5173, 5432, 6379
- 4GB RAM minimum

## Déploiement en 3 étapes

### 1. Préparation

```bash
# Cloner le projet
git clone https://github.com/votre-repo/Atelier-Services-Web-RPG.git
cd Atelier-Services-Web-RPG

# Construire les images Docker
docker build -t rpg-frontend -f ./rpg/Dockerfile ./rpg
cd Backend/RpgBackend
docker build -t rpgbackend-app .
cd ../..
```

### 2. Démarrage

```bash
# Lancer tous les services
./start_simple.sh
```

### 3. Accès

- **Backend (Swagger)**: http://localhost:8080/swagger-ui/
- **Frontend**: http://localhost:5173
- **Base de données**: localhost:5432 (user: postgres, password: root)
- **Redis**: localhost:6379

## Arrêt

```bash
# Arrêter tous les services
docker stop rpg-frontend rpg-backend rpg-redis-prod rpg-db
```

## Structure des services

```
┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│   Frontend   │    │   Backend    │    │  PostgreSQL  │
│   React      │    │  Spring Boot │    │  Base de    │
│   Port: 5173 │    │  Port: 8080  │    │  données    │
└──────┬───────┘    └──────┬───────┘    └──────┬───────┘
       │                  │                   │
       └──────────────────┘                   │
                                              │
                                      ┌───────▼───────┐
                                      │    Redis     │
                                      │  Cache        │
                                      │  Port: 6379   │
                                      └───────────────┘
```

## Configuration

### Backend

Le backend utilise ces variables d'environnement (dans Backend/RpgBackend/docker-compose.yml):

```yaml
environment:
  SPRING_DATASOURCE_URL: jdbc:postgresql://db:5432/rpg
  SPRING_DATASOURCE_USERNAME: postgres
  SPRING_DATASOURCE_PASSWORD: root  # ⚠️ À changer en production
  SPRING_CACHE_TYPE: redis
  SPRING_CACHE_REDIS_HOST: host.docker.internal
  SPRING_CACHE_REDIS_PORT: 6379
```

### Frontend

Le frontend utilise Vite en mode preview pour la production.

## Pour la production réelle

### Sécurité

1. **Changer les mots de passe** dans docker-compose.yml
2. **Configurer HTTPS** avec Nginx/Traefik
3. **Activer l'authentification**
4. **Configurer un firewall**

### Exemple Nginx

```nginx
server {
    listen 80;
    server_name rpg.votre-domaine.com;
    
    location / {
        proxy_pass http://localhost:5173;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
    
    location /api/ {
        proxy_pass http://localhost:8080/;
        proxy_set_header Host $host;
    }
}
```

### Déploiement AWS

Pour AWS EC2:

```bash
# Se connecter
ssh -i votre-cle.pem ubuntu@votre-ip

# Installer Docker
sudo apt update
sudo apt install -y docker.io
sudo usermod -aG docker ubuntu

# Cloner et lancer
git clone https://github.com/votre-repo/Atelier-Services-Web-RPG.git
cd Atelier-Services-Web-RPG
./start_simple.sh
```

## Monitoring

```bash
# Voir les logs
docker logs -f rpg-backend
docker logs -f rpg-frontend

# Voir l'utilisation
docker stats
```

## Mise à jour

```bash
# Arrêter
docker stop rpg-frontend rpg-backend rpg-redis-prod rpg-db

# Mettre à jour
git pull origin main

# Reconstruire
docker build -t rpg-frontend -f ./rpg/Dockerfile ./rpg
cd Backend/RpgBackend
docker build -t rpgbackend-app .
cd ../..

# Redémarrer
./start_simple.sh
```

## Résolution de problèmes

### Port déjà utilisé

```bash
# Trouver le processus
sudo lsof -i :8080

# Tuer le processus
kill -9 PID
```

### Base de données

```bash
# Vérifier
docker logs rpg-db

# Se connecter
docker exec -it rpg-db psql -U postgres -d rpg
```

### Redis

```bash
# Tester
docker exec -it rpg-redis-prod redis-cli ping
```

## Avantages de cette approche

✅ **Simple**: Un seul script pour tout lancer
✅ **Rapide**: Déploiement en moins de 2 minutes
✅ **Fiable**: Services indépendants
✅ **Portable**: Fonctionne sur n'importe quel serveur avec Docker
✅ **Maintenable**: Facile à mettre à jour

Parfait pour un MVP ou un déploiement rapide en production !
