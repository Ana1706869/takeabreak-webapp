#!/usr/bin/env bash
set -euo pipefail

# Usage (run on remote Ubuntu server as root):
# APP_DOMAIN=app.example.com \
# DB_NAME=take_a_break_web DB_USER=takeabreak DB_PASS='strong-pass' \
# bash server-bootstrap.sh

if [[ "${EUID}" -ne 0 ]]; then
  echo "Run as root (sudo)."
  exit 1
fi

APP_DOMAIN="${APP_DOMAIN:-_}"
APP_NAME="takeabreak"
APP_USER="takeabreak"
APP_GROUP="takeabreak"
APP_DIR="/opt/takeabreak"
APP_LOG_DIR="/var/log/takeabreak"
ENV_DIR="/etc/takeabreak"
ENV_FILE="${ENV_DIR}/takeabreak.env"
SERVICE_FILE="/etc/systemd/system/takeabreak.service"
NGINX_SITE="/etc/nginx/sites-available/takeabreak"

DB_NAME="${DB_NAME:-take_a_break_web}"
DB_USER="${DB_USER:-takeabreak}"
DB_PASS="${DB_PASS:-change-me-now}"

export DEBIAN_FRONTEND=noninteractive
apt-get update -y
apt-get install -y openjdk-21-jre-headless nginx mysql-server ufw curl

if ! id -u "${APP_USER}" >/dev/null 2>&1; then
  useradd --system --home "${APP_DIR}" --shell /usr/sbin/nologin "${APP_USER}"
fi

mkdir -p "${APP_DIR}" "${APP_LOG_DIR}" "${ENV_DIR}"
chown -R "${APP_USER}:${APP_GROUP}" "${APP_DIR}" "${APP_LOG_DIR}"
chmod 750 "${APP_DIR}" "${APP_LOG_DIR}"

mysql -uroot <<SQL
CREATE DATABASE IF NOT EXISTS ${DB_NAME} CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS '${DB_USER}'@'localhost' IDENTIFIED BY '${DB_PASS}';
ALTER USER '${DB_USER}'@'localhost' IDENTIFIED BY '${DB_PASS}';
GRANT ALL PRIVILEGES ON ${DB_NAME}.* TO '${DB_USER}'@'localhost';
FLUSH PRIVILEGES;
SQL

cat > "${ENV_FILE}" <<EOF
DB_URL=jdbc:mysql://localhost:3306/${DB_NAME}?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
DB_USER=${DB_USER}
DB_PASS=${DB_PASS}
DATASOURCE_DRIVER=com.mysql.cj.jdbc.Driver
JAVA_OPTS=--enable-native-access=ALL-UNNAMED
EOF
chmod 640 "${ENV_FILE}"
chown root:"${APP_GROUP}" "${ENV_FILE}"

cat > "${SERVICE_FILE}" <<'EOF'
[Unit]
Description=Take a Break Webapp
After=network.target mysql.service
Wants=mysql.service

[Service]
Type=simple
User=takeabreak
Group=takeabreak
EnvironmentFile=/etc/takeabreak/takeabreak.env
WorkingDirectory=/opt/takeabreak
ExecStart=/bin/bash -lc 'exec /usr/bin/java ${JAVA_OPTS} -jar /opt/takeabreak/take-a-break-web-1.0.0.jar'
Restart=always
RestartSec=5
StandardOutput=append:/var/log/takeabreak/app.log
StandardError=append:/var/log/takeabreak/error.log

[Install]
WantedBy=multi-user.target
EOF

cat > "${NGINX_SITE}" <<EOF
server {
    listen 80;
    server_name ${APP_DOMAIN};

    location / {
        proxy_pass http://127.0.0.1:8081;
        proxy_set_header Host \$host;
        proxy_set_header X-Real-IP \$remote_addr;
        proxy_set_header X-Forwarded-For \$proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto \$scheme;
    }
}
EOF

ln -sf "${NGINX_SITE}" /etc/nginx/sites-enabled/takeabreak
rm -f /etc/nginx/sites-enabled/default
nginx -t
systemctl enable nginx mysql
systemctl restart nginx mysql

ufw allow OpenSSH || true
ufw allow 'Nginx Full' || true
ufw --force enable || true

systemctl daemon-reload
systemctl enable takeabreak

echo "Bootstrap complete."
echo "Next: upload JAR to ${APP_DIR}/take-a-break-web-1.0.0.jar and run: systemctl restart takeabreak"
