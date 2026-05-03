# Capstone Test Website Spring

A vulnerable Spring Boot test web application.

Intended for use with the associated [vulnerability scanner](https://github.com/vurs/capstone-vulnerability-scanner/tree/main).

### How to Deploy
#### The following steps only need to be done once:
1. Ensure Docker Desktop is installed on your PC and running
2. Clone this repository to your PC and open it in IntelliJ IDEA
3. On the right side, you should see an 'm' icon for Maven. If not, open the "app" folder and you will see a pom.xml file. Right-click it and select "Add as Maven Project"
4. Click the 'm' icon on the right side, then click the "Sync All Maven Projects" icon
5. While still in the Maven window, click the "Execute Maven Goal" icon, and type "mvn clean package" and press Enter

#### The following steps need to be done every time:
1. Open the terminal inside IntelliJ and run "docker compose up --build" to launch the Spring app, Postgres server, and Keycloak server
2. Visit localhost:8081 in your browser to access the website
3. All pages of the website (minus the landing page) are login-protected. If prompted to login, use the sample user (Username is "testuser" and password is "password")

### How to Shut Down
1. If you would like database data to persist, run "docker compose down"
2. If you want to destroy all database data for a clean reset, run "docker compose down -v"