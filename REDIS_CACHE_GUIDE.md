# Guide d'utilisation du cache Redis

Ce guide explique comment utiliser Redis comme cache dans le projet RPG.

## Configuration

Le cache Redis est déjà configuré dans le projet. Voici les principaux fichiers de configuration :

1. **docker-compose.yml** : Configure le service Redis et le connecte au backend
2. **CacheConfig.java** : Configuration Spring pour Redis
3. **pom.xml** : Dépendances Redis ajoutées

## Utilisation du cache

### Annotations disponibles

#### @Cacheable
Mémorise le résultat d'une méthode en cache.

```java
@Cacheable(value = "characters", key = "#id")
public CharacterDto findById(Long id) {
    // Cette méthode sera mise en cache
}
```

#### @CacheEvict
Supprime une entrée du cache.

```java
@CacheEvict(value = "characters", key = "#id")
public CharacterDto update(Long id, CharacterDto dto) {
    // Cette méthode supprimera l'entrée du cache après exécution
}
```

#### @CacheEvict (toutes les entrées)

```java
@CacheEvict(value = "characters", allEntries = true)
public CharacterDto create(CharacterDto dto) {
    // Cette méthode supprimera toutes les entrées du cache "characters"
}
```

### Exemple complet

Voici comment le cache est utilisé dans `CharacterService` :

```java
@Service
@Transactional
public class CharacterService {

    // Mise en cache de tous les personnages
    @Cacheable(value = "characters", key = "'all'")
    public List<CharacterDto> findAll() {
        // ...
    }

    // Mise en cache d'un personnage spécifique
    @Cacheable(value = "characters", key = "#id")
    public CharacterDto findById(Long id) {
        // ...
    }

    // Suppression du cache lors de la création
    @CacheEvict(value = "characters", allEntries = true)
    public CharacterDto create(CharacterDto dto) {
        // ...
    }

    // Suppression du cache lors de la mise à jour
    @CacheEvict(value = "characters", allEntries = true)
    public CharacterDto update(Long id, CharacterDto dto) {
        // ...
    }

    // Suppression du cache lors de la suppression
    @CacheEvict(value = "characters", allEntries = true)
    public void delete(Long id) {
        // ...
    }
}
```

## Configuration du cache

La configuration du cache se trouve dans `CacheConfig.java` :

```java
@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(60)) // Durée de vie : 60 minutes
                .disableCachingNullValues()
                .serializeValuesWith(
                    RedisSerializationContext.SerializationPair.fromSerializer(
                        new GenericJackson2JsonRedisSerializer()
                    )
                );
    }
}
```

### Personnalisation

Vous pouvez personnaliser la configuration :

- **Durée de vie** : Modifiez `Duration.ofMinutes(60)` pour changer le TTL
- **Sérialisation** : Vous pouvez utiliser un autre sérialiseur si nécessaire
- **Clés** : Vous pouvez utiliser des stratégies de clé plus complexes

## Vérification du cache

### Via Redis CLI

```bash
# Se connecter à Redis
docker exec -it rpg-redis redis-cli

# Voir toutes les clés
KEYS *

# Voir la valeur d'une clé
GET "characters::1"

# Supprimer une clé
DEL "characters::1"

# Vider tout le cache
FLUSHALL
```

### Via Spring Boot Actuator

Si vous activez Spring Boot Actuator, vous pouvez vérifier le cache via :

```bash
curl http://localhost:8080/actuator/caches
```

## Bonnes pratiques

1. **Noms de cache significatifs** : Utilisez des noms de cache clairs comme "characters", "items", etc.
2. **Stratégie de clé cohérente** : Utilisez des clés prévisibles et uniques
3. **Invalidation appropriée** : Toujours invalider le cache lors des mises à jour
4. **TTL raisonnable** : Ajustez la durée de vie en fonction de vos besoins
5. **Ne pas mettre en cache les données sensibles** : Évitez de mettre en cache des données personnelles ou sensibles

## Dépannage

### Le cache ne fonctionne pas ?

1. Vérifiez que Redis est en cours d'exécution : `docker ps | grep redis`
2. Vérifiez la connexion Redis : `docker exec -it rpg-redis redis-cli PING`
3. Vérifiez les logs du backend : `docker logs rpg-backend`
4. Assurez-vous que les annotations `@EnableCaching` sont présentes
5. Vérifiez que les dépendances Redis sont dans le pom.xml

### Erreurs de sérialisation

Si vous avez des erreurs de sérialisation, vous pouvez :
1. Utiliser un sérialiseur différent dans `CacheConfig`
2. Vous assurer que toutes les classes sont sérialisables
3. Utiliser des DTO simples pour le cache

## Performance

Redis offre une amélioration significative des performances pour :
- Les requêtes fréquentes
- Les données qui changent rarement
- Les calculs coûteux

Dans notre cas, les données des personnages sont mises en cache, ce qui réduit la charge sur la base de données.

## Monitoring

Vous pouvez monitorer Redis avec :

```bash
# Voir les statistiques
docker exec -it rpg-redis redis-cli INFO

# Voir l'utilisation mémoire
docker exec -it rpg-redis redis-cli INFO memory

# Voir les connexions actives
docker exec -it rpg-redis redis-cli INFO clients
```

## Sauvegarde et restauration

Les données Redis sont persistées dans un volume Docker. Pour sauvegarder :

```bash
# Trouver le volume
docker volume inspect atelier-services-web-rpg_redis_data

# Sauvegarder manuellement
docker run --rm --volumes-from rpg-redis -v $(pwd):/backup alpine tar cvf /backup/redis-backup.tar /data
```

## Sécurité

Pour la production, vous devriez :
1. Ajouter un mot de passe Redis
2. Configurer le TLS
3. Limiter l'accès au port Redis
4. Utiliser un réseau Docker isolé

Exemple de configuration sécurisée dans docker-compose.yml :

```yaml
redis:
  image: redis:7-alpine
  command: redis-server --requirepass yourpassword
  environment:
    - REDIS_PASSWORD=yourpassword
```
