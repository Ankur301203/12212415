# 1. Logging Middleware Service – `logging` folder

##  Logging Middleware (Spring Boot)

This Spring Boot service handles centralized logging by forwarding log entries to an external logging evaluation service using an authenticated HTTP request.

### Features

* Token-based authentication to fetch bearer token from an auth endpoint
* Logs sent to external service using REST API
* Logs include stack, level, package name, and message

### Project Structure

```
logging/
├── controller/
│   └── TestController.java
├── model/
│   └── LogRequest.java
├── middleware/
│   └── LoggingService.java
├── auth/
│   └── TokenService.java
└── application.properties
```

### Authentication Flow

* On startup, the `TokenService` fetches a bearer token from:

  ```
  POST http://20.244.56.144/evaluation-service/auth
  ```
* The token is then used for all subsequent `POST /logs` calls to:

  ```
  http://20.244.56.144/evaluation-service/logs
  ```

### Test Endpoint

You can hit this to trigger a sample log:

```
GET http://localhost:8080/test
```

### Configuration

Edit your `application.properties`:

```properties
server.port=8080
log.api.url=http://20.244.56.144/evaluation-service/logs
```

### ⚒Technologies Used

* Java 17+
* Spring Boot 3.x
* RESTTemplate

---

# 2. URL Shortener Service – `url-shortener` folder

##  URL Shortener Microservice (Spring Boot + MySQL)

This microservice provides functionality to shorten URLs, track usage statistics, and handle expiration of shortened links. It also integrates with the centralized logging middleware.

### Features

* Create short URLs with optional custom alias and expiry
* Auto-generates short codes if not provided
* Redirects to the original URL
* Tracks clicks with metadata (user-agent)
* Fetch stats for shortened URLs
* Integrates with internal logging middleware

---

### Configuration

#### 1. `application.properties`

```properties
server.port=8080

# MySQL config
spring.datasource.url=jdbc:mysql://localhost:3306/url_shortener
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update

# Logging Service
log.api.url=http://20.244.56.144/evaluation-service/logs
```

---

### Project Structure

```
url-shortener/
├── controller/
│   └── ShortUrlController.java
├── service/
│   └── ShortUrlService.java
├── model/
│   └── ShortUrl.java
├── repository/
│   └── ShortUrlRepository.java
├── logging/
│   ├── LoggingService.java
│   └── TokenService.java
└── UrlShortenerApplication.java
```

---

### ⚒API Endpoints

#### Create short URL

```
POST /shorturls
```

**Request Body:**

```json
{
  "url": "https://example.com/ankur-test",
  "customAlias": "ankur123",
  "validityInSeconds": 3600
}
```

**Response:**

```json
{
  "shortLink": "http://localhost:8080/shorturls/ankur123",
  "expiry": "2025-07-14T06:51:10.442590600Z"
}
```

---

#### Redirect to Original URL

```
GET /shorturls/{shortcode}
```

> Redirects with `302 Found` if valid, else `410 Gone`.

---

#### View URL Stats

```
GET /shorturls/{shortcode}/stats
```

**Response:**

```json
{
  "originalUrl": "https://example.com/ankur-test",
  "createdAt": "...",
  "expiry": "...",
  "clickCount": 3,
  "clicks": ["Clicked by: PostmanRuntime/7.36.1", ...]
}
```

---

### ⚒Technologies Used

* Java 17+
* Spring Boot 3.x
* Spring Data JPA
* MySQL
* REST APIs
* Logging via internal middleware
