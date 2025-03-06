# Reactive Spring Boot Application with Resilient Customer Management

A reactive Spring Boot application that provides resilient customer management capabilities with built-in retry mechanisms, circuit breakers, and AWS infrastructure integration.

This application implements a clean architecture pattern to manage customer data through reactive endpoints. It features comprehensive retry strategies with backoff configurations, circuit breaker patterns for fault tolerance, and a containerized deployment model. The application is built using Spring WebFlux for non-blocking operations and includes robust security headers, CORS configuration, and monitoring capabilities through Prometheus and health endpoints.

## Repository Structure
```
.
├── applications/
│   └── app-service/              # Main application service with Spring Boot configuration
├── domain/
│   ├── model/                    # Core domain models and interfaces
│   └── usecase/                  # Business logic and use case implementations
├── infrastructure/
│   ├── driven-adapters/         
│   │   └── rest-consumer/        # External REST API consumer implementation
│   └── entry-points/
│       └── reactive-web/         # Reactive web endpoints and handlers
├── deployment/
│   └── Dockerfile               # Container configuration for deployment
└── docs/
    └── infra.dot               # Infrastructure diagram
```

## Usage Instructions

### Prerequisites
- Java 17 (Amazon Corretto recommended)
- Gradle 8.2.1
- Docker (for containerized deployment)
- Available ports: 8081 (application) and 8080 (for REST consumer)

### Installation

1. Clone the repository:
```bash
git clone <repository-url>
cd <repository-name>
```

2. Build the project:
```bash
./gradlew clean build
```

3. Run the application:
```bash
./gradlew bootRun
```

### Quick Start

1. Start the application with default configuration:
```bash
java -jar applications/app-service/build/libs/NameProject.jar
```

2. Create a new customer using the REST endpoint:
```bash
curl -X POST http://localhost:8081/api/v1/test \
  -H "Content-Type: application/json" \
  -d '{"name": "John", "lastName": "Doe"}'
```

### More Detailed Examples

1. Creating a customer with retry handling:
```java
CreateCustomerUseCase useCase = new CreateCustomerUseCase(customerRepository);
Customer customer = new Customer("John", "Doe");
Mono<CustomerResult> result = useCase.createCustomer(customer);
```

2. Using different retry strategies:
```java
// Fixed delay retry
.retryWhen(buildRetryConfigFixedDelay(customer))

// Exponential backoff with jitter
.retryWhen(buildRetryConfigBackoffAndJitter(customer))

// Exponential backoff without jitter
.retryWhen(buildRetryConfigExponentialBackoffNoJitter(customer))
```

### Troubleshooting

1. Connection Issues
- Problem: Unable to connect to REST consumer
- Error: Connection refused
- Solution: Verify the REST consumer is running on port 8080 and check the URL in application.yaml
```yaml
adapter:
  restconsumer:
    url: "http://localhost:8080"
```

2. Circuit Breaker Issues
- Enable debug logging for circuit breaker:
```yaml
logging:
  level:
    io.github.resilience4j: DEBUG
```
- Monitor circuit breaker state through actuator endpoint:
```bash
curl http://localhost:8081/actuator/health
```

## Data Flow
The application follows a reactive flow for customer management operations.

```ascii
[Client Request] -> [Reactive Web Handler] -> [Use Case Layer]
     -> [Customer Repository] -> [REST Consumer] -> [External Service]
     <- [Response] <- [Retry/Circuit Breaker] <- [Error Handling]
```

Key component interactions:
1. Client requests are received by the reactive web handler
2. Requests are validated and transformed into domain models
3. Use cases implement business logic with retry mechanisms
4. The REST consumer handles external service communication
5. Circuit breakers monitor and manage service health
6. Responses are transformed and returned through the reactive pipeline
7. Error handling and retries are managed at multiple levels

## Infrastructure

![Infrastructure diagram](./docs/infra.svg)

AWS Resources:
- AppService (AWS::ECS::Service): Main application service container
- AppTaskDefinition (AWS::ECS::TaskDefinition): Task definition for the application
- AppContainer (AWS::ECS::ContainerDefinition): Container configuration
- AppLoadBalancer (AWS::ElasticLoadBalancingV2::LoadBalancer): Load balancer for the service
- AppDatabase (AWS::RDS::DBInstance): Database instance
- AppSecurityGroup (AWS::EC2::SecurityGroup): Security group for network access
- AppLogGroup (AWS::Logs::LogGroup): Application logging
- RestConsumer (AWS::ApiGateway::RestApi): REST API interface

## Deployment

1. Prerequisites:
- Docker installed
- AWS credentials configured
- Access to container registry

2. Build the Docker image:
```bash
docker build -t nameproject:latest -f deployment/Dockerfile .
```

3. Run the container:
```bash
docker run -p 8081:8081 \
  -e JAVA_OPTS="-Xmx512m -Xms256m" \
  nameproject:latest
```

4. Monitor the application:
- Health check: http://localhost:8081/actuator/health
- Metrics: http://localhost:8081/actuator/prometheus