#!/usr/bin/env sh

set -e

echo "Installing dependencies..."
apk add --no-cache curl jq >/dev/null 2>&1

DEFAULT_USER="admin"
DEFAULT_PASS="admin"
NEW_USER=$NEW_NEXUS_USERNAME
NEW_PASS=$NEW_NEXUS_PASSWORD
INIT_MARKER="/nexus-data/.initialized"
INIT_MARKER_DIR="/nexus-data"

echo "Waiting for Nexus to be fully started..."

until curl -sf -o /dev/null "$NEXUS_URL/service/rest/v1/status/writable"; do
  echo "⏳ Waiting for Nexus to be ready..."
  sleep 5
done

echo "If the marker exists, skip initialization."
if [ -f "$INIT_MARKER" ]; then
  echo "✅ Nexus already initialized — skipping setup."
  exit 0
else
  echo "The marker doesn't exists, Nexus will be initialized."
fi

echo "Nexus REST is started"

echo "Detecting current credentials..."

if curl -sf -o /dev/null -u "$NEW_USER:$NEW_PASS" "$NEXUS_URL/service/rest/v1/status/writable"; then
  echo "New credentials already set, skipping initialization"
  exit 0
elif curl -sf -o /dev/null -u "$DEFAULT_USER:$DEFAULT_PASS" "$NEXUS_URL/service/rest/v1/status/writable"; then
  echo "Default credentials detected, proceeding with initialization"
  CURRENT_USER="$DEFAULT_USER"
  CURRENT_PASS="$DEFAULT_PASS"
elif curl -sf -o /dev/null -u "$DEFAULT_USER:$NEW_PASS" "$NEXUS_URL/service/rest/v1/status/writable"; then
  echo "Password already changed but username is still default, continuing..."
  CURRENT_USER="$DEFAULT_USER"
  CURRENT_PASS="$NEW_PASS"
else
  echo "ERROR: Cannot authenticate with any known credentials"
  exit 1
fi

if [ "$CURRENT_PASS" = "$DEFAULT_PASS" ]; then
  echo "Changing admin password..."
  curl -f -u "$CURRENT_USER:$CURRENT_PASS" \
    -X PUT \
    -H "Content-Type: text/plain" \
    --data "$NEW_PASS" \
    "$NEXUS_URL/service/rest/v1/security/users/admin/change-password"
  echo "Password changed"
  CURRENT_PASS="$NEW_PASS"
fi

echo "📜 Accepting EULA..."
EULA_RESPONSE=$(curl -s -u "$CURRENT_USER:$CURRENT_PASS" \
  "$NEXUS_URL/service/rest/v1/system/eula")
EULA_PAYLOAD=$(echo "$EULA_RESPONSE" | jq '.accepted = true')
curl -f -u "$CURRENT_USER:$CURRENT_PASS" \
  -X POST \
  -H "Content-Type: application/json" \
  -d "$EULA_PAYLOAD" \
  "$NEXUS_URL/service/rest/v1/system/eula"
echo "EULA accepted"

if [ "$NEW_USER" != "$DEFAULT_USER" ]; then
  echo "Creating new admin user: $NEW_USER..."
  curl -f -u "$CURRENT_USER:$CURRENT_PASS" \
    -X POST \
    -H "Content-Type: application/json" \
    -d "{
      \"userId\": \"$NEW_USER\",
      \"firstName\": \"Administrator\",
      \"lastName\": \"User\",
      \"emailAddress\": \"admin@example.org\",
      \"password\": \"$NEW_PASS\",
      \"source\": \"default\",
      \"status\": \"active\",
      \"roles\": [\"nx-admin\"]
    }" \
    "$NEXUS_URL/service/rest/v1/security/users"
  echo "User $NEW_USER created"

  echo "Disabling default admin user..."
  curl -f -u "$NEW_USER:$NEW_PASS" \
    -X PUT \
    -H "Content-Type: application/json" \
    -d '{
      "userId": "admin",
      "firstName": "Administrator",
      "lastName": "User",
      "emailAddress": "admin@example.org",
      "source": "default",
      "status": "disabled",
      "roles": ["nx-admin"]
    }' \
    "$NEXUS_URL/service/rest/v1/security/users/admin"
fi

echo "🗑️ Removing default maven-public group (if exists)..."
curl -f -u "$NEW_USER:$NEW_PASS" \
  -X DELETE \
  "$NEXUS_URL/service/rest/v1/repositories/maven-public" || true
echo "🗑️ Removing default maven-releases (if exists)..."
curl -f -u "$NEW_USER:$NEW_PASS" \
  -X DELETE \
  "$NEXUS_URL/service/rest/v1/repositories/maven-releases" || true

echo "📦 Creating Confluent proxy repository..."
if [ ! -f "/schemas/confluent_proxy.json" ]; then
  echo "ERROR: /schemas/confluent_proxy.json not found"
  exit 1
fi
curl -f -u "$NEW_USER:$NEW_PASS" \
  -X POST \
  -H "Content-Type: application/json" \
  -d @/schemas/confluent_proxy.json \
  "$NEXUS_URL/service/rest/v1/repositories/maven/proxy"
echo "Confluent proxy repository created"

echo "📦 Creating maven-releases..."
if [ ! -f "/schemas/maven_releases.json" ]; then
  echo "ERROR: /schemas/maven_releases.json not found"
  exit 1
fi
curl -f -u "$NEW_USER:$NEW_PASS" \
  -X POST \
  -H "Content-Type: application/json" \
  -d @/schemas/maven_releases.json \
  "$NEXUS_URL/service/rest/v1/repositories/maven/hosted"
echo "Maven-releases repository created"

echo "📦 Creating custom maven-public group..."
if [ ! -f "/schemas/maven_public_group.json" ]; then
  echo "ERROR: /schemas/maven_public_group.json not found"
  exit 1
fi
curl -f -u "$NEW_USER:$NEW_PASS" \
  -X POST \
  -H "Content-Type: application/json" \
  -d @/schemas/maven_public_group.json \
  "$NEXUS_URL/service/rest/v1/repositories/maven/group"
echo "Maven repositories group created"

echo "Set marker. Before installing, make sure the directory exists..."
mkdir -p "$INIT_MARKER_DIR"
touch "$INIT_MARKER"

echo "✅ Nexus is fully ready for deployments!"
