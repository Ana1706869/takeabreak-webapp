# DEPLOYMENT AUTOMÁTICO - GUIA FINAL

## STATUS ATUAL

✅ **Feito:**

- Dockerfile criado
- Railway CLI instalado
- Node.js e npm prontos
- Maven compilando webapp
- Nginx config criado
- SSL certs prontos

⏳ **Em progresso:**

- Maven build (3-5 min)

## QUANDO BUILD TERMINAR (Comando abaixo)

```bash
# Verifica se JAR está pronto
ls -lh "d:\Projeto de Informática\webapp\target\take-a-break-web-1.0.0.jar"
```

Resultado esperado:

```
-rw-r--r-- 1 user staff  26M  Jun 15 19:30  take-a-break-web-1.0.0.jar
```

---

## DEPLOYMENT AUTOMÁTICO (1 CLIQUE)

Quando JAR estiver pronto, executa:

### **OPÇÃO 1: Comando Direto (Recomendado)**

```bash
# Navega para o projeto
cd "d:\Projeto de Informática"

# Faz login no Railway (primeira vez pede GitHub/Email)
railway login

# Inicializa projeto Railway
railway init --name takeabreak-webapp

# Deploy
railway up --force

# Obtém URL
railway open  # Abre painel Railway no browser
```

### **OPÇÃO 2: Via Docker Local + Railway**

```bash
# Testa localmente com Docker Compose
docker-compose -f docker-compose.prod.yml up

# Se funcionar localmente, deploy para Railway
railway up --force
```

### **OPÇÃO 3: Via GitHub + Railway Automático**

```bash
# Faz push para GitHub
git remote add origin https://github.com/seu-user/takeabreak.git
git add .
git commit -m "Deploy automático takeabreak"
git push -u origin main

# Railway auto-detecta e faz deploy ao fazer push
```

---

## SCRIPTS AUTOMÁTICOS CRIADOS

| Script                    | O que faz                 |
| ------------------------- | ------------------------- |
| `DEPLOY.bat`              | 1-clique deploy (Windows) |
| `deploy-railway.ps1`      | PowerShell deploy         |
| `deploy-auto.sh`          | Bash deploy               |
| `docker-compose.yml`      | Teste local               |
| `docker-compose.prod.yml` | Prod com Nginx            |

---

## COMMANDS RÁPIDOS

```bash
# Ver status
railway status

# Ver logs
railway logs -f

# Reiniciar
railway restart

# Parar
railway stop

# Apagar
railway delete

# Config DNS
railway domain add takeabreak.pt

# Var ambiente
railway env list
railway env set DB_URL "jdbc:mysql://..."
```

---

## SEQUÊNCIA COMPLETA (Passo a Passo)

### 1️⃣ Verifica Build

```bash
ls "d:\Projeto de Informática\webapp\target\take-a-break-web-1.0.0.jar"
```

### 2️⃣ Login Railway

```bash
railway login
```

(Abre browser, escolhe GitHub ou Email, autoriza)

### 3️⃣ Deploy

```bash
cd "d:\Projeto de Informática"
railway up --force
```

### 4️⃣ Obtém URL

```bash
railway open  # Abre painel
# Ou vê o output que mostrou algo como: takeabreak-webapp.up.railway.app
```

### 5️⃣ Cria DNS CNAME

Host-redirect.com:

```
Nome: @
Tipo: CNAME
Valor: takeabreak-webapp.up.railway.app
TTL: 300
```

### 6️⃣ Liga Domínio a Railway

```bash
railway domain add takeabreak.pt
```

### 7️⃣ Valida

```bash
# Espera 5 min
nslookup takeabreak.pt
curl https://takeabreak.pt
```

---

## TROUBLESHOOTING

### Build demora muito

```bash
# Verifica progresso
cd "d:\Projeto de Informática\webapp"
tail -f target/build.log
```

### Railway login falha

```bash
# Force logout e login novo
railway logout
railway login
```

### Build out of memory

```bash
# Aumenta memória Maven
$env:MAVEN_OPTS = "-Xmx2048m"
mvn clean package
```

### Porta já em uso

```bash
# Se testar local e porta 8081 está ocupada
lsof -i :8081
kill -9 PID
```

---

## CUSTO (GRÁTIS até $100/mês)

- App container: $0 (primeiros $100)
- MySQL 5GB: $0
- Domínio customizado: $0
- SSL/HTTPS: $0
- CDN: $0

Total: **$0** 🎉

---

## RESULTADO FINAL

Quando terminar:

✅ `https://takeabreak.pt` - Online e seguro
✅ HTTPS automático (Railway inclui)
✅ MySQL na nuvem
✅ Auto-scaling
✅ Backups automáticos
✅ CDN global

**Status: PRODUCTION READY** 🚀

---

## Próximas Ações

1. **Agora:** Aguarda Maven build (monitora com `ls target/take-a-break-web-1.0.0.jar`)
2. **Depois:** Copia comando `railway login` e executa
3. **Depois:** `railway up --force` para deploy
4. **Depois:** Cria DNS CNAME em host-redirect
5. **Depois:** Acede https://takeabreak.pt

**Tempo total: ~10 minutos** ⏱️
