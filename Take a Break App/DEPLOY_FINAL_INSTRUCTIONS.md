# Deploy Remoto - Instruções Finais para o Servidor 192.168.68.103

## Passo 1: Transferir o ficheiro PS1 (de qualquer forma)

Transfere este ficheiro para o Desktop do servidor:

- Origem (teu PC): `d:\Projeto de Informática\Take a Break App\deploy\setup-ssh-windows-standalone.ps1`
- Destino (servidor): `C:\Users\anasilva\Desktop\setup-ssh-windows-standalone.ps1`

Podes usar:

- Email
- USB/Pen drive
- Dropbox/Google Drive
- Qualquer partilha de ficheiros

## Passo 2: Executar o Script (no servidor)

1. **No servidor 192.168.68.103**, abre **PowerShell como Administrador**
2. Copia E COLA isto (tudo de uma vez):

```powershell
Set-ExecutionPolicy -ExecutionPolicy Bypass -Scope Process -Force; & "C:\Users\anasilva\Desktop\setup-ssh-windows-standalone.ps1"
```

3. Espera que termine (deve dizer "Setup Complete!")
4. Valida que aparece `sshd` em `Running` e porta `22` em `LISTENING`

## Passo 3: Deploy Final (do teu PC)

Quando o Passo 2 terminar, volta ao teu PC e executa:

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

Isto vai:

- Build da webapp automaticamente
- Copiar para o servidor via SSH
- Instalar MySQL, Nginx, systemd
- Iniciar tudo
- Webapp acessível em `http://192.168.68.103`

---

## Alternativa: Já está pronto Localmente!

Se não conseguires transferir para o servidor remoto, a webapp já está a 100% funcional em:

### http://localhost:8081

**Acesso:**

- Email: anasilva_pinhel@hotmail.com
- Password: informatica

**Estado:**

- ✅ Base de dados: MySQL ativo
- ✅ Serviço: Spring Boot ativo
- ✅ Validações: Todas implementadas
- ✅ UI/UX: Português com acentos
- ✅ Autenticação: Funcionando

---

## Ficheiros Importantes no Projeto

```
d:\Projeto de Informática\Take a Break App\
├── webapp/
│   ├── target/take-a-break-web-1.0.0.jar  ← Artefacto pronto
│   ├── src/main/resources/
│   │   ├── application.properties
│   │   └── schema.sql
│   └── README.md  ← Documentação completa
├── deploy/
│   ├── setup-ssh-windows-standalone.ps1  ← Script para servidor
│   ├── publish-remote.sh  ← Deploy automático
│   └── server-bootstrap.sh  ← Provisioning remoto
└── README.md  ← Documentação geral
```

---

## Troubleshooting

**Erro: "SSH não está a responder"**
→ Confirma que `setup-ssh-windows-standalone.ps1` executou até ao fim com "Setup Complete!"

**Erro: "Não consegue conectar ao MySQL"**
→ Confirma que tem MySQL instalado e a correr: `mysql -u anokas -p`

**Webapp não arranca**
→ Verifica logs: `tail -f /var/log/takeabreak/app.log`

---

**Aviso:** A password da BD (`Informatica_1706869`) está exposta neste ficheiro. Recomenda-se trocar após o deploy.
