# 🚀 DEPLOY TAKEABREAK.PT - TUDO AUTOMÁTICO

## 📋 RESUMO: 3 PASSOS, ~10 MINUTOS

| Passo | Ação                   | Tempo |
| ----- | ---------------------- | ----- |
| 1️⃣    | Executar script deploy | 5 min |
| 2️⃣    | Criar DNS A record     | 2 min |
| 3️⃣    | Validar online         | 1 min |

---

## ✅ TUDO JÁ PRONTO

Criei automaticamente:

- ✅ **Dockerfile** - Empacotamento da app
- ✅ **docker-compose.yml** - Para correr localmente
- ✅ **.dockerignore** - Otimização de build
- ✅ **deploy-railway.ps1** - Script deploy Windows
- ✅ **deploy-railway.sh** - Script deploy Linux/Mac
- ✅ **railway.json** - Configuração Railway

---

## 🎯 EXECUTAR AGORA

### **Windows (PowerShell como Admin):**

```powershell
cd "d:\Projeto de Informática"
Set-ExecutionPolicy -ExecutionPolicy Bypass -Scope Process -Force
& ".\deploy-railway.ps1"
```

### **Linux/Mac (Terminal):**

```bash
cd "d/Projeto de Informática"
bash deploy-railway.sh
```

---

## O QUE CADA SCRIPT FAZ

1. **Verifica dependências** (Node.js, Maven, Railway CLI)
2. **Compila webapp** com Maven
3. **Cria conta Railway** (se não tiver)
4. **Faz deploy automático**
5. **Gera URL pública** (ex: takeabreak-webapp.up.railway.app)

---

## 📍 PASSO 2: CRIAR DNS

Quando o deploy terminar com sucesso:

1. Abre: **https://www.host-redirect.com/login**
2. Faz login com credenciais takeabreak.pt
3. Vai a **DNS Records**
4. Cria registo CNAME:
   ```
   Nome: @
   Tipo: CNAME
   Valor: [ID].railway.app  (Railway te dá isto)
   TTL: 300
   ```
5. Guarda

---

## 🔗 PASSO 3: LIGAR DOMÍNIO À RAILWAY

Depois de criar DNS:

```bash
railway link takeabreak.pt
```

Ou manualmente no painel Railway: Settings → Domains → Add `takeabreak.pt`

---

## ✔️ VALIDAÇÃO FINAL

```bash
# Espera 5-10 min pela propagação DNS
nslookup takeabreak.pt

# Testa HTTP → HTTPS
curl -I https://takeabreak.pt

# Deve devolver 302 redirect ou 200 OK
```

---

## 🎨 QUANDO FICAR ONLINE

Acede a: **https://takeabreak.pt**

**Login:**

- Email: `anasilva_pinhel@hotmail.com`
- Senha: `informatica`

---

## 💰 CUSTO

- **Aplicação**: GRÁTIS (primeiros $100)
- **Banco de dados MySQL**: GRÁTIS (5GB)
- **Domínio customizado**: GRÁTIS
- **HTTPS/SSL**: GRÁTIS
- **Uptime 99.99%**: GRÁTIS

---

## 🛠️ COMANDOS ÚTEIS

```bash
# Ver logs
railway logs

# Reiniciar app
railway restart

# Parar app
railway stop

# Remover deploy
railway delete

# Atualizar (novo deploy)
git push  # Se estiver em GitHub
railway up
```

---

## ⚠️ TROUBLESHOOTING

### "Railway CLI não encontrado"

```bash
npm install -g @railway/cli
```

### "Erro de compilação Maven"

```bash
mvn clean install -DskipTests
```

### "Domínio ainda não funciona"

- Espera 10 minutos (propagação DNS)
- Testa: `nslookup takeabreak.pt`

### "Deploy falha"

```bash
railway logs  # Ver o erro
railway up --force  # Tentar de novo
```

---

## 📞 SUPORTE

Se algo falhar:

1. Verifica logs: `railway logs`
2. Testa compilação local: `mvn clean package`
3. Tenta deploy de novo: `railway up`

---

## 🎊 PRONTO!

Executa o script agora e a webapp fica online em produção com:

- ✅ HTTPS automático
- ✅ Base de dados cloud
- ✅ Uptime 99.99%
- ✅ Domínio personalizado

**Boa sorte! 🚀**
