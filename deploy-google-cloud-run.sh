#!/bin/bash

set -e

echo "╔═══════════════════════════════════════════╗"
echo "║  GOOGLE CLOUD RUN - DEPLOYMENT AUTO      ║"
echo "║  (Free, sem trial)                        ║"
echo "╚═══════════════════════════════════════════╝"
echo ""

PROJECT_DIR="d:\Projeto de Informática"
cd "$PROJECT_DIR"

echo "[1/5] Verificando Google Cloud CLI..."

if ! command -v gcloud &> /dev/null; then
    echo "⚠️  Google Cloud SDK não instalado."
    echo ""
    echo "Download rápido (1 min):"
    echo "  1. https://cloud.google.com/sdk/docs/install-sdk"
    echo "  2. Escolher Windows → Download Google Cloud SDK"
    echo "  3. Executar installer"
    echo "  4. Correr: gcloud init"
    echo ""
    exit 1
fi

GCLOUD_VERSION=$(gcloud --version | head -1)
echo "✓ Google Cloud SDK: $GCLOUD_VERSION"
echo ""

echo "[2/5] Autenticando no Google Cloud..."

# Verificar se já autenticado
if ! gcloud auth list | grep ACTIVE > /dev/null; then
    echo "Abrindo browser para autenticação..."
    gcloud auth login --no-launch-browser 2>&1 | head -20
    echo ""
    echo "⚠️  Abra o link no browser, autorize e copie o código de volta"
else
    CURRENT_USER=$(gcloud auth list | grep ACTIVE | awk '{print $3}')
    echo "✓ Autenticado como: $CURRENT_USER"
fi
echo ""

echo "[3/5] Criando projeto Google Cloud..."

PROJECT_ID="takeabreak-webapp-$(date +%s | tail -c 5)"
PROJECT_NAME="Take A Break Webapp"

echo "Criando projeto '$PROJECT_NAME' (ID: $PROJECT_ID)..."
gcloud projects create "$PROJECT_ID" --name="$PROJECT_NAME" 2>&1 || echo "Projeto pode já existir"

gcloud config set project "$PROJECT_ID"
echo "✓ Projeto: $PROJECT_ID"
echo ""

echo "[4/5] Preparando aplicação para Cloud Run..."

# Cloud Run precisa de Dockerfile com PORT env var
echo "Verificando Dockerfile..."
if [ ! -f "webapp/Dockerfile" ]; then
    echo "❌ Dockerfile não encontrado em webapp/Dockerfile"
    exit 1
fi
echo "✓ Dockerfile existe"

# Criar cloudbuild.yaml para build automatizado
cat > cloudbuild.yaml << 'EOF'
steps:
  # Build da imagem Docker
  - name: 'gcr.io/cloud-builders/docker'
    args:
      - 'build'
      - '-t'
      - 'gcr.io/$PROJECT_ID/takeabreak-webapp:latest'
      - '-f'
      - 'webapp/Dockerfile'
      - '.'
      - '-t'
      - 'gcr.io/$PROJECT_ID/takeabreak-webapp:$SHORT_SHA'
      - '--build-arg'
      - 'BUILDKIT_INLINE_CACHE=1'

  # Push para Google Container Registry
  - name: 'gcr.io/cloud-builders/docker'
    args: ['push', 'gcr.io/$PROJECT_ID/takeabreak-webapp:latest']

  # Deploy no Cloud Run
  - name: 'gcr.io/cloud-builders/gke-deploy'
    args:
      - 'run'
      - '--filename=.'
      - '--image=gcr.io/$PROJECT_ID/takeabreak-webapp:latest'
      - '--location=europe-west1'
      - '--output=/workspace/output'

images:
  - 'gcr.io/$PROJECT_ID/takeabreak-webapp:latest'
  - 'gcr.io/$PROJECT_ID/takeabreak-webapp:$SHORT_SHA'

options:
  machineType: 'N1_HIGHCPU_8'
EOF

echo "✓ cloudbuild.yaml criado"
echo ""

echo "[5/5] Fazendo deploy..."
echo ""
echo "Comando de deploy:"
echo "  gcloud builds submit --region=europe-west1"
echo ""

# Tentar fazer build
gcloud builds submit \
    --region=europe-west1 \
    --config=cloudbuild.yaml \
    --substitutions=SHORT_SHA=latest \
    2>&1 | head -50 || (
        echo ""
        echo "❌ Build falhou (pode precisar ativar APIs)"
        echo ""
        echo "Ativar manualmente:"
        echo "  1. https://console.cloud.google.com"
        echo "  2. Ativar APIs: Cloud Build, Cloud Run, Container Registry"
        echo "  3. Correr: gcloud builds submit --region=europe-west1"
    )

echo ""
echo "╔═══════════════════════════════════════════╗"
echo "║  DEPLOY INICIADO                          ║"
echo "╚═══════════════════════════════════════════╝"
