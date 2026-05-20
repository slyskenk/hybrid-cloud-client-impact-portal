# Hybrid Cloud Client Impact Portal

Hybrid Cloud Client Impact Portal is a Java Spring Boot portfolio application that simulates an enterprise consulting solution for clients transitioning to hybrid cloud and AI-enabled operations. It demonstrates object-oriented Java, REST APIs, role-based access, data integration with JSON/XML, project tracking, analytics, logging, and mock cloud integration.

## IBM Consulting Alignment

- Java OOP: inheritance, encapsulation, domain services, enums, and DTOs.
- Core CS: sorting, searching, maps, lists, and score-based recommendation logic.
- Web technologies: Spring MVC, Thymeleaf, HTML, CSS, JavaScript, and JQuery.
- REST architecture: `/api/clients`, `/api/projects`, and `/api/analytics`.
- Data integration: JSON and XML cloud assessment import/export.
- Collaboration workflow: role-based login for Admin, Consultant, and Client.
- Troubleshooting: SLF4J logging, centralized exception handling, and service tests.

## Demo Accounts

| Role | Username | Password |
| --- | --- | --- |
| Admin | `admin` | `admin123` |
| Consultant | `consultant` | `consult123` |
| Client | `client` | `client123` |

## Run Locally

Requirements:

- Java 17 or newer
- Maven 3.9 or newer

```bash
mvn spring-boot:run
```

Then open:

```text
http://localhost:8080
```

The local H2 console is available to the Admin role at:

```text
http://localhost:8080/h2-console
```

Use JDBC URL `jdbc:h2:mem:impactportal`.

## API Examples

Analyze a JSON cloud assessment:

```bash
curl -u consultant:consult123 -H "Content-Type: application/json" \
  -d '{"clientName":"Acme Bank","workloads":["payments","risk"],"legacySystemCount":5,"complianceRequired":true,"currentCloudUsagePercent":30,"automationMaturity":4,"integrationComplexity":6,"dataSensitivity":"HIGH"}' \
  http://localhost:8080/api/analytics/recommendations
```

List seeded clients:

```bash
curl -u consultant:consult123 http://localhost:8080/api/clients
```

Create a consulting project:

```bash
curl -u consultant:consult123 -H "Content-Type: application/json" \
  -d '{"clientId":1,"name":"Cloud Landing Zone","description":"Design secure hybrid cloud foundations.","deadline":"2026-08-15","priority":1,"assignedConsultant":"Avery Stone","migrationPath":"HYBRID_CLOUD"}' \
  http://localhost:8080/api/projects
```

## Portfolio Story

This project can be presented as a consulting delivery platform: clients have cloud profiles, consultants manage transformation projects, and the analyzer turns client configuration data into migration recommendations. It is intentionally scoped for a polished portfolio demo while leaving clear room for production-grade extensions.

## Suggested Next Enhancements

- Add PostgreSQL profile for deployment.
- Add API documentation with OpenAPI.
- Expand project CRUD screens.
- Add a GitHub Actions build workflow.
- Deploy to Render, Railway, Azure App Service, or IBM Cloud.
