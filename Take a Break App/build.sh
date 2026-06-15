#!/bin/bash
# Script para compilar o Take a Break App
# Autor: Recuperação de Bytecode

is_windows_shell() {
    case "${OSTYPE:-}" in
        msys*|mingw*|cygwin*|win32*) return 0 ;;
        *) return 1 ;;
    esac
}

echo "========================================"
echo "Compilando Take a Break App..."
echo "========================================"
echo ""

# Criar directórios necessários
mkdir -p bin
mkdir -p data

# Compilar todos os ficheiros Java
echo "Compilando ficheiros Java..."
if is_windows_shell; then
    javac -cp "dist/lib/sqlite-jdbc-3.23.1.jar;dist/lib/jcalendar-1.4.jar" -d bin src/RegistoFolgas/*.java
else
    javac -cp "dist/lib/sqlite-jdbc-3.23.1.jar:dist/lib/jcalendar-1.4.jar" -d bin src/RegistoFolgas/*.java
fi

# Verificar se compilou com sucesso
if [ $? -eq 0 ]; then
    echo ""
    echo "========================================"
    echo "COMPILACAO CONCLUIDA COM SUCESSO"
    echo "========================================"
    echo ""
    echo "Para executar a aplicação, use: ./run.sh"
    echo "Ou execute: java --enable-native-access=ALL-UNNAMED -cp \"bin:dist/lib/sqlite-jdbc-3.23.1.jar:dist/lib/jcalendar-1.4.jar\" RegistoFolgas.Login"
else
    echo ""
    echo "ERRO NA COMPILACAO!"
    echo "Verifique os erros acima."
    exit 1
fi
