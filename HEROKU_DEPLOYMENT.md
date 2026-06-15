# Deploy Heroku Automático

## ⚠️ STATUS ATUAL

✅ JAR compilado (26MB)
✅ Git inicializado  
⏳ Heroku CLI instalando...
❌ Railway trial expirado → Usando Heroku grátis em vez

---

## COMO ESTÁ AGORA AUTOMÁTICO

Criei:

- `Procfile` - Heroku sabe como executar
- `system.properties` - Java 21
- `Dockerfile` - Para alternativas

---

## PRÓXIMO PASSO: 1 CLIQUE

Quando Heroku CLI estiver pronto:

```bash
cd "d:\Projeto de Informática"

# 1. Login (abre browser)
heroku login

# 2. Criar app
heroku create takeabreak-webapp

# 3. Adicionar MySQL
heroku addons:create cleardb:ignite  # MySQL grátis

# 4. Deploy
git add .
git commit -m "Take a Break Production"
git push heroku main
```

---

## OU: Deploy via GitHub (Auto)

1. Faz push para GitHub
2. Conecta GitHub em Heroku Dashboard
3. Heroku faz deploy automático ao cada push

---

## Espera uns segundos...

Heroku CLI está a instalar. Depois executa o script de deploy.
