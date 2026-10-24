# Sparktech Demo Project

A small employee management REST API built with Spring Boot 3, Maven and Java 17.
It is used to practise the full flow: code, Git, Maven build, Jenkins CI, deploy to EC2.

## Tech stack

- Java 17 (builds fine on JDK 21 too)
- Spring Boot 3.5 (Web, Validation, Data JPA, Actuator)
- H2 database (in-memory for dev, file-based for prod)
- JUnit 5, Mockito, MockMvc
- Jenkins pipeline (`Jenkinsfile`)

## Project structure

```
sparktech-demo-project/
├── pom.xml
├── Jenkinsfile
├── Dockerfile
├── README.md
├── docs/API.md
├── deploy/sparktech-demo.service     systemd unit for the server
├── scripts/
│   ├── setup-server.sh               one-time server setup
│   └── deploy.sh                     copy jar and restart service
└── src/
    ├── main/
    │   ├── java/com/sparktech/demo/
    │   │   ├── SparktechDemoApplication.java
    │   │   ├── config/               startup config, seed data
    │   │   ├── controller/           REST endpoints
    │   │   ├── dto/                  request and response objects
    │   │   ├── exception/            custom exceptions, global handler
    │   │   ├── model/                JPA entities
    │   │   ├── repository/           database access
    │   │   └── service/              business logic
    │   └── resources/
    │       ├── application.properties
    │       ├── application-dev.properties
    │       └── application-prod.properties
    └── test/java/com/sparktech/demo/  unit and slice tests
```

## Run locally

```bash
mvn clean package
java -jar target/sparktech-demo-1.0.0-SNAPSHOT.jar
```

or during development: `mvn spring-boot:run`

- API: http://localhost:8080/api/v1/employees
- Health: http://localhost:8080/actuator/health
- H2 console (dev only): http://localhost:8080/h2-console  (JDBC URL `jdbc:h2:mem:sparktechdb`, user `sa`, empty password)

Run with the prod profile:

```bash
java -jar target/sparktech-demo-1.0.0-SNAPSHOT.jar --spring.profiles.active=prod
```

## Try the API

```bash
curl http://localhost:8080/api/v1/employees
curl "http://localhost:8080/api/v1/employees?department=HR"
curl -X POST http://localhost:8080/api/v1/employees \
  -H "Content-Type: application/json" \
  -d '{"name":"Meera","email":"meera@sparktech.com","department":"Engineering","salary":90000}'
```

See [docs/API.md](docs/API.md) for all endpoints.

## Tests

```bash
mvn test
```

## CI with Jenkins

1. Install Git, Maven and a JDK on the Jenkins server (`scripts/setup-server.sh`).
2. Create a **Pipeline** job, choose **Pipeline script from SCM**, set the repo URL and branch `*/main`.
3. Script path: `Jenkinsfile`.
4. Every push is picked up within 2 minutes (polling) and runs build, test and archive.

## Deploy to a server

```bash
./scripts/setup-server.sh    # first time only
mvn clean package
./scripts/deploy.sh
```

## Git workflow

```bash
git checkout -b feature/<short-name>
# make changes
mvn clean test
git add .
git commit -m "Describe the change"
git push -u origin feature/<short-name>
```

Then open a Pull Request into `main`.
