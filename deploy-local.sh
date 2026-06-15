#!/bin/bash

set -e

echo "╔════════════════════════════════════════╗"
echo "║   TAKE A BREAK - DEPLOY LOCAL          ║"
echo "║   (MySQL 8.0 + Java 21 + Spring Boot)  ║"
echo "╚════════════════════════════════════════╝"
echo ""

PROJECT_DIR="d:\Projeto de Informática\webapp"
MYSQL_BIN="/c/Program Files/MySQL/MySQL Server 8.0/bin"
DB_USER="root"
DB_PASS="informatica"
DB_NAME="takeabreak"
PORT=8081

cd "$PROJECT_DIR"

echo "[1/6] Iniciando MySQL..."

# Verificar se MySQL está rodando
if ! nc -z localhost 3306 2>/dev/null; then
    echo "Iniciando serviço MySQL..."
    net start MySQL80 2>/dev/null || (
        echo "❌ Erro ao iniciar MySQL"
        echo "Verifica se MySQL está instalado:"
        echo "  Services → MySQL80"
        exit 1
    )
    sleep 2
fi

echo "✓ MySQL rodando"
echo ""

echo "[2/6] Configurando database..."

# Criar database e schema
echo "Criando database e schema..."
"$MYSQL_BIN/mysql" -u "$DB_USER" -p"$DB_PASS" << EOF 2>/dev/null || true
DROP DATABASE IF EXISTS $DB_NAME;
CREATE DATABASE $DB_NAME CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE $DB_NAME;
EOF

# Carregar schema.sql
echo "Populando schema..."
"$MYSQL_BIN/mysql" -u "$DB_USER" -p"$DB_PASS" "$DB_NAME" < src/main/resources/schema.sql 2>/dev/null

echo "✓ Database pronto"
echo ""

echo "[3/6] Maven build..."

# Maven clean package
echo "Compilando aplicação (isto pode levar ~1 minuto)..."
mvn -q clean package -DskipTests 2>&1 | tail -5

if [ ! -f "target/take-a-break-web-1.0.0.jar" ]; then
    echo "❌ JAR não criado"
    exit 1
fi

echo "✓ JAR pronto (26MB)"
echo ""

echo "[4/6] Checando porta 8081..."

# Matar processo anterior se existir
lsof -ti :$PORT 2>/dev/null && kill -9 $(lsof -ti :$PORT) && sleep 1 || true

if nc -z localhost $PORT 2>/dev/null; then
    echo "⚠️  Porta $PORT ocupada"
    echo "Aguardando liberação..."
    sleep 2
fi

echo "✓ Porta $PORT livre"
echo ""

echo "[5/6] Iniciando aplicação..."

# Rodar JAR em background
export DB_URL="jdbc:mysql://localhost:3306/$DB_NAME?useSSL=false&serverTimezone=UTC"
export DB_USER="$DB_USER"
export DB_PASS="$DB_PASS"

nohup java -Dserver.port=$PORT \
    -Dspring.datasource.url="$DB_URL" \
    -Dspring.datasource.username="$DB_USER" \
    -Dspring.datasource.password="$DB_PASS" \
    -jar target/take-a-break-web-1.0.0.jar \
    > ../logs/takeabreak.log 2>&1 &

APP_PID=$!
echo "PID: $APP_PID"

# Aguardar startup (max 30s)
echo "Aguardando startup..."
for i in {1..30}; do
    if curl -s http://localhost:$PORT/registo 2>/dev/null | grep -q "input"; then
        echo "✓ Aplicação online!"
        break
    fi
    echo -n "."
    sleep 1
done
echo ""

if ! curl -s http://localhost:$PORT/registo 2>/dev/null | grep -q "input"; then
    echo "⚠️  Aplicação pode estar ainda a iniciar"
    echo "Logs: more ../logs/takeabreak.log"
fi

echo ""

echo "[6/6] Verificando acesso..."

# Test HTTP
if curl -s http://localhost:$PORT/registo > /dev/null; then
    echo "✓ HTTP OK: http://localhost:$PORT"
else
    echo "⚠️  HTTP não respondendo (ainda iniciando?)"
fi

echo ""
echo "╔════════════════════════════════════════╗"
echo "║   DEPLOY COMPLETO!                    ║"
echo "╚════════════════════════════════════════╝"
echo ""
echo "📍 Acesso local:"
echo "   Browser: http://localhost:$PORT"
echo "   API: http://localhost:$PORT/api"
echo ""
echo "📊 Logs:"
echo "   tail -f ../logs/takeabreak.log"
echo ""
echo "🛑 Parar aplicação:"
echo "   kill $APP_PID"
echo ""
echo "════════════════════════════════════════"
echo ""
echo "✅ PRÓXIMA ETAPA: DNS + Nginx"
echo ""
echo "Para acesso público (takeabreak.pt):"
echo ""
echo "1️⃣  Configurar DNS CNAME:"
echo "   @ CNAME 188.250.157.96"
echo "   TTL: 300"
echo ""
echo "2️⃣  Nginx reverse proxy:"
echo "   nginx -c $(pwd)/../nginx.conf"
echo ""
echo "3️⃣  Testar:"
echo "   curl https://takeabreak.pt"
echo ""
