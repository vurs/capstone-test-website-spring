#!/bin/bash
set -euo pipefail

# Minimal bootstrap only. Host hardening and app deploy are handled by Ansible.
export DEBIAN_FRONTEND=noninteractive

apt-get update -y
apt-get install -y python3 python3-apt sudo ca-certificates curl
