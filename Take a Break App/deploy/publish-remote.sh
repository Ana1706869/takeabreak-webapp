#!/usr/bin/env bash
set -euo pipefail

# Deploy from local machine to a remote Ubuntu server.
# Requirements: ssh/scp, Maven, sudo on remote user.
#
# Example:
# ./deploy/publish-remote.sh \
#   --host 203.0.113.10 --user anasilva --domain app.example.com \
#   --db-name take_a_break_web --db-user takeabreak --db-pass 'StrongPass123!'

HOST=""
USER_NAME=""
PORT="22"
DOMAIN="_"
DB_NAME="take_a_break_web"
DB_USER="takeabreak"
DB_PASS=""

while [[ $# -gt 0 ]]; do
  case "$1" in
    --host) HOST="$2"; shift 2 ;;
    --user) USER_NAME="$2"; shift 2 ;;
    --port) PORT="$2"; shift 2 ;;
    --domain) DOMAIN="$2"; shift 2 ;;
    --db-name) DB_NAME="$2"; shift 2 ;;
    --db-user) DB_USER="$2"; shift 2 ;;
    --db-pass) DB_PASS="$2"; shift 2 ;;
    *) echo "Unknown argument: $1"; exit 1 ;;
  esac
done

if [[ -z "${HOST}" || -z "${USER_NAME}" || -z "${DB_PASS}" ]]; then
  echo "Missing required args."
  echo "Required: --host --user --db-pass"
  exit 1
fi

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
WEBAPP_DIR="${ROOT_DIR}/webapp"
JAR_PATH="${WEBAPP_DIR}/target/take-a-break-web-1.0.0.jar"

# Avoid Maven clean failures caused by a running local instance locking target/*.jar.
LOCAL_PID="$(netstat -ano 2>/dev/null | tr -d '\r' | awk '/:8081[[:space:]]/ && /LISTENING/ {print $5; exit}')"
if [[ -n "${LOCAL_PID}" ]]; then
  echo "Stopping local process on port 8081 (PID ${LOCAL_PID}) to release locked jar..."
  cmd //c "taskkill /PID ${LOCAL_PID} /F" >/dev/null 2>&1 || true
fi

echo "[1/4] Building production jar..."
cd "${WEBAPP_DIR}"
mvn -DskipTests clean package spring-boot:repackage

if [[ ! -f "${JAR_PATH}" ]]; then
  echo "Jar not found at ${JAR_PATH}"
  exit 1
fi

echo "[2/4] Uploading bootstrap and jar..."
scp -P "${PORT}" "${ROOT_DIR}/deploy/server-bootstrap.sh" "${USER_NAME}@${HOST}:/tmp/server-bootstrap.sh"
scp -P "${PORT}" "${JAR_PATH}" "${USER_NAME}@${HOST}:/tmp/take-a-break-web-1.0.0.jar"

echo "[3/4] Provisioning remote server (will ask sudo password)..."
ssh -p "${PORT}" "${USER_NAME}@${HOST}" \
  "chmod +x /tmp/server-bootstrap.sh && sudo APP_DOMAIN='${DOMAIN}' DB_NAME='${DB_NAME}' DB_USER='${DB_USER}' DB_PASS='${DB_PASS}' bash /tmp/server-bootstrap.sh"

echo "[4/4] Installing artifact and restarting service..."
ssh -p "${PORT}" "${USER_NAME}@${HOST}" \
  "sudo mkdir -p /opt/takeabreak && sudo mv /tmp/take-a-break-web-1.0.0.jar /opt/takeabreak/take-a-break-web-1.0.0.jar && sudo chown takeabreak:takeabreak /opt/takeabreak/take-a-break-web-1.0.0.jar && sudo systemctl restart takeabreak && sudo systemctl status takeabreak --no-pager -l | head -n 20"

echo "Deploy completed."
if [[ "${DOMAIN}" != "_" ]]; then
  echo "Access URL: http://${DOMAIN}"
else
  echo "Access URL: http://${HOST}"
fi
