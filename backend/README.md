# QuickPe Backend - Spring Boot Application

## Backend Setup Instructions

### Prerequisites
- Java 11 or higher
- Maven 3.6+
- MySQL 8.0+
- IDE: IntelliJ IDEA or VS Code

### Step 1: Database Setup

1. **Install MySQL** (if not already installed)
   ```bash
   # On Ubuntu/Debian
   sudo apt-get install mysql-server
   
   # On macOS (with Homebrew)
   brew install mysql
   ```

2. **Start MySQL Service**
   ```bash
   sudo service mysql start  # Linux
   brew services start mysql # macOS
   ```

3. **Create Database and Tables**
   ```bash
   mysql -u root -p < database/schema.sql
   ```
   Or manually run the SQL script in MySQL Workbench.

4. **Verify Database Creation**
   ```bash
   mysql -u root -p
   # Inside MySQL CLI
   USE quickpe_db;
   SHOW TABLES;
   ```

### Step 2: Configure Application

1. **Update application.properties**
   - Open `src/main/resources/application.properties`
   - Update MySQL credentials:
     ```properties
     spring.datasource.username=your_mysql_username
     spring.datasource.password=your_mysql_password
     ```

### Step 3: Build and Run

1. **Build the Application**
   ```bash
   mvn clean install
   ```

2. **Run the Application**
   ```bash
   mvn spring-boot:run
   ```
   Or
   ```bash
   java -jar target/quickpe-backend-1.0.0.jar
   ```

3. **Verify Backend is Running**
   - Open browser and go to: `http://localhost:8080/api/auth/register`
   - You should see the API endpoint responding

## API Endpoints

### Authentication APIs
- **POST** `/api/auth/register` - Register new user
- **POST** `/api/auth/login` - Login user

### Wallet APIs
- **GET** `/api/wallet/balance` - Get wallet balance
- **GET** `/api/wallet/details` - Get wallet details
- **POST** `/api/wallet/add-money` - Add money to wallet

### Transaction APIs
- **POST** `/api/transactions/send-money` - Send money
- **POST** `/api/transactions/request-money` - Request money
- **GET** `/api/transactions/history` - Get transaction history
- **GET** `/api/transactions/{refId}` - Get transaction by reference ID

## Project Structure

```
backend/
├── src/
│   ├── main/
│   │   ├── java/com/quickpe/
│   │   │   ├── QuickPeApplication.java      # Main entry point
│   │   │   ├── controller/                  # REST controllers
│   │   │   ├── service/                     # Business logic
│   │   │   ├── repository/                  # Data access layer
│   │   │   ├── entity/                      # JPA entities
│   │   │   ├── dto/                         # Data transfer objects
│   │   │   ├── security/                    # JWT security
│   │   │   └── config/                      # Configuration classes
│   │   └── resources/
│   │       └── application.properties       # Application configuration
│   └── test/
│       └── java/com/quickpe/
└── pom.xml                                  # Maven dependencies
```

## Troubleshooting

### 1. MySQL Connection Error
```
Error: "Access denied for user 'root'@'localhost'"
```
**Solution:**
- Check MySQL is running: `sudo service mysql status`
- Verify credentials in `application.properties`
- Reset MySQL password if needed

### 2. Port Already in Use
```
Error: "Address already in use: bind"
```
**Solution:**
```bash
# Kill process on port 8080
lsof -ti:8080 | xargs kill -9
```

### 3. Database Not Found
```
Error: "Unknown database 'quickpe_db'"
```
**Solution:**
```bash
mysql -u root -p < database/schema.sql
```

## Environment Variables (Optional)

You can also set environment variables instead of properties:
```bash
export SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/quickpe_db
export SPRING_DATASOURCE_USERNAME=root
export SPRING_DATASOURCE_PASSWORD=root
export APP_JWT_SECRET=your_secret_key
```

## Testing APIs

### Using Postman
1. Import the Postman collection
2. Set up variables for `base_url`, `token`, etc.
3. Test endpoints

### Using cURL
```bash
# Register User
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123",
    "mobileNumber": "9876543210",
    "firstName": "John",
    "lastName": "Doe"
  }'

# Login User
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'
```

## Deployment

### Deploy to Heroku
1. Create Heroku account and install CLI
2. Create `Procfile`:
   ```
   web: java -Dserver.port=$PORT $JAVA_OPTS -jar target/quickpe-backend-1.0.0.jar
   ```
3. Deploy:
   ```bash
   heroku create your-app-name
   git push heroku main
   ```

## Support

For issues or questions, please create an issue on GitHub.
