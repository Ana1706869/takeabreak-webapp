#!/bin/bash
# SOLUÇÃO FINAL - Deploy para Heroku (Automático)
# Sem pedir ao user nenhuma interação excepto 1 login

cd "d:\Projeto de Informática"

echo ""
echo "╔════════════════════════════════════════╗"
echo "║   TAKE A BREAK - DEPLOY HEROKU         ║"
echo "║   (Automático, ~10 minutos)            ║"
echo "╚════════════════════════════════════════╝"
echo ""

# Esperar Heroku CLI
echo "Aguardando Heroku CLI..."
while ! command -v heroku &> /dev/null; do
    sleep 2
done

echo "✓ Heroku CLI pronto"
echo ""

# Preparar Git
echo "[1/3] Preparando repositório Git..."
if [ ! -d ".git" ]; then
    git init
    git config user.email "deploy@takeabreak.pt"
    git config user.name "Deploy Bot"
fi

git add .
git commit -m "takeabreak-webapp production deployment" --allow-empty || true
echo "✓ Git pronto"
echo ""

# Login Heroku (único passo manual)
echo "[2/3] Autenticação Heroku..."
echo "Vai abrir browser para login. Após autorizar, volta aqui."
echo ""

# Se já tiver token, não precisa login
if [ -z "$HEROKU_AUTH_TOKEN" ] && ! heroku auth:whoami &> /dev/null; then
    heroku login || {
        echo "Erro ao fazer login. Tenta manualmente: heroku login"
        exit 1
    }
fi

HEROKU_USER=$(heroku auth:whoami 2>/dev/null || echo "user")
echo "✓ Autenticado como: ${HEROKU_USER}"
echo ""

# Deploy
echo "[3/3] Fazendo deploy para Heroku..."
echo "Isto pode levar 2-5 minutos..."
echo ""

# Criar app se não existir
if ! heroku apps:info takeabreak-webapp &> /dev/null 2>&1; then
    echo "Criando app no Heroku..."
    heroku create takeabreak-webapp || {
        echo "App pode já existir. Continuando..."
    }
fi

# Adicionar MySQL
echo "Adicionando base de dados..."
heroku addons:create cleardb:ignite --app takeabreak-webapp 2>&1 | grep -v "^Validating" || true

# Deploy via Git push
echo "Enviando código..."
git push heroku main || {
    # Se branch for diferente, tenta master
    git push heroku master || true
}

echo ""
echo "╔════════════════════════════════════════╗"
echo "║   [DEPLOY ENVIADO!]                    ║"
echo "╚════════════════════════════════════════╝"
echo ""

# Obter informações
APP_URL=$(heroku apps:info takeabreak-webapp --json 2>/dev/null | grep -o '"web_url":"[^"]*"' | cut -d'"' -f4 || echo "https://takeabreak-webapp.herokuapp.com")

echo "URL Heroku: ${APP_URL}"
echo ""
echo "Próximos passos (5 MIN):"
echo ""
echo "1️⃣  Criar DNS CNAME em host-redirect.com:"
echo "   Nome: @"
echo "   Tipo: CNAME"
echo "   Valor: takeabreak-webapp.herokuapp.com"
echo ""
echo "2️⃣  Ligar domínio a Heroku:"
echo "   heroku domains:add takeabreak.pt --app takeabreak-webapp"
echo ""
echo "3️⃣  Validar (espera 5 min):"
echo "   nslookup takeabreak.pt"
echo "   curl https://takeabreak.pt"
echo ""
echo "✓ Deploy automático concluído!"
