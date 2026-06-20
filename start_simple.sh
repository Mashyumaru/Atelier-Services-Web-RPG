#!/bin/bash

# Script de démarrage SIMPLE pour production
# Version optimisée pour un déploiement rapide

set -e

echo "🚀 Démarrage SIMPLE du projet RPG Dashboard"
echo "============================================"

PROJECT_DIR=$(pwd)

# Vérifier et arrêter les anciens services
echo "🧹 Nettoyage des anciens services..."
docker stop rpg-frontend rpg-backend rpg-redis-prod rpg-db 2>/dev/null || true
docker rm rpg-frontend rpg-backend rpg-redis-prod rpg-db 2>/dev/null || true

# Lancer la base de données
echo "📦 Démarrage de PostgreSQL..."
cd "$PROJECT_DIR/Backend/RpgBackend"
docker compose up -d db

# Attendre la base de données
sleep 8

# Lancer Redis
echo "📦 Démarrage de Redis..."
docker run -d --name rpg-redis-prod -p 6379:6379 redis:7-alpine

# Lancer le backend
echo "📦 Démarrage du backend..."
docker compose up -d app

# Attendre le backend
sleep 15

# Lancer le frontend
echo "📦 Démarrage du frontend..."
docker run -d --name rpg-frontend -p 5173:5173 rpg-frontend

# Attendre le frontend
sleep 5

echo ""
echo "🎉 Services démarrés avec succès !"
echo "=================================="
echo "🌐 Backend : http://localhost:8080/swagger-ui/"
echo "🌐 Frontend: http://localhost:5173"
echo "📊 Base de données: localhost:5432"
echo "🔥 Cache Redis: localhost:6379"
echo ""
echo "📝 Pour arrêter: docker stop rpg-frontend rpg-backend rpg-redis-prod rpg-db"
