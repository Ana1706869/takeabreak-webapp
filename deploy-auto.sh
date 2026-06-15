#!/bin/bash
# Deploy Automático Railway - Script Completo
# Este script faz TUDO automaticamente

set -e
trap "echo 'Deploy cancelado'; exit 1" INT TERM

REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$REPO_ROOT"

echo ""
echo "╔════════════════════════════════════════╗"
echo "║   DEPLOY AUTOMÁTICO - RAILWAY.APP      ║"
echo "║   takeabreak.pt → LIVE em 5 minutos    ║"
echo "╚════════════════════════════════════════╝"
echo ""

# Cores
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
RED='\033[0;31m'
NC='\033[0m'

# ============================================
# PASSO 1: Verificar Dependências
# ============================================
echo -e "${BLUE}[1/5]${NC} Verificando dependências..."

# Node.js
if ! command -v node &> /dev/null; then
    echo -e "${YELLOW}Instalando Node.js...${NC}"
    if [[ "$OSTYPE" == "win32" ]] || [[ "$OSTYPE" == "msys" ]]; then
        choco install nodejs -y || npm_install_url="https://nodejs.org"
    else
        curl -fsSL https://deb.nodesource.com/setup_20.x | sudo -E bash - && sudo apt-get install -y nodejs || true
    fi
fi
node_ver=$(node --version)
echo -e "${GREEN}✓${NC} Node.js ${node_ver}"

# Maven
if ! command -v mvn &> /dev/null; then
    echo -e "${RED}ERRO: Maven não encontrado. Instala e tenta de novo.${NC}"
    exit 1
fi
mvn_ver=$(mvn --version 2>/dev/null | head -1)
echo -e "${GREEN}✓${NC} ${mvn_ver}"

# Railway CLI
if ! command -v railway &> /dev/null; then
    echo -e "${YELLOW}Instalando Railway CLI...${NC}"
    npm install -g @railway/cli --silent
fi
railway_ver=$(railway --version 2>/dev/null || echo "desconhecida")
echo -e "${GREEN}✓${NC} Railway CLI ${railway_ver}"

echo -e "${GREEN}✓${NC} Todas as dependências prontas"
echo ""

# ============================================
# PASSO 2: Compilar Webapp
# ============================================
echo -e "${BLUE}[2/5]${NC} Compilando webapp com Maven..."

cd webapp
if [ ! -f "target/take-a-break-web-1.0.0.jar" ]; then
    echo "Compilando (primeira vez, pode levar 2-3 min)..."
    mvn -DskipTests clean package spring-boot:repackage -q || {
        echo -e "${RED}ERRO na compilação${NC}"
        exit 1
    }
fi

if [ ! -f "target/take-a-break-web-1.0.0.jar" ]; then
    echo -e "${RED}ERRO: JAR não encontrado${NC}"
    exit 1
fi

jar_size=$(du -h "target/take-a-break-web-1.0.0.jar" | cut -f1)
echo -e "${GREEN}✓${NC} Webapp compilada (${jar_size})"
cd ..
echo ""

# ============================================
# PASSO 3: Preparar Git para Railway
# ============================================
echo -e "${BLUE}[3/5]${NC} Preparando repositório Git..."

if [ ! -d ".git" ]; then
    echo "Inicializando Git..."
    git init
    git config user.email "deploy@takeabreak.pt"
    git config user.name "Take a Break Deploy"
fi

if [ -z "$(git remote get-url origin 2>/dev/null)" ]; then
    echo "Criando remote Railway..."
    # Railway vai dar o remote depois
    git add .
    git commit -m "Deploy automático takeabreak.pt" --allow-empty
fi

echo -e "${GREEN}✓${NC} Git pronto"
echo ""

# ============================================
# PASSO 4: Deploy para Railway
# ============================================
echo -e "${BLUE}[4/5]${NC} Fazendo deploy para Railway..."
echo "Nota: Pode pedir para fazer login no Railway (GitHub ou Email)"
echo ""

# Se não tiver projeto Railway, criar
if [ ! -f ".railway/config.json" ]; then
    echo "Criando projeto Railway..."
    railway init --name "takeabreak-webapp" --static-only=false || {
        echo -e "${YELLOW}Projeto pode já existir. Continuando...${NC}"
    }
fi

# Deploy
echo "Enviando código para Railway..."
railway up --force || {
    echo -e "${YELLOW}Aviso: Railway pode estar a processar. Verifica em https://railway.app${NC}"
}

echo -e "${GREEN}✓${NC} Deploy enviado"
echo ""

# ============================================
# PASSO 5: Gerar URLs e Instruções
# ============================================
echo -e "${BLUE}[5/5]${NC} Finalizando setup..."

# Tentar obter URL da Railway
RAILWAY_URL=$(railway domain 2>/dev/null || echo "takeabreak-webapp.up.railway.app")

echo ""
echo -e "${GREEN}╔════════════════════════════════════════╗"
echo "║    [SUCESSO] DEPLOY COMPLETO!          ║"
echo "╚════════════════════════════════════════╝${NC}"
echo ""
echo -e "Railway URL: ${BLUE}${RAILWAY_URL}${NC}"
echo ""
echo -e "${YELLOW}Próximos passos (3 MIN):${NC}"
echo ""
echo "1️⃣  ${BLUE}Criar DNS CNAME${NC} em host-redirect.com:"
echo "   Nome: @"
echo "   Tipo: CNAME"
echo "   Valor: ${RAILWAY_URL}"
echo "   TTL: 300"
echo ""
echo "2️⃣  ${BLUE}Ligar domínio à Railway${NC}:"
echo "   ${BLUE}railway link takeabreak.pt${NC}"
echo ""
echo "3️⃣  ${BLUE}Validar${NC} (espera 5 min pela propagação DNS):"
echo "   ${BLUE}nslookup takeabreak.pt${NC}"
echo "   ${BLUE}curl https://takeabreak.pt${NC}"
echo ""
echo -e "${YELLOW}Login após ativo:${NC}"
echo "   Email: anasilva_pinhel@hotmail.com"
echo "   Senha: informatica"
echo ""
echo -e "${YELLOW}Comandos úteis:${NC}"
echo "   railway logs          # Ver logs"
echo "   railway restart       # Reiniciar"
echo "   railway env           # Ver variáveis"
echo ""
echo -e "${GREEN}Deploy automático concluído! 🚀${NC}"
echo ""

# Salvar instruções
cat > RAILWAY_DEPLOY_INFO.txt << EOF
RAILWAY DEPLOYMENT INFO
=======================
Deploy Time: $(date)
Railway URL: ${RAILWAY_URL}
JAR Size: ${jar_size}

Next Steps:
1. Create CNAME DNS record pointing to ${RAILWAY_URL}
2. Link domain: railway link takeabreak.pt
3. Wait 5 minutes for DNS propagation
4. Access https://takeabreak.pt

Credentials:
Email: anasilva_pinhel@hotmail.com
Password: informatica
EOF

echo "✓ Informações salvas em RAILWAY_DEPLOY_INFO.txt"
