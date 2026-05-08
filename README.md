# QuickPe - Digital Payment Web Application

A full-stack digital payment web application similar to PhonePe and Google Pay, built with React.js, Spring Boot, and MySQL.

## 🚀 Features

### Core Features
- ✅ User Registration and Login with JWT Authentication
- ✅ Add/Link Bank Account
- ✅ Wallet System (add money, check balance)
- ✅ Send Money via Mobile Number or UPI ID
- ✅ Transaction History
- ✅ QR Code Payment System
- ✅ Request Money
- ✅ Profile Management

### Advanced UI Features
- ✅ Dark Mode Toggle (Light/Dark theme switch)
- ✅ Fake Debit Card UI (visual card display)
- ✅ Fully Mobile Responsive Design
- ✅ Modern Fintech UI Design

## 💻 Tech Stack

### Frontend
- React.js (Functional Components with Hooks)
- HTML5 & CSS3 / Tailwind CSS
- Context API for State Management
- Axios for API Calls
- QR Code Library

### Backend
- Java Spring Boot
- Spring Security with JWT
- JPA/Hibernate
- REST APIs

### Database
- MySQL 8.0+

## 📁 Project Structure

```
Quick-Pe/
├── frontend/                 # React Frontend
│   ├── src/
│   │   ├── components/      # Reusable Components
│   │   ├── pages/           # Page Components
│   │   ├── context/         # Context API
│   │   ├── services/        # API Services
│   │   ├── styles/          # Global Styles
│   │   ├── utils/           # Utility Functions
│   │   ├── App.jsx
│   │   └── index.js
│   ├── public/
│   ├── package.json
│   └── README.md
│
└── backend/                  # Spring Boot Backend
    ├── src/
    │   ├── main/java/com/quickpe/
    │   │   ├── controller/  # REST Controllers
    │   │   ├── service/     # Business Logic
    │   │   ├── repository/  # Data Access
    │   │   ├── entity/      # JPA Entities
    │   │   ├── dto/         # Data Transfer Objects
    │   │   ├── security/    # JWT Security
    │   │   └── QuickPeApplication.java
    │   └── resources/
    │       └── application.properties
    ├── pom.xml
    └── README.md
```

## 🔧 Setup Instructions

### Prerequisites
- Node.js (v14+) and npm
- Java 11+
- MySQL 8.0+
- Git

### Backend Setup

1. **Clone and navigate to backend:**
   ```bash
   cd backend
   ```

2. **Create MySQL Database:**
   ```sql
   CREATE DATABASE quickpe_db;
   ```

3. **Configure Database:**
   - Update `src/main/resources/application.properties` with your MySQL credentials

4. **Build and Run:**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```
   Backend runs on: `http://localhost:8080`

### Frontend Setup

1. **Clone and navigate to frontend:**
   ```bash
   cd frontend
   ```

2. **Install Dependencies:**
   ```bash
   npm install
   ```

3. **Start Development Server:**
   ```bash
   npm start
   ```
   Frontend runs on: `http://localhost:3000`

## 📚 API Documentation

### Authentication
- **POST** `/api/auth/register` - Register new user
- **POST** `/api/auth/login` - Login user

### Wallet
- **GET** `/api/wallet/balance` - Get wallet balance
- **POST** `/api/wallet/add-money` - Add money to wallet
- **GET** `/api/wallet/transactions` - Get transaction history

### Transactions
- **POST** `/api/transactions/send-money` - Send money
- **POST** `/api/transactions/request-money` - Request money
- **GET** `/api/transactions/history` - Get all transactions

### Bank Account
- **POST** `/api/bank-account/link` - Link bank account
- **GET** `/api/bank-account/list` - List linked accounts

### Profile
- **GET** `/api/profile` - Get user profile
- **PUT** `/api/profile/update` - Update profile

## 🎨 UI Pages

1. **Login Page** - User authentication
2. **Register Page** - New user registration
3. **Dashboard** - Home page with balance and quick actions
4. **Send Money** - Transfer funds via mobile or UPI
5. **Card UI** - Virtual debit card display
6. **Transactions** - Transaction history
7. **Add Money** - Wallet top-up
8. **Profile** - User profile management

## 🔐 Security Features

- JWT-based authentication
- Password encryption (BCrypt)
- Secure API endpoints
- CORS configuration
- Input validation

## 📱 Responsive Design

- Mobile-first approach
- Flexbox and Grid layouts
- Touch-friendly UI
- Tablet and Desktop optimized

## 🌙 Dark Mode

- Toggle switch in header
- Context API for state management
- Persistent theme preference
- Smooth transitions

## 📝 Contributing

1. Create a feature branch (`git checkout -b feature/AmazingFeature`)
2. Commit changes (`git commit -m 'Add AmazingFeature'`)
3. Push to branch (`git push origin feature/AmazingFeature`)
4. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see LICENSE file for details.

## 👨‍💻 Author

**Sandeep8229**

---

**Last Updated:** 2026-05-08
