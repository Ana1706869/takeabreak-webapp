#!/bin/bash
# Script para executar o Take a Break App
# Autor: Recuperação de Bytecode

is_windows_shell() {
    case "${OSTYPE:-}" in
        msys*|mingw*|cygwin*|win32*) return 0 ;;
        *) return 1 ;;
    esac
}

echo "========================================"
echo "Take a Break App - Sistema de Folgas"
echo "========================================"
echo ""

# Criar directórios necessários
mkdir -p bin
mkdir -p data

# Compilar antes de executar
echo "Compilando aplicação..."
if is_windows_shell; then
    javac -cp "dist/lib/sqlite-jdbc-3.23.1.jar;dist/lib/jcalendar-1.4.jar" -d bin src/RegistoFolgas/*.java
else
    javac -cp "dist/lib/sqlite-jdbc-3.23.1.jar:dist/lib/jcalendar-1.4.jar" -d bin src/RegistoFolgas/*.java
fi

if [ $? -ne 0 ]; then
    echo ""
    echo "ERRO NA COMPILACAO. Aplicacao nao foi iniciada."
    exit 1
fi

# Copiar base de dados se não existir
if [ ! -f data/AgendamentoFolgas.db ]; then
    echo "Copiando base de dados..."
    cp dist/data/AgendamentoFolgas.db data/
fi

# Executar aplicação
echo "Iniciando aplicação..."
# Usar separador correto para Windows (;) vs Unix (:)
if is_windows_shell; then
    java --enable-native-access=ALL-UNNAMED -cp "bin;dist/lib/sqlite-jdbc-3.23.1.jar;dist/lib/jcalendar-1.4.jar" RegistoFolgas.Login
else
    java --enable-native-access=ALL-UNNAMED -cp "bin:dist/lib/sqlite-jdbc-3.23.1.jar:dist/lib/jcalendar-1.4.jar" RegistoFolgas.Login
fi
