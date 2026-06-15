# INSTRUÇÕES - Colocar Webapp em Produção (https://takeabreak.pt)

## PASSO 1: COPIAR FICHEIROS PARA O SERVIDOR (2 MIN)

Copia estes 2 ficheiros do teu PC para o **servidor 192.168.68.103**:

**Origem (teu PC):**

- `d:\Projeto de Informática\setup-nginx.bat`
- `d:\Projeto de Informática\nginx.conf`

**Destino (servidor):**

- `C:\Users\anasilva\Desktop\` (ou qualquer local acessível)

**Como copiar:**

- Via USB / OneDrive / Google Drive / Partilha de rede
- Ou manualmente se tiveres acesso local

---

## PASSO 2: EXECUTAR O SCRIPT NO SERVIDOR (2 MIN)

No **servidor 192.168.68.103**:

1. **Clica com direito do rato** em `setup-nginx.bat`
2. Escolhe **"Run as administrator"** (muito importante!)
3. Espera até dizer `[SUCESSO] NGINX CONFIGURADO E ATIVO!`
4. Carrega em qualquer tecla para fechar

**O script vai:**

- Instalar Nginx
- Gerar certificado TLS auto-assinado
- Criar reverse proxy 80/443 → 8081
- Ativar o serviço

**Output esperado:**

```
[OK] Chocolatey pronto
[OK] Nginx instalado
[OK] Diretório criado
[OK] Certificado criado
[OK] nginx.conf copiado
[OK] Configuração válida
[OK] Nginx iniciado
[OK] Porta 80 LISTENING
[OK] Porta 443 LISTENING
[SUCESSO] NGINX CONFIGURADO E ATIVO!
```

---

## PASSO 3: CRIAR REGISTO DNS (2 MIN)

1. Abre em qualquer browser: **https://www.host-redirect.com/login**
2. Faz login com credenciais de takeabreak.pt
3. Vai a **DNS Records** ou **Zone File** de takeabreak.pt
4. **CRIA UM NOVO REGISTO:**
   - **Tipo:** A
   - **Nome:** @ (ou deixa em branco)
   - **Valor:** 188.250.157.96
   - **TTL:** 300
5. **Clica em Guardar/Save**

---

## PASSO 4: CONFIGURAR PORT FORWARD NO ROUTER (3 MIN)

1. Abre em qualquer browser: **http://192.168.1.1** (ou o IP do teu router)
2. Faz login com credenciais do router
3. Procura por **"Port Forwarding"** ou **"Virtual Server"**
4. **CRIA 2 REGRAS:**

   **Regra 1 (HTTP):**
   - Porta Externa: 80
   - Porta Interna: 80
   - IP Interno: 192.168.68.103
   - Protocolo: TCP
   - Guarda

   **Regra 2 (HTTPS):**
   - Porta Externa: 443
   - Porta Interna: 443
   - IP Interno: 192.168.68.103
   - Protocolo: TCP
   - Guarda

5. **Reinicia o router** (opcional, mas recomendado)

---

## PASSO 5: VALIDAR (1 MIN)

Abre uma terminal/PowerShell e executa:

```bash
# Validar DNS
nslookup takeabreak.pt

# Testar HTTP -> HTTPS redirect
curl -I http://takeabreak.pt

# Testar HTTPS (vai avisar sobre certificado, é normal)
curl -k -I https://takeabreak.pt
```

**Output esperado:**

```
> nslookup takeabreak.pt
Name:    takeabreak.pt
Address: 188.250.157.96

> curl -I https://takeabreak.pt
HTTP/1.1 302 Moved Temporarily
Location: https://takeabreak.pt/login
```

---

## ACEDER À WEBAPP

Quando tudo estiver pronto:

**URL:** https://takeabreak.pt

**Credenciais:**

- Email: anasilva_pinhel@hotmail.com
- Senha: informatica

---

## TROUBLESHOOTING

**Erro: "Nginx não inicia"**

- Abre a PowerShell como Admin e executa:
  ```powershell
  Get-EventLog Application -Newest 20 | Where-Object {$_.Source -like "*nginx*"} | Format-List
  ```

**Erro: "Porta 80/443 já em uso"**

- Verifica se algum outro serviço está a usar:
  ```powershell
  netstat -ano | findstr ":80 "
  netstat -ano | findstr ":443 "
  ```

**Erro: "DNS não resolve"**

- Espera 5 minutos pela propagação DNS
- Testa com: `nslookup takeabreak.pt 1.1.1.1`

**Certificado auto-assinado aviso no browser**

- Isto é NORMAL com certificados self-signed
- Clica em "Avançado" → "Continua mesmo assim"
- Para remover o aviso: instala Let's Encrypt depois

---

## PRÓXIMOS PASSOS (OPCIONAL)

Depois de tudo estar a funcionar, podes:

1. **Instalar Let's Encrypt** para certificado válido (sem avisos)
2. **Configurar backup automático** do MySQL
3. **Monitorizr uptime** com ferramentas de health check

---

**Tempo total: ~9 minutos**

Quando terminares os 5 passos, responde e eu faço a validação final!
