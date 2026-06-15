# Deploy para Render.com (Grátis e Indefinido)

## POR QUE RENDER EM VEZ DE RAILWAY?

| Recurso        | Railway         | Render            |
| -------------- | --------------- | ----------------- |
| Tier Grátis    | Trial (expirou) | **Indefinido** ✅ |
| App Container  | $5+/mês         | **Grátis** ✅     |
| PostgreSQL     | $0-15           | **Grátis** ✅     |
| MySQL          | Não grátis      | **Grátis** ✅     |
| Domínio Custom | Grátis          | **Grátis** ✅     |
| SSL/HTTPS      | Grátis          | **Grátis** ✅     |
| Suporte        | Pago            | Comun (Grátis)    |

**Render é 100% grátis com limites generosos.**

---

## DEPLOYMENT RENDER (3 PASSOS)

### 1. Conectar GitHub

```bash
cd "d:\Projeto de Informática"

# Se não tiver Git
git init
git add .
git commit -m "takeabreak webapp production"

# Fazer push para GitHub
git remote add origin https://github.com/seu-user/takeabreak.git
git branch -M main
git push -u origin main
```

### 2. Deploy via Render Dashboard

1. Abre: **https://render.com**
2. Clica em **"New +"** → **"Web Service"**
3. Conecta GitHub (autoriza)
4. Escolhe repo `takeabreak`
5. Preenche:
   ```
   Name: takeabreak-webapp
   Environment: Docker
   Branch: main
   Build Command: (deixa em branco - usa Dockerfile)
   Start Command: (deixa em branco)
   ```
6. Variáveis de ambiente:
   ```
   RAILWAY_RUNTIME_VERSION=java21
   DB_URL: jdbc:mysql://mysql:3306/take_a_break_web...
   DB_USER: takeabreak
   DB_PASS: Informatica_1706869
   ```
7. Clica em **"Create Web Service"**

### 3. Ligar Base de Dados

1. No painel Render, clica em **"New +"** → **"MySQL"**
2. Preenche credenciais
3. Copia URL de conexão para web service

---

## OU: Deploy Local com Docker

Se quiseres testar local antes:

```bash
cd "d:\Projeto de Informática"

# Build Docker
docker build -f webapp/Dockerfile -t takeabreak:latest .

# MySQL em container
docker run -d --name mysql-takeabreak \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=take_a_break_web \
  -e MYSQL_USER=takeabreak \
  -e MYSQL_PASSWORD=Informatica_1706869 \
  mysql:8.0

# App em container
docker run -d --name takeabreak-app \
  -p 8081:8081 \
  -e DB_URL="jdbc:mysql://mysql-takeabreak:3306/take_a_break_web..." \
  -e DB_USER=takeabreak \
  -e DB_PASS=Informatica_1706869 \
  takeabreak:latest

# Aceder
open http://localhost:8081
```

---

## Alternativa 2: Heroku + JawsDB (Grátis)

1. Cria conta: https://www.heroku.com
2. Instala Heroku CLI
3. Deploy:
   ```bash
   heroku login
   heroku create takeabreak-webapp
   heroku addons:create jawsdb:mysql
   git push heroku main
   ```

---

## Alternativa 3: Vercel + Prisma (Para Node)

Vercel é grátis mas requer Node/Next.js.

---

## OPÇÃO RECOMENDADA: GitHub Pages + Backend Cloud

Usa GitHub Pages para frontend + cloud backend:

1. **Frontend:** GitHub Pages (grátis, CNAME)
2. **Backend:** Heroku, Render ou Firebase

---

## PRÓXIMOS PASSOS IMEDIATOS

### **OPÇÃO A: Render (Recomendado - 10 min)**

```bash
# 1. GitHub Push
cd "d:\Projeto de Informática"
git init
git add .
git commit -m "takeabreak v1.0"
git remote add origin https://github.com/seu-user/takeabreak.git
git push -u origin main

# 2. Render Dashboard: https://render.com
# 3. Novo Web Service → GitHub → takeabreak
```

### **OPÇÃO B: Docker Local (Teste)**

```bash
docker-compose -f docker-compose.prod.yml up
```

### **OPÇÃO C: Heroku (Também Grátis)**

```bash
heroku login
cd webapp
git init
git add .
git commit -m "initial"
heroku create takeabreak
git push heroku main
```

---

## SUMÁRIO

✅ **JAR compilado**: 26MB
✅ **Docker pronto**: Dockerfile criado
✅ **Autenticação Railway**: OK (mas trial expirou)
🔄 **Próximo**: Escolher plataforma alternativa

**Tempo: ~5-10 minutos até estar online**

Qual é a tua preferência?
