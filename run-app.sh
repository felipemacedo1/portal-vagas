#!/bin/bash

echo "🚀 Iniciando Portal de Vagas..."
echo "📊 Verificando PostgreSQL..."

# Verificar se PostgreSQL está rodando
if ! docker-compose ps | grep -q "Up (healthy)"; then
    echo "⚠️  PostgreSQL não está rodando. Iniciando..."
    docker-compose up -d
    echo "⏳ Aguardando PostgreSQL ficar pronto..."
    sleep 10
fi

echo "✅ PostgreSQL está rodando"
echo "🔧 Iniciando aplicação Spring Boot..."

# Rodar aplicação
mvn spring-boot:run

echo "🎯 Aplicação disponível em:"
echo "   API: http://localhost:8081"
echo "   Swagger: http://localhost:8081/swagger-ui/index.html"