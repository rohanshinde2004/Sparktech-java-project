#!/usr/bin/env bash
# One-time setup of an Ubuntu server to build and run this project.
set -euo pipefail

sudo apt update
sudo apt install -y openjdk-21-jdk git maven

sudo cp deploy/sparktech-demo.service /etc/systemd/system/sparktech-demo.service
sudo systemctl daemon-reload
sudo systemctl enable sparktech-demo
echo "Server ready. Build the jar, then run scripts/deploy.sh"
