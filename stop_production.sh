#!/bin/bash

# Script d'arrêt des services en production
# Arrête tous les conteneurs et nettoie les ressources

PROJECT_DIR=$(pwd)

echo "⏹️  Arrêt des services RPG Dashboard"
echo "====================================="

# Arrêter tous les conteneurs
echo "🛑 Arrêt des conteneurs..."
docker stop rpg-frontend rpg-backend rpg-redis-prod rpg-db 2>/dev/null || true

# Supprimer les conteneurs
echo "🗑️  Suppression des conteneurs..."
docker rm rpg-frontend rpg-backend rpg-redis-prod rpg-db 2>/dev/null || true

# Supprimer les réseaux inutilisés
echo "🌐 Nettoyage des réseaux..."
docker network prune -f

# Arrêter Docker Compose dans le backend
echo "📦 Arrêt de docker-compose dans Backend..."
cd "$PROJECT_DIR/Backend/RpgBackend" 2>/dev/null && docker compose down 2>/dev/null || true

# Arrêter Docker Compose dans le frontend
echo "📦 Arrêt de docker-compose dans Frontend..."
cd "$PROJECT_DIR/rpg" 2>/dev/null && docker compose down 2>/dev/null || true

echo ""
echo "✅ Tous les services ont été arrêtés et nettoyés"
echo "================================================"
