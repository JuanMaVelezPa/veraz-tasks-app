# Frontend - Tasks App

Angular 20 Single Page Application for task management with modern UI components.

## 🚀 Quick Start

### Prerequisites
- Node.js 18 or higher
- npm or yarn package manager

### Installation
```bash
# Install dependencies
npm install

# Run development server
npm start
```

### Verification
- **Application:** http://localhost:4200
- **Default user:** `admin_user` / `Abc123456*`

## 🔧 Configuration

### Environment Variables
```typescript
// src/environments/environment.ts
export const environment = {
  apiUrl: 'http://localhost:3000/api',
  production: false,
  environment: 'development',
  version: '1.0.0'
};
```

### API Configuration
The frontend connects to the Spring Boot backend running on port 3000.

## 📁 Project Structure

```
src/app/
├── core/           # Core functionality
│   ├── guards/     # Route guards
│   ├── interceptors/ # HTTP interceptors
│   └── services/   # Core services
├── features/       # Feature modules
│   ├── auth/       # Authentication
│   │   ├── layout/ # Auth layout
│   │   ├── pages/  # Sign-in/Sign-up pages
│   │   └── services/ # Auth services
│   ├── admin/      # Admin panel
│   └── person/     # Person management
├── shared/         # Shared components
│   ├── components/ # Reusable components
│   ├── layout/     # Main layout
│   ├── pages/      # Shared pages
│   └── services/   # Shared services
└── app.routes.ts   # Main routing
```

## 🔐 Authentication

### Features
- **JWT Token Management:** Automatic token storage and refresh
- **Route Guards:** Protected routes based on authentication status
- **HTTP Interceptors:** Automatic token injection in requests
- **State Management:** Centralized authentication state

### Authentication Flow
1. User navigates to sign-in page
2. Credentials are sent to `/api/auth/sign-in`
3. JWT token is stored in browser storage
4. Token is automatically included in subsequent requests
5. Route guards protect authenticated areas

## 🎨 UI Framework

### Technologies
- **Angular 20:** Latest Angular framework
- **DaisyUI:** Component library for Tailwind CSS
- **Tailwind CSS:** Utility-first CSS framework
- **Responsive Design:** Mobile-first approach

### Key Components
- **Navbar:** Main navigation with user menu
- **Sidebar:** Collapsible sidebar navigation
- **Footer:** Application footer
- **Layout Components:** Consistent page layouts

## 🛠️ Development

```bash
# Install dependencies
npm install

# Start development server
npm start

# Build for production
npm run build

# Run tests
npm test

# Watch mode for development
npm run watch
```

## 📱 Features

### Authentication Module
- **Sign-in Page:** User login with form validation
- **Sign-up Page:** User registration
- **Auth Layout:** Dedicated layout for auth pages
- **Token Management:** Automatic JWT handling

### Admin Module
- **Admin Dashboard:** Administrative interface
- **User Management:** User administration features
- **Protected Routes:** Role-based access control

### Person Module
- **Person Management:** CRUD operations for persons
- **Employee Management:** Employee-specific features
- **Client Management:** Client-specific features

### Shared Components
- **Form Components:** Reusable form elements
- **Modal Components:** Dialog and modal windows
- **Table Components:** Data display tables
- **Utility Components:** Common UI elements

## 🔧 Build Configuration

### Angular Configuration
- **Target:** ES2022
- **Module:** ES2022
- **Strict Mode:** Enabled
- **AOT Compilation:** Enabled for production

### Build Scripts
```json
{
  "start": "ng serve -o",
  "build": "ng build",
  "watch": "ng build --watch --configuration development",
  "test": "ng test"
}
```

## 🚀 Deployment

### Production Build
```bash
# Build for production
npm run build

# The built files will be in dist/frontend/
```

### Environment Configuration
- **Development:** Uses `environment.ts`
- **Production:** Uses `environment.prod.ts` (create if needed)

## 📊 Performance

### Optimizations
- **Lazy Loading:** Feature modules loaded on demand
- **Tree Shaking:** Unused code elimination
- **AOT Compilation:** Ahead-of-time compilation
- **Bundle Optimization:** Optimized bundle sizes

### Monitoring
- **Console Logging:** Development debugging
- **Error Handling:** Global error management
- **Performance Metrics:** Built-in Angular metrics

## 🔗 Integration

### Backend Integration
- **REST API:** Communication with Spring Boot backend
- **JWT Authentication:** Secure API communication
- **Error Handling:** Consistent error management
- **Loading States:** User feedback during API calls

### External Libraries
- **RxJS:** Reactive programming
- **Angular Router:** Client-side routing
- **Angular Forms:** Form handling and validation
