# Capstone Test Website Spring

A vulnerable Spring Boot test web application.

Intended for use with the associated [vulnerability scanner](https://github.com/vurs/capstone-vulnerability-scanner/tree/main).

### Project Overview

Our team is building a Dynamic Application Security Testing (DAST) tool to perform automated scans of web applications, inject crafted payloads, identify web vulnerabilities, and generate user-friendly reports that explain the found vulnerabilities and offer remediation recommendations.

Note that this is not a tool that scans source code like Checkmarx, SonarQube, Semgrep, or other Static Application Security Testing (SAST) tools. This is a tool that scans live deployed applications and is essentially an automated black-box penetration test, with payloads that are able to penetrate from the frontend to the backend to identify vulnerabilities such as SQLi and command injection, to name a few.

We aim to produce a desktop application for the scanner, where developers can manually configure and run scans on their live web apps. Additionally, one major benefit of DAST is the fact that it can be integrated into CI/CD pipelines to scan code changes automatically, thus we will also be producing a GitHub Actions Workflow, where development teams can import the workflow into their own GitHub repositories and configure scans that happen on a scheduled basis, or on every merge to main, or any other flexible schedule of their choice.

### Setup and Installation Instructions

#### How to Deploy

##### The following steps only need to be done once:

1. Ensure Docker Desktop is installed on your PC
2. Clone this repository to your PC and open it in IntelliJ IDEA
3. In the repository directory, run `./dev_scripts/setup_dev.sh` to configure git hooks and create the `.dev-env-stamp` required for local and Docker app startup. If on Windows, a popup might appear asking you what tool you want to run the script with; choose Git Bash. Branch names must include a Jira ticket ID (for example `SCRUM-123-my-branch`); commits on those branches are prefixed with the ticket ID automatically.

##### The following steps need to be done after cloning, and anytime new Maven dependencies are added to the project:

1. In IntelliJ, on the right side, you should see an 'm' icon for Maven. If not, open the "app" folder and you will see a pom.xml file. Right-click it and select "Add as Maven Project"
2. Click the 'm' icon on the right side, then click the "Sync All Maven Projects" icon
3. While still in the Maven window, click the "Execute Maven Goal" icon, and type "mvn clean package" and press Enter

##### The following steps need to be done every time you want to deploy:

1. Ensure Docker Desktop is running
2. Open the terminal inside IntelliJ and run "docker compose up --build" to launch the Spring app, Postgres server, and Keycloak server
3. Visit localhost:8081 in your browser to access the website
4. All pages of the website (minus the landing page) are login-protected. If prompted to login, use the sample user (Username is "testuser" and password is "password")

#### API-only scanner testing

The app exposes OpenAPI docs and scanner-friendly REST endpoints on the same port (`8081`) as the HTML site. No separate API server is required.

**OpenAPI spec:** `http://127.0.0.1:8081/v3/api-docs`

**Injectable REST endpoints (intentionally vulnerable):**

| Method | Path | Parameter | Vulnerability |
|--------|------|-----------|---------------|
| GET | `/users/search` | `username` (query) | SQL injection |
| GET | `/network/ping` | `host` (query) | Command injection |

These routes are open without Keycloak so the vulnerability scanner can reach them in `--scan-profile api` mode. HTML pages and other routes still use Keycloak login.

**Unauthenticated API-only scan** (from the scanner repo):

```
python main.py http://127.0.0.1:8081 \
  --scan-profile api \
  --openapi-url /v3/api-docs
```

**Authenticated API-only scan** (optional JSON login for scanner tooling):

```
python main.py http://127.0.0.1:8081 \
  --scan-profile api \
  --openapi-url /v3/api-docs \
  --auth-api-url /api/auth/login \
  --auth-field username=testuser \
  --auth-field password=password
```

`POST /api/auth/login` accepts `{"username":"testuser","password":"password"}` and returns a session cookie. This is separate from Keycloak and exists only to support scanner authentication testing.

**Automated regression test** (from the scanner repo, with this app running):

```
pytest --run-integration tests/integration/test_openapi_api_scan.py
```

Or: `python dev_scripts/verify_spring_api_scan.py`

#### How to Shut Down

1. If you would like database data to persist, run "docker compose down"
2. If you want to destroy all database data for a clean reset, run "docker compose down -v"

#### Troubleshooting

On macOS, you may run into an issue where your browser cannot resolve "host.docker.internal", thus breaking all Keycloak functionality. To fix this, you need to do the following:
1. Add the following line to your /etc/hosts file:
    * 127.0.0.1       host.docker.internal
2. Run the following CLI command to flush your DNS cache:
   * sudo dscacheutil -flushcache; sudo killall -HUP mDNSResponder
  
On Windows, you may run into an issue where your browser cannot resolve "host.docker.internal", thus breaking all Keycloak functionality. To fix this, you need to do the following:
1. Add the following line to the bottom of your C:\Windows\System32\drivers\etc\hosts file, and delete the other entry for host.docker.internal:
    * 127.0.0.1 host.docker.internal
2. Save the file and retry the application

### Current Project Status

The following features have been created so far:

* Scanner Phase 1: Crawler/Spider (HTTP and browser/Selenium modes)
* Scanner Phase 2: Payload Injector
* Scanner Phase 3: Vulnerability Verifier
* Scanner Phase 4: Report Generator
* Reflected and Stored XSS payloads
* SQL injection payloads (differential and error-based detection)
* Command injection payloads
* Security header misconfiguration checks
* Broken access control checks (on a feature branch)
* Exposed directories and files checks (on a feature branch)
* Vulnerable test web application to test scanner against
* Authentication for HTML form login and JSON API login (SPA-friendly)
* Session probe-based re-authentication
* Hash-route aware browser crawling for SPAs
* Base desktop application for vulnerability scanner

### Features Still in Progress

The following features are in progress:

* Additional payloads (Buffer Overflow, No Anti-CSRF Tokens, etc.)
* Increased desktop application functionality for vulnerability scanner
* GitHub Actions Workflow for vulnerability scanner (allows scans to be run on a schedule or for important events such as PRs)
* REST/API endpoint discovery for SPA backends (Juice Shop seeds, query-param URLs, browser network capture, OpenAPI)
* API injection for SQLi, XSS, and command injection on discovered REST endpoints

### Known Issues and Limitations

* **Traditional HTML sites** work best with HTTP crawl mode.
* **SPAs** require browser crawl mode to discover client-rendered routes and hash-based navigation (for example `#/about`). REST API endpoints are discovered via seeds, query parameters, browser network capture, and OpenAPI specs; injection runs against those endpoints separately from HTML forms.
* **Session probing** is opt-in. Targets with authenticated scans should expose a probe endpoint (for example `/api/session/me`) that returns `401`/`403` when logged out, or configure `--auth-session-probe-json-field` when the probe returns `200` for both states.
* **Browser mode** requires Selenium and Chrome. Overlays, cookie banners, and heavy client-side rendering can still limit link discovery on some apps.

### Links to Relevant Documentation, Diagrams, and Demos

* [Team Jira](https://sheridan-cyber-capstone-2026.atlassian.net/jira/software/projects/SCRUM/boards/1) (Coordinate with me for access)
* [Revised Project Plan for Phase 2](https://sheridan-cyber-capstone-2026.atlassian.net/wiki/x/AQD-AQ) (Coordinate with me for access)
* [System Architecture Diagram](https://drive.google.com/file/d/15G35238mbmTRRoIVubBf7kuU-v8xIKI9/view?usp=sharing)
* [Repo Contributions Distribution](https://github.com/vurs/capstone-vulnerability-scanner/graphs/contributors)
* Project demo coming soon...
