#!/bin/bash

echo "Démarrage des services Docker..."

docker-compose up -d --build

echo "Services démarrés :"
echo "- Backend: http://localhost:8080"
echo "- Frontend: http://localhost:5173"
echo "- ToolJet (low-code): http://localhost:3000"
echo "- Redis: redis://localhost:6379"
echo "- PostgreSQL: postgres://postgres:root@localhost:5432/rpg"
