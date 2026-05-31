$ErrorActionPreference = "Stop"

$repoRoot = Split-Path -Parent $PSScriptRoot

git -C $repoRoot config core.hooksPath dev_scripts/git-hooks

New-Item -ItemType File -Path (Join-Path $repoRoot ".dev-env-stamp") -Force | Out-Null
New-Item -ItemType File -Path (Join-Path $repoRoot "app/.dev-env-stamp") -Force | Out-Null

Write-Host "Development setup complete: Git hooks installed (commit-msg: Jira ticket prefix)."
Write-Host "Created .dev-env-stamp files for local and Docker startup checks."
