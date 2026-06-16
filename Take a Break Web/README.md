# Take a Break Webapp

Aplicacao web do projeto Take a Break, desenvolvida com Spring Boot + Thymeleaf + MySQL.

## 1. Requisitos

- Java JDK 17+ (recomendado: JDK 24, conforme ambiente atual)
- Maven 3.9+
- MySQL 8.0+
- Git Bash, PowerShell ou CMD

## 2. Estrutura relevante

- `pom.xml`: dependencias e build
- `src/main/resources/application.properties`: configuracao de porta e base de dados
- `src/main/resources/schema.sql`: criacao/atualizacao de schema e dados iniciais

## 3. Configuracao da Base de Dados

A aplicacao usa por defeito:

- Base: `take_a_break_web`

Pode sobrescrever por variaveis de ambiente:

- `DB_URL`
- `DB_USER`
- `DB_PASS`
- `DATASOURCE_DRIVER`

Exemplo (Git Bash):

```bash
export DB_URL="jdbc:mysql://localhost:3306/take_a_break_web?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"

```

## 3.1 Configuracao de Producao (obrigatorio)

Para evitar perda de dados em producao:

- use uma base de dados persistente (nao temporaria)
- configure `SPRING_PROFILES_ACTIVE=prod`
- configure `DB_URL`, `DB_USER` e `DB_PASS` para a base de producao

Com o profile `prod`, a app usa `application-prod.properties` e desativa a inicializacao SQL automatica (`spring.sql.init.mode=never`).

Exemplo (Linux/Git Bash):

```bash
export SPRING_PROFILES_ACTIVE=prod
export DB_URL="jdbc:mysql://<host>:3306/take_a_break_web?useSSL=true&serverTimezone=UTC"
export DB_USER="<utilizador>"
export DB_PASS="<password>"
java -jar target/take-a-break-web-1.0.0.jar
```

Exemplo (PowerShell):

```powershell
$env:SPRING_PROFILES_ACTIVE="prod"
$env:DB_URL="jdbc:mysql://<host>:3306/take_a_break_web?useSSL=true&serverTimezone=UTC"
$env:DB_USER="<utilizador>"
$env:DB_PASS="<password>"
java -jar target/take-a-break-web-1.0.0.jar
```

## 4. Instalar dependencias e compilar

Na raiz do projeto:

```bash
cd webapp
mvn -DskipTests compile
```

## 5. Executar a aplicacao

### Opcao A: Executar em desenvolvimento

```bash
cd webapp
mvn -DskipTests package spring-boot:repackage
java -jar target/take-a-break-web-1.0.0.jar
```

### Opcao B: Usar task existente no VS Code

Task recomendada no workspace:

- `Package Webapp`

Essa task ja executa:

```text
mvn -DskipTests clean package spring-boot:repackage
```

Depois pode iniciar o jar em `webapp/target/`.

## 6. Aceder a aplicacao

- URL: `http://localhost:8081`
- Porta definida em `application.properties` por `server.port=8081`

## 7. Processo de inicializacao de dados

No arranque, a app executa scripts SQL de inicializacao (`spring.sql.init.mode=always`), incluindo:

- criacao de tabelas
- ajustes de colunas
- normalizacoes de dados
- indices (incluindo unicidade de telefone)

## 8. Seguranca de passwords

- Passwords novas (registo e alteracao de perfil) sao guardadas com BCrypt.
- Passwords antigas em texto simples sao migradas para hash BCrypt automaticamente no arranque da app.

## 9. Validacoes implementadas

No registo, existe validacao frontend e backend para:

- email (formato)
- codigo-postal (`XXXX-XXX`)
- telefone (9 digitos)
- nome/localidade/concelho/distrito (carateres adequados)
- unicidade de email
- unicidade de telefone
- confirmacao de password igual a password

## 10. Troubleshooting

### Erro: porta 8081 em uso

Mude a porta:

```properties
server.port=8082
```

### Erro no `mvn clean`: ficheiro jar bloqueado

Ocorre quando existe processo Java a usar o jar. Pare o processo e volte a executar:

```bash
# Git Bash
ps aux | grep java
kill -9 <PID>
```

### Erro de ligacao ao MySQL

Confirmar:

- MySQL ativo
- credenciais corretas
- base `take_a_break_web` acessivel
- variaveis `DB_URL/DB_USER/DB_PASS` coerentes

### Verificacao rapida de compilacao

```bash
cd webapp
mvn -DskipTests compile
```

## 11. Comandos uteis

```bash
# compilar
mvn -DskipTests compile

# package com repackage para jar executavel
mvn -DskipTests clean package spring-boot:repackage

# executar jar
java -jar target/take-a-break-web-1.0.0.jar
```
