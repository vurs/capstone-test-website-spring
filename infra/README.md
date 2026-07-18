# AWS deployment (EC2 + Terraform + Ansible)

Deploys this Spring test app to a single hardened EC2 instance. Access is limited to team CIDRs (and, during CI, the current GitHub-hosted runner IP).

## Why EC2 instead of Lightsail

EC2 security groups give precise CIDR allowlists for SSH and app ports, which is what we need for “team + scanner only.” Lightsail is slightly cheaper at the low end, but its firewall model is less flexible for this workflow.

Default size is **`t3.small` (2 GiB)** — about **$15/month** on-demand in `us-east-1`. That is the practical minimum for Postgres + Keycloak + Spring under Docker. The root volume is encrypted **gp3**.

### Cost components (us-east-1, always-on)

| Component | Typical cost | Notes |
|-----------|--------------|--------|
| EC2 `t3.small` | ~$12–15/mo | Main cost driver |
| EBS gp3 20 GiB | ~$1.60/mo | Root volume |
| Elastic IP | **$0** while attached | Charged only if allocated and **not** associated with a running instance |
| S3 state bucket | cents/mo | Tiny tfstate + occasional scan PDFs |
| SSM parameters | free tier / negligible | Standard parameters |
| Data transfer | usually low | Outbound to internet; scans are modest |

**Ballpark: ~$14–17/month** with the instance running 24/7. No NAT Gateway, Load Balancer, RDS, or other high-cost services. Destroy the stack when idle (`action=destroy`) to stop EC2/EBS charges; release the EIP with destroy so you are not billed for an idle address.

## One-time GitHub setup

Repository **Secrets**:

| Secret | Purpose |
|--------|---------|
| `AWS_ACCESS_KEY_ID` | IAM user/role key with EC2, VPC read, EIP, and S3 (state bucket) permissions |
| `AWS_SECRET_ACCESS_KEY` | Matching secret |
| `ALLOWED_CIDRS` | Comma-separated team CIDRs, e.g. `203.0.113.10/32,198.51.100.4/32` |
| `SCAN_AUTH_FIELDS` | Optional; for the scanner workflow, e.g. `{"username":"testuser","password":"password"}` |

`ALLOWED_CIDRS` must **not** include `0.0.0.0/0`. Use `/32` for individual public IPs.

Optional repository **Variables**:

| Variable | Default |
|----------|---------|
| `AWS_REGION` | `us-east-1` |
| `TF_STATE_BUCKET` | `capstone-spring-tfstate-<account-id>` (created automatically) |

Attach the managed policy document in [`iam/capstone-spring-gha-policy.json`](iam/capstone-spring-gha-policy.json) to the GitHub Actions IAM user (`capstone-spring-gha`).

That policy grants `s3:*` on `*` for this dedicated CI user so Terraform state bucket create/head/list/put all work. Tighten later if you want.

If the state-bucket step fails with **403** after you created the bucket manually:

1. IAM → Users → `capstone-spring-gha` → **Permissions** — confirm the policy is **Attached** (not only created).
2. IAM → Policies → your policy → **Edit** → replace JSON with `infra/iam/capstone-spring-gha-policy.json` → **Save changes**.
3. Set repository variable `TF_STATE_BUCKET` to the **exact** bucket name from the S3 console (must match character-for-character).
4. Locally verify with the same access keys:

```bash
export AWS_ACCESS_KEY_ID=...
export AWS_SECRET_ACCESS_KEY=...
export AWS_DEFAULT_REGION=us-east-1
aws s3api head-bucket --bucket YOUR_EXACT_BUCKET_NAME
aws s3api list-objects-v2 --bucket YOUR_EXACT_BUCKET_NAME --max-items 1
```

Both commands must succeed before the workflow will.

## Workflows

### `Deploy Spring App to AWS` (`.github/workflows/deploy-aws.yml`)

| Trigger | Behavior |
|---------|----------|
| **Run workflow → `deploy`** | Replace any existing managed instance, provision a new one, harden it, deploy the app |
| **Run workflow → `destroy`** | Tear down all managed resources; do **not** create a new instance |
| **Push to `main`** (app/infra paths) | Same as `deploy` |

Each `deploy`:

1. Applies Terraform with **team CIDRs only** (`ALLOWED_CIDRS`) in the security group
2. Temporarily allowlists **this deploy runner** for SSH/app ports, runs Ansible, then revokes that runner IP
3. Writes instance details to **private AWS SSM** under `/capstone-spring/*`

### `Capstone Vulnerability Scan`

Loads the app URL and security group ID from SSM. Temporarily allowlists **the scanner runner** (ports 8080/8081), runs the scan, then revokes that IP. PDF reports go to the private state bucket under `scan-reports/`.

CI runners are never left permanently in the security group—only team IPs from `ALLOWED_CIDRS` persist.

## Public repository and log exposure

This repo is public, so **anyone can read Actions logs and download Actions artifacts**. The workflows are written with that in mind:

| Avoided | Instead |
|---------|---------|
| Printing public IP / URLs / SG IDs in the job summary | Values are `::add-mask::`’d and stored in SSM `SecureString` parameters |
| GitHub Actions variables for `SCAN_TARGET_URL` | SSM `/capstone-spring/app_url` (only AWS credentials can read) |
| Public scan-report artifacts | Private S3 object in the state bucket |

Team members with AWS access retrieve details locally:

```bash
aws ssm get-parameter --name /capstone-spring/app_url --with-decryption --query Parameter.Value --output text
aws ssm get-parameter --name /capstone-spring/ssh_host --with-decryption --query Parameter.Value --output text
aws ssm get-parameter --name /capstone-spring/instance_id --query Parameter.Value --output text
aws ssm get-parameter --name /capstone-spring/security_group_id --query Parameter.Value --output text
```

SSH private key remains in Terraform state (private S3), not in GitHub:

```bash
cd infra/terraform
terraform output -raw ssh_private_key_pem > /tmp/capstone-spring.pem
chmod 600 /tmp/capstone-spring.pem
```

Masking is best-effort: unusual log formatting can still leak values. Do not paste instance details into issues, PR descriptions, or screenshots of the Actions UI.

## Instance hardening (defense in depth)

| Layer | Control |
|-------|---------|
| Network | Security group: TCP 22, 8080, 8081 **only** from allowlisted CIDRs (authoritative) |
| Host firewall | UFW deny-inbound default; only ports 22/8080/8081 open (CIDR filtering left to the SG so CI can add ephemeral runner IPs) |
| SSH | Key-only, no root login, `AllowUsers ubuntu`, fail2ban |
| OS | Unattended security upgrades |
| App surface | Postgres is **not** published on the host; only Keycloak `8080` and Spring `8081` |
| IMDS | IMDSv2 required on the instance |

This is not a formal compliance baseline (CIS/STIG), but it blocks unsolicited internet scanning and limits access to your team and CI runners.

## Manual operator access

After a successful deploy, read connection details from SSM (see above), then SSH with the key from Terraform state:

```bash
SSH_HOST="$(aws ssm get-parameter --name /capstone-spring/ssh_host --with-decryption --query Parameter.Value --output text)"
cd infra/terraform
terraform output -raw ssh_private_key_pem > /tmp/capstone-spring.pem
chmod 600 /tmp/capstone-spring.pem
ssh -i /tmp/capstone-spring.pem "$SSH_HOST"
```

## Local Terraform (optional)

```bash
cd infra/terraform
terraform init \
  -backend-config="bucket=YOUR_STATE_BUCKET" \
  -backend-config="key=capstone-spring/terraform.tfstate" \
  -backend-config="region=us-east-1" \
  -backend-config="encrypt=true"

cat > ci.auto.tfvars <<'EOF'
aws_region    = "us-east-1"
allowed_cidrs = ["203.0.113.10/32"]
EOF

terraform apply
```
