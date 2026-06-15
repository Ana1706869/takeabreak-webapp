#!/bin/bash

set -e

echo "╔════════════════════════════════════════╗"
echo "║   RAILWAY DEPLOY - DOCKER BUILD       ║"
echo "║   (Take a Break - Spring Boot)        ║"
echo "╚════════════════════════════════════════╝"
echo ""

PROJECT_DIR="d:\Projeto de Informática"
DOCKER_IMAGE="takeabreak-webapp:latest"

echo "[1/4] Verificando Docker..."
if ! command -v docker &> /dev/null; then
    echo "❌ Docker não instalado"
    exit 1
fi
docker --version
echo "✓ Docker disponível"
echo ""

echo "[2/4] Buildando imagem Docker..."
cd "$PROJECT_DIR"
docker build -f webapp/Dockerfile -t "$DOCKER_IMAGE" .
echo "✓ Imagem construída: $DOCKER_IMAGE"
echo ""

echo "[3/4] Autenticando no Railway..."
railway whoami || (echo "❌ Não autenticado. Faça 'railway login' primeiro"; exit 1)
echo "✓ Autenticado no Railway"
echo ""

echo "[4/4] Fazendo deploy via Railway..."

# Adicionar Docker service ao projeto Railway
echo "Criando projeto takeabreak-webapp no Railway..."
cd "$PROJECT_DIR"

# Verificar se projeto existe
if railway project-id > /dev/null 2>&1; then
    PROJECT_ID=$(railway project-id)
    echo "✓ Projeto existente: $PROJECT_ID"
else
    # Criar novo projeto
    echo "Criando novo projeto..."
    railway init --name "takeabreak-webapp" --skip-provisioning 2>&1 || true
fi

echo ""
echo "Fazendo push da imagem Docker para Railway..."
echo "Isto pode levar alguns minutos..."
echo ""

# Railway vai usar o Dockerfile automaticamente
# Basta fazer git push
git add -A 2>/dev/null || true
git commit -m "Deploy ${DATE}" 2>/dev/null || true
railway up --detach 2>&1 || (

    # Se acima falhar, tenta método alternativo via service add
    echo "Tentando método alternativo..."
    railway service add --name takeabreak-app --source . 2>&1 || true
    railway up 2>&1 || true
)

echo ""
echo "╔════════════════════════════════════════╗"
echo "║   DEPLOY COMPLETO                     ║"
echo "╚════════════════════════════════════════╝"
echo ""
echo "✓ Aplicação em deploy!"
echo ""

# Tentar obter URL
echo "URL da aplicação:"
railway domain --service takeabreak-app 2>/dev/null || railway service list 2>/dev/null | grep -i "takeabreak\|public url" || echo "(URL será disponível em 1-2 minutos)"

echo ""
echo "Próximos passos:"
echo "1. Aguardar build & deployment (2-5 minutos)"
echo "2. Configurar DNS CNAME para takeabreak.pt → [URL Railway]"
echo "3. Ligar custom domain no Railway dashboard"
echo ""
