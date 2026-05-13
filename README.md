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

##### The following steps need to be done after cloning, and anytime new Maven dependencies are added to the project:

1. In IntelliJ, on the right side, you should see an 'm' icon for Maven. If not, open the "app" folder and you will see a pom.xml file. Right-click it and select "Add as Maven Project"
2. Click the 'm' icon on the right side, then click the "Sync All Maven Projects" icon
3. While still in the Maven window, click the "Execute Maven Goal" icon, and type "mvn clean package" and press Enter

##### The following steps need to be done every time you want to deploy:

1. Ensure Docker Desktop is running
2. Open the terminal inside IntelliJ and run "docker compose up --build" to launch the Spring app, Postgres server, and Keycloak server
3. Visit localhost:8081 in your browser to access the website
4. All pages of the website (minus the landing page) are login-protected. If prompted to login, use the sample user (Username is "testuser" and password is "password")

#### How to Shut Down

1. If you would like database data to persist, run "docker compose down"
2. If you want to destroy all database data for a clean reset, run "docker compose down -v"

#### Troubleshooting

On macOS, you may run into an issue where your browser cannot resolve "host.docker.internal", thus breaking all Keycloak functionality. To fix this, you need to do the following:
1. Add the following line to your /etc/hosts file:
    * 127.0.0.1       host.docker.internal
2. Run the following CLI command to flush your DNS cache:
   * sudo dscacheutil -flushcache; sudo killall -HUP mDNSResponder

### Current Project Status

The following features have been created so far:
* Scanner Phase 1: Crawler/Spider
* Scanner Phase 2: Payload Injector
* Scanner Phase 3: Vulnerability Verifier
* Scanner Phase 4: Report Generator
* Reflected and Stored XSS payloads
* Vulnerable Test Web Application to test scanner against

### Features Still in Progress

The following features are in progress:
* Authentication mechanism for vulnerability scanner
* Additional payloads (SQLi, Command Injection, Security Misconfiguration, Broken Access Control, Exposed Directories and Files, Buffer Overflow, No Anti-CSRF Tokens, etc.)
* Desktop application for vulnerability scanner
* GitHub Actions Workflow for vulnerability scanner
* AJAX/Single-Page Application (SPA) support for vulnerability scanner

### Known Issues and Limitations

* Currently only scans for XSS
* Currently only works on standard HTML-based websites, does not work on Single-Page Applications (SPAs) such as Angular or React

### Links to Relevant Documentation, Diagrams, and Demos

* [Team Jira](https://sheridan-cyber-capstone-2026.atlassian.net/jira/software/projects/SCRUM/boards/1) (Coordinate with me for access)
* [Revised Project Plan for Phase 2](https://sheridan-cyber-capstone-2026.atlassian.net/wiki/x/AQD-AQ) (Coordinate with me for access)
* [System Architecture Diagram](https://drive.google.com/file/d/15G35238mbmTRRoIVubBf7kuU-v8xIKI9/view?usp=sharing)
* [Repo Contributions Distribution](https://github.com/vurs/capstone-vulnerability-scanner/graphs/contributors)
* Project demo coming soon...
