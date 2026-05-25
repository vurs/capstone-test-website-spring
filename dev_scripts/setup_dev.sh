#!/usr/bin/env bash
set -euo pipefail

# Resolve the repository root so this script works no matter where it is run from.
repo_root="$(cd "$(dirname "$0")/.." && pwd)"

# Ensure the helper script and hook entrypoint are executable before Git uses them.
chmod +x "$repo_root/dev_scripts/jira_commit_prefix.sh"
chmod +x "$repo_root/dev_scripts/git-hooks/commit-msg"

# Point Git at the repository's shared hooks directory.
git -C "$repo_root" config core.hooksPath dev_scripts/git-hooks

# Mark both the repository root and the app build context as initialized.
touch "$repo_root/.dev-env-stamp"
touch "$repo_root/app/.dev-env-stamp"

# Confirm the development setup step completed successfully.
echo "Development setup complete: Git hooks installed (commit-msg: Jira ticket prefix)."
echo "Created .dev-env-stamp files for local and Docker startup checks."
