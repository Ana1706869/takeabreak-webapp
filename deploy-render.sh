#!/bin/bash

set -e

echo "╔════════════════════════════════════════╗"
echo "║   TAKE A BREAK - DEPLOY RENDER.COM     ║"
echo "║   (Free tier, sem trial)               ║"
echo "╚════════════════════════════════════════╝"
echo ""

PROJECT_DIR="d:\Projeto de Informática"
cd "$PROJECT_DIR"

echo "[1/4] Criando ficheiro render.yaml..."

cat > render.yaml << 'EOF'
services:
  - type: web
    name: takeabreak-webapp
    env: docker
    region: frankfurt
    plan: free
    branch: main
    dockerfilePath: ./Take a Break Web/Dockerfile
    dockerContext: ./Take a Break Web
    
    envVars:
      - key: SPRING_PROFILES_ACTIVE
        value: prod
      - key: PORT
        value: 10000
      - key: DB_URL
        sync: false
      - key: DB_USER
        sync: false
      - key: DB_PASS
        sync: false
EOF

echo "✓ render.yaml criado"
echo ""

echo "[2/4] Git commit..."
git add render.yaml "Take a Break Web/Dockerfile" "Take a Break Web/pom.xml" "Take a Break Web/src/"
git commit -m "Render.com deployment setup" 2>/dev/null || echo "(Nada para commitar)"
echo "✓ Git pronto"
echo ""

echo "[3/4] Push para GitHub..."
echo ""
echo "PRÓXIMO PASSO MANUAL:"
echo "1. Abrir https://dashboard.render.com"
echo "2. Clicar 'New +'  → 'Web Service'"
echo "3. Conectar GitHub repo: https://github.com/seu-usuario/takeabreak"
echo "4. Selecionar branch 'main'"
echo "5. Render vai detectar render.yaml automaticamente"
echo "6. Clique Deploy"
echo ""
echo "Render vai:"
echo "  - Fazer build da imagem Docker"
echo "  - Criar MySQL database"
echo "  - Deploy da aplicação"
echo "  - Fornecer URL pública"
echo ""
echo "⏱️  Tempo estimado: 3-5 minutos"
echo ""

echo "[4/4] Instruções para DNS..."
echo ""
echo "Após deploy estar pronto (status: Live em https://dashboard.render.com):"
echo ""
echo "1. Copiar URL pública do Render (ex: takeabreak-webapp-xxxxx.onrender.com)"
echo "2. Em host-redirect.com DNS:"
echo "   - CNAME @ → takeabreak-webapp-xxxxx.onrender.com"
echo "3. Render auto-SSL (Let's Encrypt)"
echo ""

echo "╔════════════════════════════════════════╗"
echo "║   SETUP COMPLETO                      ║"
echo "║   Vá para: https://dashboard.render.com│"
echo "╚════════════════════════════════════════╝"
