## Technologies and Libraries

- **Java 21**

- **Spring Boot 3**

- **PostgreSQL**

- **Redis**

- **Aspect-Oriented Programming**

- **ApplicationEventPublisher**

- **MapStruct**

- **Lombok**

- **Exception Handling**

- **Swagger**

- **JUnit & Mockito**

- **Jacoco**

## Setup and Running the Application

### Requirements

- **Java 21**
- **Docker** (for PostgreSQL database)
- **Maven** (to build the project)
- **Postman** (optional, for API testing)

### Steps to Run the Application

1. **Build the application**:

   ```bash
   mvn clean install
   ```

2. **Run the PostgreSQL database & Redis with Docker**:

   Navigate to the `docker` folder and start the database with Docker Compose.

   ```bash
   docker-compose up
   ```

   The PostgreSQL database will be available at `localhost:5432` and courier and store test data filled.
   The Redis database will be available at `localhost:6379`.

3. **Run the application**:

   After the database is running, start the Spring Boot application using:

   ```bash
   mvn spring-boot:run
   ```

   The application will run on port `8080`.

# Swagger Documentation

This project uses Swagger for API documentation. 

To access the Swagger UI, run the application and navigate to the following URL in your browser:

```
http://localhost:8080/swagger-ui.html
```

### API Testing with Postman

For convenience, Postman collections have been created for testing the APIs. You can find the collection in the
following path:

```plaintext
   postman-collection/courier-tracking-service.postman_collection.json
```

Import this collection into your Postman to test the various endpoints.


### Example Postman Requests

Here are the key API endpoints:

1. **Save or update courier**:

   POST `/api/v1/couriers`

   Example request body:

   ```json
   {
     "fullName": "Test Kurye 99",
     "phoneNumber": "123456789"
   }
   ```
   
2. **Update courier location**:

   POST `/api/v1/couriers/location`

   Example request body:

   ```json
   {
    "courierId": 1,
    "time": 1734698805,
    "location": {
        "lat": 41.10286503376812,
        "lng": 29.026186385964625
    }
   }
   ```
   
3. **Get courier by id**:
   GET `/api/v1/couriers/{id}`


4. **Get courier by phone number**:
   GET `/api/v1/couriers/phone-number/{phone-number}`


5. **Get all courier with pageable**:
   GET `/api/v1/couriers/all?page=0&size=2`


6. **Get all store with pageable**:
   GET `/api/v1/stores/all?page=0&size=2`

7. **Get store by id**:
   GET `/api/v1/store/{id}`

### Test Coverage

The project includes **unit tests** and **integration tests** with **Jacoco** for code coverage.

To run the tests and generate a coverage report, run:

```bash
mvn test
```

The report will be generated under the `/target/site/jacoco` directory.
