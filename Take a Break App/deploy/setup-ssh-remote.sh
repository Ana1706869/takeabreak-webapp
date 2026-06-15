#!/usr/bin/env bash

# Alternative: SSH setup script for Ubuntu/Debian servers
# Usage on remote Ubuntu: bash setup-ssh-remote.sh

if [[ "${EUID}" -ne 0 ]]; then
  echo "Run with sudo: sudo bash setup-ssh-remote.sh"
  exit 1
fi

echo "====== SSH Setup for Linux ======"
apt-get update -y
apt-get install -y openssh-server openssh-client

systemctl enable ssh
systemctl restart ssh

ufw allow 22/tcp 2>/dev/null || true
ufw --force enable 2>/dev/null || true

echo "SSH Status:"
systemctl status ssh --no-pager

echo ""
echo "Listening on port 22:"
netstat -tlnp | grep :22 || ss -tlnp | grep :22

echo ""
echo "SSH Setup Complete!"
echo "You can now run the deploy script from your local machine."
