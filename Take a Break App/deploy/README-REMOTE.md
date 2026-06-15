# Deploy remoto (Ubuntu + Nginx + systemd)

Este diretorio tem scripts para publicar a webapp num servidor remoto Ubuntu.

## 1) Pré-requisitos locais

- SSH e SCP funcionais para o servidor
- Maven instalado localmente
- Utilizador remoto com permissões de sudo

## 2) Comando de deploy

No raiz do projeto:

```bash
chmod +x deploy/publish-remote.sh deploy/server-bootstrap.sh
./deploy/publish-remote.sh \
  --host SEU_IP_OU_DOMINIO \
  --user SEU_USER \
  --port 22 \
  --domain SEU_DOMINIO \
  --db-name take_a_break_web \
  --db-user takeabreak \
  --db-pass 'MUDE_PARA_UMA_PASSWORD_FORTE'
```

## 3) O que o script configura no servidor

- Java 21 JRE
- MySQL Server
- Nginx (reverse proxy para 127.0.0.1:8081)
- Firewall UFW (SSH + Nginx)
- Serviço systemd: takeabreak
- Variáveis em /etc/takeabreak/takeabreak.env

## 4) Comandos úteis no servidor

```bash
sudo systemctl status takeabreak
sudo journalctl -u takeabreak -f
sudo tail -f /var/log/takeabreak/app.log
sudo tail -f /var/log/takeabreak/error.log
sudo systemctl restart takeabreak
```

## 5) HTTPS (opcional)

Se o domínio já apontar para o servidor:

```bash
sudo apt-get install -y certbot python3-certbot-nginx
sudo certbot --nginx -d SEU_DOMINIO
```
