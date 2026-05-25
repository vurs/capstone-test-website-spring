#!/usr/bin/env bash
set -euo pipefail

# Resolve the repository root so this script works no matter where it is run from.
repo_root="$(cd "$(dirname "$0")/.." && pwd)"

# Ensure the helper script and hook entrypoint are executable before Git uses them.
chmod +x "$repo_root/dev_scripts/jira_commit_prefix.sh"
chmod +x "$repo_root/dev_scripts/git-hooks/commit-msg"

# Point Git at the repository's shared hooks directory.
git -C "$repo_root" config core.hooksPath dev_scripts/git-hooks

# Confirm the development setup step completed successfully.
echo "Development setup complete: Git hooks installed (commit-msg: Jira ticket prefix)."
