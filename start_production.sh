#!/bin/bash

# Script de démarrage en production pour le projet RPG Dashboard
# Ce script lance chaque service avec son propre docker-compose
# Plus simple à déployer et à gérer en production

set -e  # Arrêter en cas d'erreur

echo "🚀 Démarrage du projet RPG Dashboard en mode production"
echo "===================================================="

# Chemin du projet
PROJECT_DIR=$(pwd)

echo "📁 Dossier du projet : $PROJECT_DIR"

# Fonction pour vérifier si un port est disponible
check_port() {
    local port=$1
    if lsof -Pi :$port -sTCP:LISTEN -t >/dev/null; then
        echo "❌ Le port $port est déjà utilisé"
        exit 1
    fi
}

# Fonction pour attendre qu'un service soit prêt
await_service() {
    local service_name=$1
    local port=$2
    local endpoint=$3
    local max_attempts=30
    local attempt=1
    
    echo "⏳ Attente du démarrage de $service_name sur le port $port..."
    
    while ! curl -sf "http://localhost:$port$endpoint" > /dev/null 2>&1; do
        if [ $attempt -ge $max_attempts ]; then
            echo "❌ Échec du démarrage de $service_name"
            exit 1
        fi
        sleep 2
        attempt=$((attempt + 1))
    done
    
    echo "✅ $service_name est prêt sur le port $port"
}

# Vérifier les ports nécessaires
echo "🔍 Vérification des ports..."
check_port 5432  # PostgreSQL
check_port 6379  # Redis
check_port 8080  # Backend
check_port 5173  # Frontend

# Lancer la base de données PostgreSQL
echo "📦 Démarrage de la base de données PostgreSQL..."
cd "$PROJECT_DIR/Backend/RpgBackend"
docker compose up -d db

# Attendre que la base de données soit prête
echo "⏳ Attente de la base de données PostgreSQL..."
sleep 10

# Lancer Redis
echo "📦 Démarrage de Redis..."
docker run -d --name rpg-redis-prod -p 6379:6379 redis:7-alpine

# Lancer le backend
echo "📦 Démarrage du backend Spring Boot..."
docker compose up -d app

# Attendre que le backend soit prêt
await_service "Backend" 8080 "/swagger-ui.html"

# Lancer le frontend
echo "📦 Démarrage du frontend React..."
cd "$PROJECT_DIR/rpg"
docker compose up -d

# Attendre que le frontend soit prêt
await_service "Frontend" 5173 "/"

echo ""
echo "🎉 Tous les services sont démarrés avec succès !"
echo "================================================"
echo "🌐 Accès aux services :"
echo "  - Backend (Swagger) : http://localhost:8080/swagger-ui/"
echo "  - Frontend : http://localhost:5173"
echo "  - Base de données : localhost:5432 (user: postgres, password: root)"
echo "  - Redis : localhost:6379"
echo ""
echo "📊 Pour voir les logs :"
echo "  docker logs -f rpg-backend-app"
echo "  docker logs -f rpg-frontend"
echo ""
echo "⏹️  Pour arrêter les services :"
echo "  ./stop_production.sh"

