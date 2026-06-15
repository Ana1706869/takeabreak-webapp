# ⚠️ SETUP DO SSH REQUERIDO NO SERVIDOR

Para colocar a webapp em produção, precisas de executar um script no servidor Windows **192.168.68.103** primeiro.

## Passo 1: Transferir o Ficheiro PowerShell

Transfere este ficheiro para o **Desktop** do servidor 192.168.68.103:

**Origem (teu PC):**

```
d:\Projeto de Informática\Take a Break App\deploy\setup-ssh-windows-standalone.ps1
```

**Destino (servidor Windows):**

```
C:\Users\anasilva\Desktop\setup-ssh-windows-standalone.ps1
```

**Opções de transferência:**

- Email
- USB/Pen drive
- Dropbox/Google Drive
- Partilha de rede
- Qualquer método que funcione para ti

---

## Passo 2: Executar o Script no Servidor

1. **No servidor 192.168.68.103**, abre **PowerShell como Administrador** (direito do rato → "Run as Administrator")

2. **Copia e COLA isto tudo de uma vez:**

```powershell
Set-ExecutionPolicy -ExecutionPolicy Bypass -Scope Process -Force; & "C:\Users\anasilva\Desktop\setup-ssh-windows-standalone.ps1"
```

3. **Espera que termine** - deve dizer:

   ```
   Setup Complete!
   ```

4. **Valida o resultado** - deve mostrar:
   - `sshd` em **Running**
   - Porta **22** em **LISTENING**

---

## Passo 3: Deploy Automático (depois do SSH estar pronto)

Quando o Passo 2 terminar com sucesso, volta ao teu PC e executa isto na terminal:

```bash
cd '/d/Projeto de Informática/Take a Break App/webapp'
chmod +x deploy/publish-remote.sh deploy/server-bootstrap.sh
./deploy/publish-remote.sh \
  --host 192.168.68.103 \
  --user anasilva \
  --port 22 \
  --domain 192.168.68.103 \
  --db-name take_a_break_web \
  --db-user takeabreak \
  --db-pass 'Informatica_1706869'
```

Este script vai automaticamente:

- ✅ Fazer build da webapp
- ✅ Copiar para o servidor via SSH
- ✅ Instalar MySQL, Nginx, systemd
- ✅ Iniciar tudo
- ✅ Webapp acessível em `http://192.168.68.103`

---

## Troubleshooting

### Erro: "PowerShell policy denied"

Se vires algo como "cannot be loaded because running scripts is disabled", executa:

```powershell
Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser -Force
```

### Erro: "SSH still not working after setup"

Reinicia o servidor e tenta de novo.

### Erro: "Port 22 not listening"

Espera 30 segundos e corre o comando de novo.

---

## Status

**Webapp Local (sem deploy):** http://localhost:8081

- Email: `anasilva_pinhel@hotmail.com`
- Password: `informatica`

**Webapp Produção (após deploy):** http://192.168.68.103 (em breve!)
