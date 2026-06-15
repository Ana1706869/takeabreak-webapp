# Take a Break App

Este repositório contém:

- Aplicação Desktop (Swing + SQLite) em `src/RegistoFolgas`
- Aplicação Web em `webapp/`

## Aplicação Desktop: Instalação e Execução

## 1) Pré-requisitos

- Java JDK 17 ou superior (recomendado: JDK 24)
- Windows CMD, PowerShell ou Git Bash

Dependências já incluídas no projeto:

- `dist/lib/sqlite-jdbc-3.23.1.jar`
- `dist/lib/jcalendar-1.4.jar`
- Base de dados base: `dist/data/AgendamentoFolgas.db`

## 2) Compilar a Desktop

Na raiz do projeto:

### Windows (CMD)

```bat
build.bat
```

### Git Bash

```bash
./build.sh
```

## 3) Executar a Desktop

Na raiz do projeto:

### Windows (CMD)

```bat
run.bat
```

### Git Bash

```bash
./run.sh
```

Estes scripts:

- compilam os `.java` para `bin/`
- criam `data/` se necessário
- copiam `AgendamentoFolgas.db` para `data/` na primeira execução
- arrancam a aplicação em `RegistoFolgas.Login`

## 4) Execução manual (opcional)

```bash
javac -cp "dist/lib/sqlite-jdbc-3.23.1.jar;dist/lib/jcalendar-1.4.jar" -d bin src/RegistoFolgas/*.java
java --enable-native-access=ALL-UNNAMED -cp "bin;dist/lib/sqlite-jdbc-3.23.1.jar;dist/lib/jcalendar-1.4.jar" RegistoFolgas.Login
```

## 6) Verificação rápida

Compilação Desktop testada com sucesso com:

```bash
javac -cp "dist/lib/sqlite-jdbc-3.23.1.jar;dist/lib/jcalendar-1.4.jar" -d bin src/RegistoFolgas/*.java
```

Sem erros de compilação (apenas notas de API deprecated/unchecked).

## 7) Aplicação Web

Para a documentação completa da webapp, consulte:

- `webapp/README.md`

## 8) Produção (Web + SSL)

Esta secção resume o processo completo utilizado para colocar a aplicação web em produção, com acesso HTTPS.

### 8.1 Pré-requisitos no servidor

- Java JDK 24 instalado
- Maven disponível para build
- Nginx instalado em `C:\nginx`
- Webapp (JAR) em `C:\takeabreak`
- Aplicação Spring Boot a escutar em `127.0.0.1:8081`

### 8.2 Build da webapp

Na pasta `webapp/`:

```bash
mvn -DskipTests clean package spring-boot:repackage
```

Artefacto gerado:

- `webapp/target/take-a-break-web-1.0.0.jar`

### 8.3 Publicação do artefacto

Copiar o JAR para a pasta de execução em produção:

```bat
copy webapp\target\take-a-break-web-1.0.0.jar C:\takeabreak\take-a-break-web-1.0.0.jar
```

### 8.4 Reiniciar a aplicação Java

Exemplo com PowerShell:

```powershell
cd C:\takeabreak
.\start.ps1
```

### 8.5 Configuração HTTPS no Nginx

O `nginx.conf` deve ter:

- servidor HTTP na porta `80` com redirecionamento para HTTPS
- servidor HTTPS na porta `443` com:
  - `ssl_certificate C:/nginx/conf/ssl/takeabreak.crt;`
  - `ssl_certificate_key C:/nginx/conf/ssl/takeabreak.key;`
- `proxy_pass` para `http://127.0.0.1:8081`

### 8.6 Gerar certificado SSL (autoassinado)

Com OpenSSL:

```powershell
& 'C:\Program Files\Git\usr\bin\openssl.exe' req -x509 -nodes -newkey rsa:2048 `
	-keyout 'C:\nginx\conf\ssl\takeabreak.key' `
	-out 'C:\nginx\conf\ssl\takeabreak.crt' `
	-days 365 -subj '/CN=takeabreak.pt'
```

### 8.7 Reiniciar Nginx

```powershell
cd C:\nginx
.\nginx.exe -c C:\nginx\conf\nginx.conf
```

Se já existir processo antigo do Nginx, parar primeiro e arrancar novamente.

### 8.8 Validação final

Testes rápidos:

```bash
curl -I http://takeabreak.pt/
curl -k -I https://takeabreak.pt/
```

Resultados esperados:

- HTTP acessível e/ou redirecionado para HTTPS
- HTTPS com resposta `200 OK`

### 8.9 Site final em produção

- URL principal: `https://takeabreak.pt/`
- Login e dashboard disponíveis via HTTPS
- Frase publicada no ecrã de login: `Aplicação para gestâo de ausências`

Nota sobre certificado:

- O certificado atual é autoassinado. Alguns browsers podem mostrar aviso de confiança.
- Para remover avisos no browser, usar um certificado emitido por uma CA pública (ex.: Let's Encrypt).
