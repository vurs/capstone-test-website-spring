#!/usr/bin/env bash
set -euo pipefail

repo_root="$(cd "$(dirname "$0")/.." && pwd)"

chmod +x "$repo_root/dev_scripts/jira_commit_prefix.sh"
chmod +x "$repo_root/dev_scripts/git-hooks/commit-msg"

git -C "$repo_root" config core.hooksPath dev_scripts/git-hooks

echo "Git hooks installed (commit-msg: Jira ticket prefix)."
