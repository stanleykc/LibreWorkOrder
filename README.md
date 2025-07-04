# LibreWorkOrder

A companion app for Libre311 that allows the creation and management of work orders from Service Requests.

## Architecture

This application follows a three-tier architecture:

- **Frontend**: Svelte/SvelteKit application with TypeScript
- **Backend**: Micronaut application with Java
- **Database**: MySQL database

## Features

- Work order creation and management
- Integration with Libre311 service requests
- RESTful API for work order operations
- Web-based interface for work order management
- Status tracking and assignment capabilities

## Quick Start

### Using Docker Compose (Recommended)

1. Clone the repository
2. Run the application stack:
   ```bash
   docker-compose up -d
   ```

3. Access the application:
   - Frontend: http://localhost:5173
   - Backend API: http://localhost:8080/api/work-orders
   - Database: localhost:3306

### Manual Setup

#### Database Setup

1. Install MySQL and create the database:
   ```bash
   mysql -u root -p < database/schema.sql
   mysql -u root -p < database/sample_data.sql
   ```

#### Backend Setup

1. Navigate to the backend directory:
   ```bash
   cd backend
   ```

2. Configure the database connection in `src/main/resources/application.yml`

3. Run the backend:
   ```bash
   ./gradlew run
   ```

#### Frontend Setup

1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Start the development server:
   ```bash
   npm run dev
   ```

## API Endpoints

### Work Orders

- `GET /api/work-orders` - List all work orders
- `POST /api/work-orders` - Create a new work order
- `GET /api/work-orders/{id}` - Get work order by ID
- `PUT /api/work-orders/{id}` - Update work order
- `DELETE /api/work-orders/{id}` - Delete work order
- `GET /api/work-orders/libre311/{serviceRequestId}` - Get work order by Libre311 service request ID
- `GET /api/work-orders/status/{status}` - Get work orders by status
- `GET /api/work-orders/assigned/{assignedTo}` - Get work orders by assignee
- `PUT /api/work-orders/{id}/assign` - Assign work order
- `PUT /api/work-orders/{id}/status` - Update work order status

### Example: Creating a Work Order

```bash
curl -X POST http://localhost:8080/api/work-orders \
  -H "Content-Type: application/json" \
  -d '{
    "libre311ServiceRequestId": "SR-2024-006",
    "title": "Fix broken streetlight",
    "description": "Streetlight is out on Main Street",
    "priority": "HIGH"
  }'
```

## Database Schema

The application uses a single `work_orders` table with the following fields:

- `id` - Primary key
- `libre311_service_request_id` - Reference to Libre311 service request
- `title` - Work order title
- `description` - Detailed description
- `status` - Current status (PENDING, ASSIGNED, IN_PROGRESS, COMPLETED, CANCELLED)
- `assigned_to` - Email/username of assigned person
- `priority` - Priority level (LOW, MEDIUM, HIGH, URGENT)
- `created_at` - Creation timestamp
- `updated_at` - Last update timestamp

## Integration with Libre311

This application is designed to receive work order creation requests from Libre311 through the REST API. Libre311 can:

1. Create work orders by posting to `/api/work-orders`
2. Check work order status by querying `/api/work-orders/libre311/{serviceRequestId}`
3. Receive status updates through webhook notifications (to be implemented)

## Development

### Technology Stack

- **Frontend**: 
  - Svelte 5
  - SvelteKit
  - TypeScript
  - Tailwind CSS
  - Vite

- **Backend**:
  - Micronaut 4.2.1
  - Java 17
  - Hibernate/JPA
  - MySQL Connector
  - Gradle

- **Database**:
  - MySQL 8.0

### Project Structure

```
LibreWorkOrder/
├── frontend/          # Svelte frontend application
│   ├── src/
│   │   ├── lib/
│   │   │   ├── components/
│   │   │   ├── api.ts
│   │   │   └── types.ts
│   │   └── routes/
│   └── package.json
├── backend/           # Micronaut backend application
│   ├── src/main/java/com/libre311/workorder/
│   │   ├── controller/
│   │   ├── service/
│   │   ├── repository/
│   │   ├── entity/
│   │   └── dto/
│   └── build.gradle
├── database/          # Database schema and sample data
│   ├── schema.sql
│   └── sample_data.sql
└── docker-compose.yml # Development environment
```

## License

This project is licensed under the Apache License 2.0.