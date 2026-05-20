# Hybrid Cloud Client Impact Portal

Hybrid Cloud Client Impact Portal is a Java Spring Boot portfolio application that simulates an enterprise consulting platform for clients moving toward hybrid cloud and AI-enabled operations. It includes role-based login, client management, project tracking, JSON/XML cloud assessment analysis, REST APIs, mock cloud resources, logging, and automated tests.

GitHub repository:

```text
https://github.com/slyskenk/hybrid-cloud-client-impact-portal
```

## IBM Consulting Alignment

- Java OOP: domain inheritance, encapsulated entities, services, DTOs, enums, and validation.
- Core CS: lists, maps, grouping, sorting, search, and rule-based recommendation logic.
- Web technologies: Spring MVC, Thymeleaf, HTML, CSS, JavaScript, and JQuery-compatible pages.
- REST architecture: `/api/clients`, `/api/projects`, `/api/analytics`.
- Data integration: JSON and XML cloud assessment import, analysis, and JSON export.
- Collaboration workflow: Admin, Consultant, and Client roles.
- Troubleshooting: SLF4J logging, centralized exception handling, Maven tests, and GitHub Actions CI.

## Features

- Dashboard with portfolio metrics, client readiness, project status, and mock cloud resources.
- Client page with searchable client table and client creation form.
- Project page with project creation and status update workflow.
- Analytics page that accepts JSON or XML assessments and produces migration recommendations.
- API endpoints protected with HTTP Basic auth for integration-style testing.
- H2 in-memory database seeded with sample consulting data at startup.

## Demo Accounts

| Role | Username | Password |
| --- | --- | --- |
| Admin | `admin` | `admin123` |
| Consultant | `consultant` | `consult123` |
| Client | `client` | `client123` |

## Requirements

- Java 17 or newer
- Maven 3.9 or newer
- Windows PowerShell for the helper scripts

The project is compiled for Java 17. It was also tested locally with Java 24.

## Build

Using a global Maven install:

```powershell
mvn package
```

Using the local Maven install included in this workspace:

```powershell
tools\apache-maven-3.9.16\bin\mvn.cmd "-Dmaven.repo.local=.m2\repository" package
```

Using the helper script:

```powershell
.\scripts\build.ps1
```

The build creates:

```text
target\hybrid-cloud-client-impact-portal-0.1.0-SNAPSHOT.jar
```

## Run The Application

Run directly from source:

```powershell
mvn spring-boot:run
```

Run the packaged JAR:

```powershell
java -jar target\hybrid-cloud-client-impact-portal-0.1.0-SNAPSHOT.jar
```

Run on another port:

```powershell
java -jar target\hybrid-cloud-client-impact-portal-0.1.0-SNAPSHOT.jar --server.port=8081
```

Run with the helper script:

```powershell
.\scripts\run-jar.ps1
.\scripts\run-jar.ps1 -Port 8081
```

Then open:

```text
http://localhost:8080/login
```

## H2 Database Console

The H2 console is available to the Admin role:

```text
http://localhost:8080/h2-console
```

Use:

```text
JDBC URL: jdbc:h2:mem:impactportal
User: sa
Password:
```

The database is in-memory, so data resets each time the app restarts.

## API Examples

List clients:

```powershell
curl.exe -u consultant:consult123 http://localhost:8080/api/clients
```

List analytics summary:

```powershell
curl.exe -u consultant:consult123 http://localhost:8080/api/analytics
```

Analyze a JSON cloud assessment:

```powershell
curl.exe -u consultant:consult123 `
  -H "Content-Type: application/json" `
  -d "@samples/cloud-assessment.json" `
  http://localhost:8080/api/analytics/recommendations
```

Analyze an XML cloud assessment:

```powershell
curl.exe -u consultant:consult123 `
  -H "Content-Type: application/xml" `
  -d "@samples/cloud-assessment.xml" `
  http://localhost:8080/api/analytics/recommendations/xml
```

Create a consulting project:

```powershell
curl.exe -u consultant:consult123 `
  -H "Content-Type: application/json" `
  -d "{\"clientId\":1,\"name\":\"Cloud Landing Zone\",\"description\":\"Design secure hybrid cloud foundations.\",\"deadline\":\"2026-08-15\",\"priority\":1,\"assignedConsultant\":\"Avery Stone\",\"migrationPath\":\"HYBRID_CLOUD\"}" `
  http://localhost:8080/api/projects
```

## Tests

Run all tests:

```powershell
mvn test
```

Or with the local Maven install:

```powershell
tools\apache-maven-3.9.16\bin\mvn.cmd "-Dmaven.repo.local=.m2\repository" test
```

Current coverage includes:

- Cloud recommendation scoring.
- Anonymous web redirect behavior.
- Consultant page access.
- HTTP Basic API access.
- JSON analyzer endpoint behavior.

## Project Structure

```text
src/main/java/com/ibmjob/hybridportal
  config        security and seed data
  controller    MVC pages and REST APIs
  domain        JPA entities and enums
  dto           request/response and form objects
  repository    Spring Data repositories
  service       business logic and analysis engine

src/main/resources
  templates     Thymeleaf UI
  static        CSS and JavaScript

samples         JSON/XML cloud assessment examples
scripts         PowerShell build and run helpers
```

## Troubleshooting

If `mvn` is not recognized, use:

```powershell
tools\apache-maven-3.9.16\bin\mvn.cmd "-Dmaven.repo.local=.m2\repository" package
```

If port `8080` is already in use, run on another port:

```powershell
java -jar target\hybrid-cloud-client-impact-portal-0.1.0-SNAPSHOT.jar --server.port=8081
```

If the JAR cannot be rebuilt because it is locked, stop the running app process first:

```powershell
Get-NetTCPConnection -LocalPort 8080
Stop-Process -Id <OwningProcess>
```

Java 24 may print native access warnings from embedded Tomcat. They do not block the app. Java 17 or 21 is recommended for a quieter portfolio demo.

## Portfolio Story

This project can be presented as a consulting delivery platform: client profiles capture readiness and risk, consultants manage transformation projects, and the analyzer converts enterprise cloud assessment data into practical migration recommendations.

## Suggested Next Enhancements

- Add PostgreSQL profile for deployment.
- Add OpenAPI documentation.
- Add user persistence instead of in-memory demo users.
- Add project milestone editing screens.
- Deploy to Render, Railway, Azure App Service, or IBM Cloud.
