#!/bin/sh

# replace this with your own installation directory of keycloak
export KEYCLOAK_HOME=$HOME/tmp/keycloak-24.0.4

$KEYCLOAK_HOME/bin/kc.sh start-dev -Dkeycloak.migration.action=import \
-Dkeycloak.migration.realmName=workshop \
-Dkeycloak.migration.provider=singleFile -Dkeycloak.migration.file=./keycloak_realm_workshop.json \
-Dkeycloak.migration.strategy=OVERWRITE_EXISTING -Dkeycloak.profile=preview

