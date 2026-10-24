#!/usr/bin/env bash
# Copies the built jar to /opt/sparktech-demo and restarts the service.
# Run from the project root after "mvn clean package".
set -euo pipefail

JAR=$(ls target/sparktech-demo-*.jar | grep -v '\.original$' | head -n 1)
echo "Deploying ${JAR}"

sudo mkdir -p /opt/sparktech-demo
sudo cp "${JAR}" /opt/sparktech-demo/app.jar
sudo chown -R ubuntu:ubuntu /opt/sparktech-demo
sudo systemctl restart sparktech-demo
sudo systemctl --no-pager status sparktech-demo
