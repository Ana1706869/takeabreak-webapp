#!/bin/bash

echo "╔════════════════════════════════════════╗"
echo "║   DEPLOY RENDER.COM - TAKE A BREAK     ║"
echo "║   (Free tier, SSL automático)          ║"
echo "╚════════════════════════════════════════╝"
echo ""

# Verificar se Render CLI está instalado
if ! command -v render &> /dev/null; then
    echo "Instalando Render CLI..."
    # Render não tem CLI oficial, vamos usar web manual ou script
    # A forma mais rápida é criar ficheiros render.yaml e fazer login web
    echo "⚠️  Render CLI não disponível"
    echo "Vou criar ficheiros de configuração para upload manual..."
fi

PROJECT_DIR="d:\\Projeto de Informática"
cd "$PROJECT_DIR"

echo ""
echo "[1/3] Criar ficheiro render.yaml (configuração do serviço)..."

# Criar render.yaml com configuração do serviço
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

echo "[2/3] Criar .renderignore (arquivos a ignorar no build)..."

cat > .renderignore << 'EOF'
logs/
certs/
*.log
.git/
.vscode/
node_modules/
target/
EOF

echo "✓ .renderignore criado"
echo ""

echo "[3/3] Instruções para deploy manual..."
echo ""
echo "╔════════════════════════════════════════════════════════════╗"
echo "║  PRÓXIMAS ETAPAS (Manual no browser)                       ║"
echo "╚════════════════════════════════════════════════════════════╝"
echo ""
echo "1. Abrir: https://dashboard.render.com"
echo "   - Clique em 'Sign up' ou 'Sign in'"
echo "   - Use email + senha ou GitHub"
echo ""
echo "2. Após login, clique em 'New Web Service'"
echo ""
echo "3. Escolha 'Public Git repository' ou 'Build and deploy from GitHub'"
echo "   Se tiver GitHub:"
echo "   - Conectar repositório GitHub: https://github.com/your-username/takeabreak"
echo "   - Branch: main ou master"
echo ""
echo "4. Se não tiver GitHub, fazer upload direto:"
echo "   - Compactar pasta: zip -r takeabreak.zip ."
echo "   - Upload direto na web"
echo ""
echo "5. Render automaticamente:"
echo "   - Detecta Dockerfile"
echo "   - Faz build (2-5 minutos)"
echo "   - Deploy automático"
echo "   - Gera URL pública (takeabreak-xxxxx.onrender.com)"
echo ""
echo "6. Depois do deploy estar pronto ('Live'):"
echo "   - Copiar URL do Render"
echo "   - Em host-redirect.com:"
echo "     Nome: @"
echo "     Tipo: CNAME"
echo "     Valor: [URL do Render]"
echo "     TTL: 300"
echo ""
echo "7. Depois criar custom domain no Render:"
echo "   - Settings → Custom Domains"
echo "   - Adicionar: takeabreak.pt"
echo ""
echo "Render vai usar SSL automático (Let's Encrypt)"
echo ""
echo "╔════════════════════════════════════════════════════════════╗"
echo "║  Abrindo browser em Render.com...                          ║"
echo "╚════════════════════════════════════════════════════════════╝"

# Tentar abrir no browser
if command -v start &> /dev/null; then
    start https://dashboard.render.com
elif command -v open &> /dev/null; then
    open https://dashboard.render.com
else
    echo ""
    echo "Abra manualmente: https://dashboard.render.com"
fi
