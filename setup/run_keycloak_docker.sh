#!/bin/sh

# replace this config with your own directory
export WORKSHOP_HOME=/home/afa/Development/localgit/secure-oauth2-oidc-workshop/setup

docker run --rm -p 8080:8080 -e DB_VENDOR=h2 \
           -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin \
           -v $WORKSHOP_HOME/keycloak_realm_workshop.json:/data/import/keycloak_realm_workshop.json quay.io/keycloak/keycloak:24.0.4 \
           start-dev -Dkeycloak.migration.action=import \
           -Dkeycloak.migration.realmName=workshop \
           -Dkeycloak.migration.provider=singleFile -Dkeycloak.migration.file=/data/import/keycloak_realm_workshop.json \
           -Dkeycloak.migration.strategy=OVERWRITE_EXISTING -Dkeycloak.profile=preview