# DEPLOY EM CLOUD - RAILWAY.APP

## Opção 1: Automático (Recomendado)

### Passo 1: Instalar Railway CLI

```bash
npm install -g @railway/cli
```

### Passo 2: Executar deploy

```bash
cd "d:\Projeto de Informática"
bash deploy-railway.sh
```

Este script vai:

- ✅ Compilar a webapp
- ✅ Criar projeto no Railway
- ✅ Fazer deploy automático
- ✅ Gerar URL pública

---

## Opção 2: Manual via Portal

1. Abre https://railway.app
2. Clica em "New Project"
3. Escolhe "Deploy from GitHub"
4. Conecta teu repo (ou usa `git init` + `git push`)
5. Seleciona branch e confirma
6. Railway faz deploy automático

---

## Passo 3: Configurar Domínio

Depois do deploy, Railway te dá um URL como `takeabreak-webapp.up.railway.app`

Para usar `takeabreak.pt`:

### No painel da Railway:

1. Vai a **Settings** → **Domains**
2. Adiciona domínio: `takeabreak.pt`
3. Railway gera um CNAME

### No host-redirect.com DNS:

1. Cria registo CNAME:
   - **Nome**: @ (ou takeabreak)
   - **Tipo**: CNAME
   - **Valor**: `<id>.railway.app`
   - **TTL**: 300

2. Espera 5 minutos pela propagação

---

## Passo 4: Validar

```bash
nslookup takeabreak.pt
curl -I https://takeabreak.pt
```

---

## Base de Dados

A Railway inclui MySQL grátis (5GB).

Variáveis de ambiente já configuradas:

```
DB_URL: jdbc:mysql://mysql:3306/take_a_break_web...
DB_USER: takeabreak
DB_PASS: Informatica_1706869
```

---

## Custo

- App container: **GRÁTIS** (primeiros 100$)
- MySQL: **GRÁTIS** (até 5GB)
- Domínio customizado: **GRÁTIS**
- SSL/HTTPS: **GRÁTIS**

---

## Troubleshooting

### "Erro: Railway CLI não reconhecido"

```bash
# Instala via npm
npm install -g @railway/cli
```

### "Build falha"

```bash
# Verifica logs
railway logs
```

### "Domínio não funciona"

```bash
# Espera propagação DNS (até 10 min)
nslookup takeabreak.pt
```

---

## Depois Funcionar

Quando `https://takeabreak.pt` estiver online:

**Credenciais:**

- Email: anasilva_pinhel@hotmail.com
- Password: informatica

---

## Para Para/Reiniciar

```bash
railway restart
```

## Para Remover

```bash
railway delete
```
