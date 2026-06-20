# Configuration Docker pour le projet RPG

Ce projet utilise Docker pour orchestrer les services suivants :
- Backend Spring Boot
- Frontend React
- Base de données PostgreSQL
- Cache Redis
- Base de données low-code (ToolJet)

## Prérequis
- Docker
- Docker Compose

## Structure des fichiers

```
.
├── docker-compose.yml          # Configuration principale
├── Backend/RpgBackend/          # Backend Spring Boot
│   ├── Dockerfile              # Dockerfile pour le backend
│   └── docker-compose.yml      # Ancienne configuration (obsolète)
├── rpg/                        # Frontend React
│   └── Dockerfile              # Dockerfile pour le frontend
└── .github/workflows/ci-cd.yml # Pipeline CI/CD
```

## Services

### 1. Backend (Spring Boot)
- Port: 8080
- Dépendances: PostgreSQL, Redis
- Configuration Redis: Activé pour le cache

### 2. Frontend (React)
- Port: 5173
- Dépendances: Backend

### 3. Base de données (PostgreSQL)
- Port: 5432
- Utilisateur: postgres
- Mot de passe: root
- Base de données: rpg

### 4. Cache (Redis)
- Port: 6379
- Utilisé pour le cache applicatif

### 5. Base de données low-code (ToolJet)
- Port: 3000
- Connectée à la même base PostgreSQL
- Permet de créer des interfaces sans code

## Configuration Redis pour le backend

Le backend est configuré pour utiliser Redis comme cache. Les propriétés suivantes sont définies :

```properties
spring.cache.type=redis
spring.cache.redis.host=redis
spring.cache.redis.port=6379
```

## Pipeline CI/CD

La pipeline GitHub Actions inclut :
1. Construction du backend et du frontend
2. Exécution des tests
3. Déploiement sur Docker Hub
4. Déploiement sur le serveur de production

## Commandes utiles

### Démarrer tous les services
```bash
docker-compose up -d
```

### Arrêter tous les services
```bash
docker-compose down
```

### Reconstruire les images
```bash
docker-compose build --no-cache
```

### Voir les logs
```bash
docker-compose logs -f
```

### Accéder aux services
- Backend: http://localhost:8080
- Frontend: http://localhost:5173
- ToolJet (low-code): http://localhost:3000
- Redis: redis://localhost:6379
- PostgreSQL: postgres://postgres:root@localhost:5432/rpg

## Configuration du cache Redis dans le backend

Pour utiliser Redis comme cache dans votre application Spring Boot, ajoutez les dépendances suivantes dans votre `pom.xml` :

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

Puis configurez le cache dans votre application :

```java
@Configuration
@EnableCaching
public class CacheConfig {
    
    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(60))
                .disableCachingNullValues()
                .serializeValuesWith(
                    RedisSerializationContext.SerializationPair.fromSerializer(
                        new GenericJackson2JsonRedisSerializer()
                    )
                );
    }
}
```

## Utilisation du cache

Pour utiliser le cache dans vos services :

```java
@Service
public class MyService {
    
    @Cacheable(value = "myCache", key = "#id")
    public MyEntity getById(Long id) {
        // Logique pour récupérer l'entité
    }
    
    @CacheEvict(value = "myCache", key = "#entity.id")
    public MyEntity update(MyEntity entity) {
        // Logique pour mettre à jour l'entité
    }
}
```

## Base de données low-code (ToolJet)

ToolJet est une plateforme open-source pour créer des applications internes sans code. Elle est connectée à votre base de données PostgreSQL existante.

Pour accéder à l'interface :
1. Allez sur http://localhost:3000
2. Créez un compte administrateur
3. Commencez à créer vos applications

## Variables d'environnement

Vous pouvez personnaliser les variables d'environnement dans le fichier `docker-compose.yml` ou en créant un fichier `.env` :

```env
# PostgreSQL
POSTGRES_DB=rpg
POSTGRES_USER=postgres
POSTGRES_PASSWORD=root

# Redis
REDIS_PORT=6379

# Backend
BACKEND_PORT=8080

# Frontend
FRONTEND_PORT=5173

# ToolJet
TOOLJET_PORT=3000
```

## Notes

- Assurez-vous que les ports 8080, 5173, 3000, 5432 et 6379 sont disponibles sur votre machine
- Les volumes Docker sont utilisés pour persister les données de PostgreSQL et Redis
- La configuration actuelle utilise des images légères (alpine) pour une meilleure performance
