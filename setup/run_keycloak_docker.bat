@echo off

setlocal

rem replace this config with your own directory
set WORKSHOP_HOME=C:\secure-oauth2-oidc-workshop\setup

docker run --rm -p 8080:8080 -e DB_VENDOR=h2 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin -v $WORKSHOP_HOME/keycloak_realm_workshop.json:/data/import/keycloak_realm_workshop.json quay.io/keycloak/keycloak:24.0.4 start-dev --import-realm

