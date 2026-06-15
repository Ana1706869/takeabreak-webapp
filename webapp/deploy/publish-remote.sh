#!/usr/bin/env bash
set -euo pipefail

# Wrapper to allow running deploy from inside webapp/
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(cd "${SCRIPT_DIR}/../.." && pwd)"

exec "${ROOT_DIR}/deploy/publish-remote.sh" "$@"
