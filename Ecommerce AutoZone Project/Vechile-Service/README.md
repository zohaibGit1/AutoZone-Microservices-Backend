# Vehicle Service API

A Spring Boot application for managing vehicle service operations including customer registration, vehicle management, service visits, complaints, and invoicing.

## Table of Contents

- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [API Endpoints](#api-endpoints)
- [Data Models](#data-models)
- [Enums](#enums)
- [Setup Instructions](#setup-instructions)

---

## Tech Stack

- **Java 17+**
- **Spring Boot 4.1.1**
- **Spring Data JPA**
- **H2/MySQL Database**
- **Lombok**
- **Jakarta Validation**

---

## Project Structure

```
Vechile-Service/
├── src/main/java/com/Vechile_Service/
│   ├── controller/          # REST Controllers
│   ├── service/            # Business Logic
│   ├── repo/               # JPA Repositories
│   ├── entity/             # Database Entities
│   ├── dto/                # Data Transfer Objects
│   ├── constant/           # Enums
│   ├── exception/          # Custom Exceptions
│   └── mapper/             # Entity-DTO Mappers
└── src/main/resources/
    ├── application.yaml    # Main configuration
    └── application-dev.yml # Dev environment config
```

---

## API Endpoints

Base URL: `http://localhost:8081/api/v1`

### Customer Management

#### 1. Register Customer

**Endpoint:** `POST /customers/register-customer`

**Request Body:**
```json
{
  "customerName": "John Doe",
  "customerEmail": "john.doe@example.com",
  "customerPhone": "9876543210"
}
```

**Response (201 Created):**
```json
{
  "customerId": 1,
  "customerName": "John Doe",
  "customerEmail": "john.doe@example.com",
  "customerPhone": "9876543210",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00",
  "vehicles": []
}
```

#### 2. Get Customer History

**Endpoint:** `GET /customers/history?search={phoneOrEmail}`

**Response (200 OK):**
```json
{
  "customerId": 1,
  "customerName": "John Doe",
  "customerEmail": "john.doe@example.com",
  "customerPhone": "9876543210",
  "vehicles": [
    {
      "vehicleId": 1,
      "vehicleNumber": "MH12AB1234",
      "vehicleName": "Honda City",
      "vehicleModel": "ZX",
      "vehicleType": "SEDAN",
      "visits": [
        {
          "visitId": 1,
          "visitDate": "2024-01-15",
          "currentKm": 25000,
          "complaints": [
            {
              "complaintId": 1,
              "complaintDescription": "Engine noise"
            }
          ]
        }
      ]
    }
  ]
}
```

#### 3. Update Customer

**Endpoint:** `PATCH /customers/update-customer/{customerId}`

**Request Body:**
```json
{
  "customerName": "John Smith",
  "customerEmail": "john.smith@example.com",
  "customerPhone": "9876543211"
}
```

**Response (200 OK):**
```json
{
  "customerId": 1,
  "customerName": "John Smith",
  "customerEmail": "john.smith@example.com",
  "customerPhone": "9876543211",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-16T14:20:00",
  "vehicles": []
}
```

---

### Vehicle Management

#### 4. Register Vehicle

**Endpoint:** `POST /vehicle/register-vehicle`

**Request Body:**
```json
{
  "vehicleNumber": "MH12AB1234",
  "vehicleName": "Honda City",
  "vehicleModel": "ZX",
  "vehicleType": "SEDAN",
  "customerId": 1
}
```

**Response (201 Created):**
```json
{
  "vehicleId": 1,
  "vehicleNumber": "MH12AB1234",
  "vehicleName": "Honda City",
  "vehicleModel": "ZX",
  "vehicleType": "SEDAN",
  "customerId": 1
}
```

---

### Vehicle Visit Management

#### 5. Register Vehicle Visit

**Endpoint:** `POST /vehicle-visit`

**Request Body:**
```json
{
  "currentKm": 25000,
  "vehicleId": 1
}
```

**Response (201 Created):**
```json
{
  "visitId": 1,
  "visitDate": "2024-01-15",
  "currentKm": 25000,
  "checkInTime": "2024-01-15T10:30:00",
  "vehicleId": 1
}
```

---

### Complaint Management

#### 6. Register Complaint

**Endpoint:** `POST /complaint/register/complaint`

**Request Body:**
```json
{
  "complaintDescription": "Engine making unusual noise at high speeds",
  "vehicleVisitId": 1
}
```

**Response (201 Created):**
```json
{
  "complaintId": 1,
  "complaintDescription": "Engine making unusual noise at high speeds",
  "vehicleVisitId": 1
}
```

---

### Invoice Management

#### 7. Calculate Invoice

**Endpoint:** `POST /invoices/calculate`

**Request Body:**
```json
{
  "vehicleVisitId": 1,
  "items": [
    {
      "description": "Oil Change",
      "itemType": "SERVICE",
      "quantity": 1,
      "unitPrice": 1500.00
    },
    {
      "description": "Air Filter",
      "itemType": "PART",
      "quantity": 1,
      "unitPrice": 800.00
    },
    {
      "description": "Labor Charges",
      "itemType": "LABOUR",
      "quantity": 2,
      "unitPrice": 500.00
    }
  ],
  "discount": 200.00,
  "taxPercentage": 18.00
}
```

**Response (200 OK):**
```json
{
  "subtotal": 3300.00,
  "discount": 200.00,
  "taxableAmount": 3100.00,
  "taxPercentage": 18.00,
  "taxAmount": 558.00,
  "grandTotal": 3658.00,
  "items": [
    {
      "description": "Oil Change",
      "itemType": "SERVICE",
      "quantity": 1,
      "unitPrice": 1500.00,
      "totalPrice": 1500.00
    },
    {
      "description": "Air Filter",
      "itemType": "PART",
      "quantity": 1,
      "unitPrice": 800.00,
      "totalPrice": 800.00
    },
    {
      "description": "Labor Charges",
      "itemType": "LABOUR",
      "quantity": 2,
      "unitPrice": 500.00,
      "totalPrice": 1000.00
    }
  ]
}
```

#### 8. Create Invoice

**Endpoint:** `POST /invoices`

**Request Body:** Same as Calculate Invoice

**Response (201 Created):**
```json
{
  "invoiceId": 1,
  "invoiceNumber": "INV-2024-001",
  "vehicleVisitId": 1,
  "createdAt": "2024-01-15T14:30:00",
  "subtotal": 3300.00,
  "discount": 200.00,
  "taxPercentage": 18.00,
  "taxAmount": 558.00,
  "grandTotal": 3658.00,
  "paymentStatus": "PENDING",
  "paymentMethod": null,
  "items": [
    {
      "description": "Oil Change",
      "itemType": "SERVICE",
      "quantity": 1,
      "unitPrice": 1500.00,
      "totalPrice": 1500.00
    },
    {
      "description": "Air Filter",
      "itemType": "PART",
      "quantity": 1,
      "unitPrice": 800.00,
      "totalPrice": 800.00
    },
    {
      "description": "Labor Charges",
      "itemType": "LABOUR",
      "quantity": 2,
      "unitPrice": 500.00,
      "totalPrice": 1000.00
    }
  ]
}
```

#### 9. Get Invoice

**Endpoint:** `GET /invoices/{invoiceId}`

**Response (200 OK):** Same as Create Invoice response

#### 10. Update Payment

**Endpoint:** `PATCH /invoices/{invoiceId}/payment`

**Request Body:**
```json
{
  "paymentMethod": "UPI"
}
```

**Response (200 OK):**
```json
{
  "invoiceId": 1,
  "invoiceNumber": "INV-2024-001",
  "vehicleVisitId": 1,
  "createdAt": "2024-01-15T14:30:00",
  "subtotal": 3300.00,
  "discount": 200.00,
  "taxPercentage": 18.00,
  "taxAmount": 558.00,
  "grandTotal": 3658.00,
  "paymentStatus": "SUCCESSFUL",
  "paymentMethod": "UPI",
  "items": ["..."]
}
```

#### 11. Generate Invoice PDF

**Endpoint:** `GET /invoices/{invoiceId}/pdf`

**Response:** PDF file download (Content-Type: application/pdf)

---

## Data Models

### CustomerRequestDto
```json
{
  "customerName": "string (2-100 chars)",
  "customerEmail": "string (valid email)",
  "customerPhone": "string (10-digit Indian number starting with 6-9)"
}
```

### CustomerResponseDto
```json
{
  "customerId": "Long",
  "customerName": "string",
  "customerEmail": "string",
  "customerPhone": "string",
  "createdAt": "LocalDateTime",
  "updatedAt": "LocalDateTime",
  "vehicles": "List<VehicleResponseDto>"
}
```

### VehicleRequestDto
```json
{
  "vehicleNumber": "string (format: XX00XX0000)",
  "vehicleName": "string (2-50 chars)",
  "vehicleModel": "string (2-50 chars)",
  "vehicleType": "VehicleType enum",
  "customerId": "Long (positive)"
}
```

### VehicleResponseDto
```json
{
  "vehicleId": "Long",
  "vehicleNumber": "string",
  "vehicleName": "string",
  "vehicleModel": "string",
  "vehicleType": "VehicleType enum",
  "customerId": "Long"
}
```

### VehicleVisitRequestDto
```json
{
  "currentKm": "Integer (>= 0)",
  "vehicleId": "Long (positive)"
}
```

### VehicleVisitResponse
```json
{
  "visitId": "Long",
  "visitDate": "LocalDate",
  "currentKm": "Integer",
  "checkInTime": "LocalDateTime",
  "vehicleId": "Long"
}
```

### ComplaintRequestDto
```json
{
  "complaintDescription": "string",
  "vehicleVisitId": "Long (positive)"
}
```

### ComplaintResponseDto
```json
{
  "complaintId": "Long",
  "complaintDescription": "string",
  "vehicleVisitId": "Long"
}
```

### InvoiceRequestDto
```json
{
  "vehicleVisitId": "Long (positive)",
  "items": "List<InvoiceItemRequestDto>",
  "discount": "BigDecimal (>= 0)",
  "taxPercentage": "BigDecimal (>= 0)"
}
```

### InvoiceItemRequestDto
```json
{
  "description": "string (max 255 chars)",
  "itemType": "ItemType enum",
  "quantity": "Integer (> 0)",
  "unitPrice": "BigDecimal (> 0)"
}
```

### InvoiceResponseDto
```json
{
  "invoiceId": "Long",
  "invoiceNumber": "string",
  "vehicleVisitId": "Long",
  "createdAt": "LocalDateTime",
  "subtotal": "BigDecimal",
  "discount": "BigDecimal",
  "taxPercentage": "BigDecimal",
  "taxAmount": "BigDecimal",
  "grandTotal": "BigDecimal",
  "paymentStatus": "PaymentStatus enum",
  "paymentMethod": "PaymentMethod enum",
  "items": "List<InvoiceItemResponseDto>"
}
```

### InvoiceCalculationResponseDto
```json
{
  "subtotal": "BigDecimal",
  "discount": "BigDecimal",
  "taxableAmount": "BigDecimal",
  "taxPercentage": "BigDecimal",
  "taxAmount": "BigDecimal",
  "grandTotal": "BigDecimal",
  "items": "List<InvoiceItemResponseDto>"
}
```

### PaymentRequestDto
```json
{
  "paymentMethod": "PaymentMethod enum"
}
```

---

## Enums

### VehicleType
- `CAR`
- `JEEP`
- `SUV`
- `SEDAN`
- `BIKE`

### ItemType
- `SERVICE`
- `PART`
- `LABOUR`

### PaymentMethod
- `CASH`
- `UPI`
- `ONLINE`
- `NET_BANKING`

### PaymentStatus
- `SUCCESSFUL`
- `FAILED`
- `PENDING`

---

## Setup Instructions

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- MySQL/H2 Database

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd Vechile-Service
   ```

2. **Configure Database**
   
   Update `application-dev.yml` with your database credentials:
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/vehicle_service
       username: your_username
       password: your_password
   ```

3. **Build the project**
   ```bash
   ./mvnw clean install
   ```

4. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```

   Or use the Maven wrapper:
   ```bash
   ./mvnw clean package
   java -jar target/Vechile-Service-0.0.1-SNAPSHOT.jar
   ```

5. **Access the API**
   
   The application will start on port `8081`.
   
   Base URL: `http://localhost:8081/api/v1`

### Health Check

```bash
curl http://localhost:8081/actuator/health
```

---

## Error Handling

The API uses standard HTTP status codes:

- `200 OK` - Request successful
- `201 Created` - Resource created successfully
- `400 Bad Request` - Invalid request data
- `404 Not Found` - Resource not found
- `409 Conflict` - Duplicate resource
- `500 Internal Server Error` - Server error

Error Response Format:
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation error details",
  "path": "/api/v1/customers/register-customer"
}
```

---

## License

This project is proprietary software.
