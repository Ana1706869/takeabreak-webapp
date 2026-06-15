#!/bin/bash
# Deploy Railway - Semi-Automático (apenas 1 login manual necessário)

set -e

REPO_ROOT="d:\Projeto de Informática"
cd "$REPO_ROOT"

echo ""
echo "╔════════════════════════════════════════╗"
echo "║   DEPLOY RAILWAY - FASE 2              ║"
echo "║   (Build já completo: JAR 26MB)        ║"
echo "╚════════════════════════════════════════╝"
echo ""

# Cores
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
RED='\033[0;31m'
NC='\033[0m'

# ============================================
# ETAPA 1: Autenticação Railway
# ============================================
echo -e "${BLUE}[1/3]${NC} Verificando autenticação Railway..."

if ! railway status &> /dev/null; then
    echo -e "${YELLOW}Requerido:${NC} Login no Railway"
    echo ""
    echo "Vai abrir o browser para autenticação."
    echo "Escolhe GitHub ou Email, autoriza e volta aqui."
    echo ""
    read -p "Pressiona ENTER para começar login..." _
    
    railway login || {
        echo -e "${RED}Erro ao fazer login. Tenta manualmente:${NC}"
        echo "  railway login"
        exit 1
    }
fi

railway_user=$(railway whoami 2>/dev/null || echo "unknown")
echo -e "${GREEN}✓${NC} Autenticado como: ${railway_user}"
echo ""

# ============================================
# ETAPA 2: Inicializar/Verificar Projeto Railway
# ============================================
echo -e "${BLUE}[2/3]${NC} Preparando projeto Railway..."

if [ ! -f ".railway/config.json" ]; then
    echo "Criando novo projeto Railroad..."
    railway init --name "takeabreak-webapp" || {
        echo -e "${YELLOW}Projeto pode já existir. Continuando...${NC}"
    }
fi

echo -e "${GREEN}✓${NC} Projeto Railway pronto"
echo ""

# ============================================
# ETAPA 3: Deploy
# ============================================
echo -e "${BLUE}[3/3]${NC} Fazendo deploy para Railway..."
echo "Isto pode levar 2-5 minutos..."
echo ""

railway up --force || {
    echo -e "${RED}Erro ao fazer deploy. Logs:${NC}"
    railway logs -f || true
    exit 1
}

echo ""
echo -e "${GREEN}╔════════════════════════════════════════╗"
echo "║   [SUCESSO] DEPLOY CONCLUÍDO!          ║"
echo "╚════════════════════════════════════════╝${NC}"
echo ""

# ============================================
# ETAPA 4: Obter informações
# ============================================
echo -e "${BLUE}[4/4]${NC} Finalizando..."

echo "Abrindo painel Railway..."
railway open || true

# Tentar obter URL
RAILWAY_URL=$(railway domain 2>/dev/null || echo "takeabreak-webapp.up.railway.app")

echo ""
echo -e "${YELLOW}Próximos passos (5 MIN):${NC}"
echo ""
echo "1️⃣  ${BLUE}Criar registo CNAME em host-redirect.com:${NC}"
echo "   Nome: @"
echo "   Tipo: CNAME"
echo "   Valor: ${RAILWAY_URL}"
echo "   TTL: 300"
echo ""
echo "2️⃣  ${BLUE}Ligar domínio a Railway:${NC}"
echo "   ${BLUE}railway link takeabreak.pt${NC}"
echo ""
echo "3️⃣  ${BLUE}Validar (espera 5 min pela propagação DNS):${NC}"
echo "   ${BLUE}nslookup takeabreak.pt${NC}"
echo "   ${BLUE}curl https://takeabreak.pt${NC}"
echo ""
echo -e "${YELLOW}Login:${NC}"
echo "   Email: anasilva_pinhel@hotmail.com"
echo "   Senha: informatica"
echo ""
echo -e "${YELLOW}Comandos úteis:${NC}"
echo "   railway logs -f       # Ver logs em real-time"
echo "   railway restart       # Reiniciar"
echo "   railway env list      # Ver variáveis de ambiente"
echo ""
echo -e "${GREEN}Deploy concluído com sucesso! 🚀${NC}"
echo ""

# Salvar info
cat > RAILWAY_DEPLOY.info << EOF
DEPLOYMENT CONCLUÍDO
====================
Data: $(date)
Railway CLI: $(railway --version)
Projeto: takeabreak-webapp
URL Railway: ${RAILWAY_URL}
JAR: 26MB

Próximos: DNS CNAME + railway link takeabreak.pt
EOF

echo "✓ Informações salvas em RAILWAY_DEPLOY.info"
