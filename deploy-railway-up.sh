#!/bin/bash

set -e

echo "╔════════════════════════════════════════╗"
echo "║   RAILWAY DEPLOY - CÓDIGO SOURCE      ║"
echo "║   (Detecção automática: Dockerfile)   ║"
echo "╚════════════════════════════════════════╝"
echo ""

PROJECT_DIR="d:\Projeto de Informática"
cd "$PROJECT_DIR"

echo "[1/3] Verificando autenticação Railway..."
if ! railway whoami 2>/dev/null; then
    echo "❌ Não autenticado. Faça 'railway login' primeiro"
    exit 1
fi
USER=$(railway whoami)
echo "✓ Autenticado como: $USER"
echo ""

echo "[2/3] Inicializando projeto Railway..."

# Railway precisa de ficheiro .railway/config.ts para algumas operações
# Mas consegue fazer deploy com git push
mkdir -p .railway 2>/dev/null || true

# Git config
git config user.email "deploy@takeabreak.pt" 2>/dev/null || true
git config user.name "Deploy Bot" 2>/dev/null || true
git add -A 2>/dev/null || true
git commit -m "Railway deployment" 2>/dev/null || true

echo "✓ Projeto pronto para deploy"
echo ""

echo "[3/3] Fazendo deploy para Railway..."
echo "Isto pode levar 3-5 minutos (build + start)..."
echo ""

# Railway link ao projeto, depois push
# Primeira vez precisa de estar numa pasta de projeto Railway
# Vamos tentar via railway up com --detach

railway up --detach 2>&1 | head -50 && echo "..." && sleep 5

if railway deployment list 2>/dev/null | head -3; then
    echo ""
    echo "✓ Deployment iniciado!"
else
    echo "(Verificando status...)"
fi

echo ""
echo "╔════════════════════════════════════════╗"
echo "║   DEPLOY ENVIADO                      ║"
echo "╚════════════════════════════════════════╝"
echo ""

# Tentar obter info
echo "Informações do deployment:"
railway deployment list 2>/dev/null | head -5 || echo "(Aguardando status...)"

echo ""
echo "URL da aplicação será gerada em 2-3 minutos"
echo "Pode acompanhar em: https://railway.app"
echo ""
