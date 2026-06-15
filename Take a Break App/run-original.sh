#!/bin/bash
# Script para executar Take a Break App usando o JAR original compilado
# Este é o executável correcto com a interface correcta

echo "========================================"
echo "Take a Break App - Sistema de Folgas"
echo "========================================"
echo ""
echo "Iniciando aplicação (usando JAR original)..."
echo ""

# Copiar base de dados se não existir
mkdir -p data
if [ ! -f data/AgendamentoFolgas.db ]; then
    echo "Preparando base de dados..."
    cp dist/data/AgendamentoFolgas.db data/
fi

# Executar o JAR original
java -jar dist/Estagio.jar
