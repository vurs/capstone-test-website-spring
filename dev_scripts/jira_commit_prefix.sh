#!/usr/bin/env bash
set -euo pipefail

commit_msg_file="$1"
original_message="$(<"$commit_msg_file")"
original_message="${original_message%"${original_message##*[![:space:]]}"}"
original_message="${original_message#"${original_message%%[![:space:]]*}"}"

if [[ "$original_message" == Merge* ]]; then
  exit 0
fi

branch_name="$(git rev-parse --abbrev-ref HEAD)"
jira_id="$(grep -oE 'SCRUM-[0-9]+' <<<"$branch_name" | head -n1 || true)"

if [[ -z "$jira_id" ]]; then
  echo "No Jira ticket ID found in branch name."
  echo "Please ensure the branch name starts with a valid Jira ticket ID, such as SCRUM-123-test-branch."
  echo "To rename the branch, use the command: git branch -m <new-branch-name>."
  echo "Then, commit your changes with the new branch name."
  exit 1
fi

if [[ "$original_message" == "$jira_id"* ]]; then
  exit 0
fi

updated_message="${jira_id} ${original_message}"
printf '%s\n' "$updated_message" >"$commit_msg_file"
echo "Updated commit message: ${updated_message}"
