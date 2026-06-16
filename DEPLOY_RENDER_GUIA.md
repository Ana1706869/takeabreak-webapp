# Guia Deploy Render.com - Take a Break

## 🚀 PASSO-A-PASSO COMPLETO

### **PASSO 1: Criar Conta no Render (1 min)**

1. Já está em https://dashboard.render.com/register
2. Escolhe uma opção:
   - **GitHub** (rápido - recomendado)
   - **Email + Password** (manual)
   - **Google**

**Se escolher GitHub:**

- Clique "GitHub"
- Autoriza acesso
- Pronto!

**Se escolher Email:**

- Preenche: `seu_email@gmail.com`
- Preenche: password segura
- Clique "Create Account"
- Confirma email (check inbox)

---

### **PASSO 2: Criar Web Service (2 min)**

3. Após login, está no Dashboard
4. Clique no botão **"+ New"** (canto superior direito)
5. Seleciona **"Web Service"**

---

### **PASSO 3: Conectar Repositório Git (2 min)**

6. Escolhe uma opção:

**OPÇÃO A: GitHub (Recomendado)**

- Clique em "GitHub"
- Autoriza app Render no GitHub
- Seleciona o repositório: `takeabreak`
- Branch: `main` ou `master`
- Clique "Next" → "Create Service"

**OPÇÃO B: Upload Direto (sem GitHub)**

- Clique em "Public Repository"
- Deixa em branco (vamos fazer upload)
- Clique "Continue"

---

### **PASSO 4: Configuração do Serviço (3 min)**

7. Preenche formulário:

| Campo           | Valor                      |
| --------------- | -------------------------- |
| **Name**        | `takeabreak-webapp`        |
| **Environment** | `Docker`                   |
| **Region**      | `Frankfurt` (ou `US East`) |
| **Branch**      | `main`                     |

8. Em **"Build Command"** deixa em branco (Dockerfile define)

9. **Environment Variables:**

```
DB_URL=jdbc:mysql://localhost:3306/takeabreak?useSSL=false&serverTimezone=UTC
DB_USER=anokas
DB_PASS=Informatica_1706869
PORT=10000
```

10. Plano: **"Free"** (0.00 USD)

11. Clique **"Create Service"**

---

### **PASSO 5: Esperar Deploy (5-10 min)**

12. Render vai:
    - ✓ Fazer build da imagem Docker
    - ✓ Compilar código Maven
    - ✓ Criar certificado SSL automático
    - ✓ Deploy automático

**Acompanhar:**

- Ver "Deploys" tab
- Status muda: `Building` → `Live`

**Quando estiver "Live":**

- Copia a URL pública (ex: `takeabreak-webapp-xxxxx.onrender.com`)

---

### **PASSO 6: Configurar DNS CNAME (2 min)**

13. Abrir: https://host-redirect.com/login
    - Email: seu email
    - Password: sua senha
    - Domain: takeabreak.pt

14. Ir para **"DNS Settings"** ou **"Zone Editor"**

15. Procura por registo **"@"** (root domain)

16. **Criar novo registo CNAME:**

| Campo | Valor                                  |
| ----- | -------------------------------------- |
| Name  | `@` (ou deixar em branco)              |
| Type  | `CNAME`                                |
| Value | `takeabreak-webapp-xxxxx.onrender.com` |
| TTL   | `300`                                  |

17. **Clique "Save"** (aguarda 2-5 minutos para propagar)

---

### **PASSO 7: Ligar Custom Domain no Render (1 min)**

18. Voltar ao Render Dashboard
19. Seleciona o serviço "takeabreak-webapp"
20. Clique em **"Settings"**
21. Scroll até **"Custom Domains"**
22. Clique **"+ Add Custom Domain"**
23. Preenche: `takeabreak.pt`
24. Clique **"Add"**

Render vai:

- Verificar DNS CNAME
- Gerar certificado SSL Let's Encrypt automático
- Ativar HTTPS

---

### **PASSO 8: Validar em Produção ✅ (1 min)**

25. Abrir browser:
    - https://takeabreak.pt/

**Esperado:**

- ✅ Sem erro DNS
- ✅ Sem erro de certificado
- ✅ Página de login carrega
- ✅ Lock verde (HTTPS válido)

26. **Testar login:**
    - Email: `anasilva_pinhel@hotmail.com`
    - Password: `informatica`

---

## 📊 Tempos Estimados

| Etapa                | Tempo           |
| -------------------- | --------------- |
| Criar Render account | 1 min           |
| Setup Web Service    | 3 min           |
| Deploy automático    | 5-10 min        |
| Configurar DNS       | 2 min           |
| Custom Domain Render | 1 min           |
| DNS propagar         | 2-5 min         |
| **TOTAL**            | **~20 minutos** |

---

## 🐛 Troubleshooting

**Problema: "Still building..."**

- ✓ Normal! Aguarde 5-10 minutos
- Ver logs em "Logs" tab do Render

**Problema: "Certificate Error"**

- Espere 5 minutos após adicionar custom domain
- Render gera certificado automaticamente

**Problema: "DNS still not working"**

- Verifica registos DNS: `nslookup takeabreak.pt`
- TTL 300 propaga em 5 minutos
- Se ainda falhar, recarrega DNS: `ipconfig /flushdns` (Windows)

**Problema: "App crashed"**

- Ver erro em "Logs" do Render
- Verificar variáveis de ambiente (DB_URL, DB_USER, DB_PASS)

---

## ✅ O que fica pronto

Após **20 minutos**:

- ✓ App em produção HTTPS
- ✓ Domínio takeabreak.pt funciona
- ✓ SSL certificado válido (Let's Encrypt)
- ✓ Acessível de qualquer lugar do mundo
- ✓ MySQL database funciona
- ✓ Backups automáticos Render
- ✓ Escala automática (free tier)

---

## 💾 URLs Importantes

| Serviço           | Link                         |
| ----------------- | ---------------------------- |
| Render Dashboard  | https://dashboard.render.com |
| Take a Break App  | https://takeabreak.pt        |
| Host-Redirect DNS | https://host-redirect.com    |
| Gmail login       | https://mail.google.com      |

---

**Pronto? Começa em:** https://dashboard.render.com/register
