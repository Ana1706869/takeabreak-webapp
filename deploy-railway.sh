#!/bin/bash
# Deploy Webapp para Railway.app
# Uso: bash deploy-railway.sh

set -e

echo "=========================================="
echo "  Deploy Webapp para Railway.app"
echo "=========================================="
echo ""

# Cores para output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# [1] Verificar se Railway CLI está instalado
echo -e "${YELLOW}[1/5]${NC} Verificando Railway CLI..."
if ! command -v railway &> /dev/null; then
    echo -e "${YELLOW}Railway CLI não encontrado. Instalando...${NC}"
    npm install -g @railway/cli || {
        echo -e "${RED}Erro ao instalar Railway CLI${NC}"
        echo "Instala manualmente: npm install -g @railway/cli"
        exit 1
    }
fi
echo -e "${GREEN}[OK]${NC} Railway CLI pronto"
echo ""

# [2] Build local
echo -e "${YELLOW}[2/5]${NC} Compilando webapp..."
cd webapp
mvn -DskipTests clean package spring-boot:repackage
if [ ! -f "target/take-a-break-web-1.0.0.jar" ]; then
    echo -e "${RED}Erro: JAR não encontrado após compilação${NC}"
    exit 1
fi
echo -e "${GREEN}[OK]${NC} Webapp compilado"
cd ..
echo ""

# [3] Verificar se tem projeto Railway
echo -e "${YELLOW}[3/5]${NC} Verificando projeto Railway..."
if [ ! -f ".railway/config.json" ]; then
    echo -e "${YELLOW}Projeto Railway não encontrado. Criando...${NC}"
    railway init --name takeabreak-webapp || {
        echo -e "${RED}Erro ao inicializar projeto Railway${NC}"
        echo "Cria manualmente em https://railway.app"
        exit 1
    }
fi
echo -e "${GREEN}[OK]${NC} Projeto Railway pronto"
echo ""

# [4] Deploy
echo -e "${YELLOW}[4/5]${NC} Fazendo deploy para Railway..."
railway deploy --service web || {
    echo -e "${RED}Erro ao fazer deploy${NC}"
    exit 1
}
echo -e "${GREEN}[OK]${NC} Deploy completo"
echo ""

# [5] Informar URL
echo -e "${YELLOW}[5/5]${NC} Obtendo URL de acesso..."
RAILWAY_URL=$(railway domain)
echo ""
echo -e "${GREEN}=========================================="
echo "  [SUCESSO] WEBAPP ONLINE!"
echo "==========================================${NC}"
echo ""
echo -e "URL da Railway: ${GREEN}${RAILWAY_URL}${NC}"
echo ""
echo "Próximos passos:"
echo "1. Criar DNS A em host-redirect.com:"
echo "   @ | A | <IP-da-Railway> | TTL 300"
echo ""
echo "2. Adicionar domínio à Railway:"
echo "   railway link takeabreak.pt"
echo ""
echo -e "3. Validar: ${GREEN}https://takeabreak.pt${NC}"
echo ""
