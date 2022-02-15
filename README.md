# Dummy Bank

## Implementation

This application is implemented using Java 11, Spring-Boot, and Maven. It connects to a Postgres database that is
exposed on port 5432 running in a Docker container. Flyway is used for database migrations and populates the database with
two accounts upon startup.

## How To Run

### Prerequisites

* Docker
* Java 11

### Database

```
docker-compose up -d
```

### Application

```
./mvnw spring-boot:run
```

Note: The application requires the Postgres database to be running first

## Tests

### Unit Tests

```
./mvnw test
```

### Integration Tests

```
./mvnw failsafe:integration-test
```

Note: These tests require the Postgres database to be running first

## API

### Get Account

#### Request

`GET /account/{accountId}`

```
curl -i -H 'Accept: application/json' http://localhost:8080/account/1
```

#### Response

```
HTTP/1.1 200 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Tue, 15 Feb 2022 18:34:42 GMT

{"id":1,"balance":3505,"transactions":[{"payeeId":1,"payerId":2,"amount":1,"timestamp":"2022-02-15T17:55:17.345195"}]}
```

### Make Transaction

#### Request

`POST /transaction`

```
curl -i -H "Content-Type: application/json" -d '{"to": 1, "from": 2, "amount":500}' localhost:8080/transaction
```

#### Response

```
HTTP/1.1 200 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Tue, 15 Feb 2022 18:38:20 GMT

{"payeeId":1,"payerId":2,"amount":500,"timestamp":"2022-02-15T18:38:55.925468"}
```
