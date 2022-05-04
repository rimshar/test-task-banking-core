# Test task - Banking core system

A small core banking solution capable of keeping track of accounts, balances and transactions.
# Requirements

* [JDK 17](https://jdk.java.net/17/) (Unless launching the app in Docker container)
* [Docker](https://docs.docker.com/get-docker/) 
* [Docker Compose](https://docs.docker.com/compose/install/)
# Setup

### Running the application in Docker:

* Create JAR file from application via:
```
./gradlew assemble
```
* Create Docker image via:
```
docker build -t test-task/banking-core .
```
* Start Docker containers via:
```
docker-compose up -d 
```
The application endpoints should be available on `localhost:8080`

### Running the application locally / running tests:

* Start the required database and rabbitmq containers via:
```
docker-compose up -d rabbitmq database
```
* Start application via:
```
./gradlew bootRun
```
* Run the tests via:
```
./gradlew test
```
### Checking test coverage
Test coverage report is available in `build/reports/jacoco/test/html/index.html` 
either after running all tests, or after running `./gradlew jacocoTestReport` 


