# Configuração de JAVA_HOME - RESOLVIDO ✓

**Status**: ✅ **JÁ CONFIGURADO** - A configuração foi adicionada ao seu `~/.bashrc`

## Configuração Atual

O seu `~/.bashrc` agora contém:

```bash
export PATH="/c/Program Files/Java/jdk-24/bin:$PATH"
export JAVA_HOME="C:/Program Files/Java/jdk-24"
```

## Como Usar

### Opção 1: Bash/Git Bash (AUTOMÁTICO)

Após abrir um **novo terminal**, tudo funcionará automaticamente:

```bash
cd webapp
mvn clean package
```

### Opção 2: Usar Scripts Fornecidos

No diretório raiz do projeto:

**Linux/Mac/Git Bash:**

```bash
./build-webapp.sh
```

**Windows CMD:**

```batch
build-webapp.bat
```

Estes scripts garantem que `JAVA_HOME` está configurado corretamente, independentemente da configuração do sistema.

## Verificação

Após abrir um novo terminal, teste:

```bash
java -version
javac -version
mvn --version
```

Todos devem mostrar versões sem erros e referir Java 24.

## Troubleshooting

Se ainda vir o erro após abrir um novo terminal:

1. **Verifique o ~/.bashrc:**

   ```bash
   cat ~/.bashrc | grep JAVA_HOME
   ```

   Deve mostrar: `export JAVA_HOME="C:/Program Files/Java/jdk-24"`

2. **Force a configuração manualmente:**

   ```bash
   export JAVA_HOME="C:/Program Files/Java/jdk-24"
   export PATH="$JAVA_HOME/bin:$PATH"
   mvn clean
   ```

3. **Use os scripts de build:**
   ```bash
   ./build-webapp.sh   # Linux/Mac/Bash
   build-webapp.bat    # Windows CMD
   ```
