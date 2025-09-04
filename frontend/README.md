# Frontend - Tasks App

Angular 20 Single Page Application for comprehensive task and person management with modern UI components and advanced features.

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
│   │   └── pages/  # Admin pages (persons, users)
│   ├── person/     # Person management
│   │   ├── components/ # Person components
│   │   ├── interfaces/ # Person interfaces
│   │   └── services/   # Person services
│   ├── employee/   # Employee management
│   │   ├── components/ # Employee components
│   │   ├── interfaces/ # Employee interfaces
│   │   └── services/   # Employee services
│   ├── client/     # Client management
│   │   ├── components/ # Client components
│   │   ├── interfaces/ # Client interfaces
│   │   ├── services/   # Client services
│   │   ├── constants/  # Client constants
│   │   └── pipes/      # Client display pipes
│   ├── users/      # User management
│   │   ├── components/ # User components
│   │   ├── interfaces/ # User interfaces
│   │   └── services/   # User services
│   └── profile/    # User profile
│       └── pages/  # Profile pages
├── shared/         # Shared components
│   ├── components/ # Reusable components
│   │   ├── tabs/   # Tab navigation
│   │   ├── icon/   # Icon component
│   │   ├── loading/ # Loading component
│   │   └── feedback-message/ # Feedback messages
│   ├── layout/     # Main layout
│   ├── pages/      # Shared pages
│   ├── services/   # Shared services
│   ├── interfaces/ # Shared interfaces
│   └── constants/  # Shared constants
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

## 🆕 Recent Features & Improvements

### 🏢 Complete Client Management Module
- **Full CRUD Operations:** Create, read, update, and delete clients
- **Advanced Client Types:** Individual, Corporate, and Government clients
- **Client Categories:** Basic, Premium, and Enterprise classifications
- **Client Status Management:** Active, Inactive, and Suspended states
- **Client Rating System:** 1-5 star rating system
- **Business Information:** Company details, payment terms, and methods
- **Cache Optimization:** Intelligent caching with pattern-based invalidation
- **Form Validation:** Comprehensive validation with reactive forms
- **Type Safety:** Strong TypeScript typing with enums and interfaces

### 🎨 Enhanced UI/UX Design
- **Icon Organization:** Clean, large icons (2xl) without background circles
- **Visual Consistency:** Unified icon system across all modules
- **Tab Navigation:** Improved tab system with visual avatars
- **Responsive Design:** Mobile-first approach with adaptive layouts
- **Loading States:** Enhanced loading indicators and feedback
- **Form Validation:** Real-time validation with descriptive error messages

### 🛡️ Centralized Error Handling
- **HttpErrorService:** Centralized HTTP error management
- **Contextual Error Messages:** Specific messages based on operation type
- **Backend Message Priority:** Uses backend messages when available
- **Comprehensive Status Handling:** Covers all HTTP status codes (400, 401, 403, 404, 409, 422, 429, 500, 502, 503, 504, 0)

### 👥 Enhanced User Management
- **User Deletion:** Complete deletion with confirmation modal
- **Cache Invalidation:** Automatic cache clearing after operations
- **Navigation Protection:** Redirects when accessing deleted users
- **Responsive Button Layout:** Optimized for desktop and mobile

### 🔧 Architecture Improvements
- **Service Layer:** Clear separation of concerns
- **API Services:** Dedicated HTTP communication services
- **Error Propagation:** Consistent error handling
- **State Management:** Improved with signals and observables
- **Modular Structure:** Feature-based organization with clear boundaries
- **Type Safety:** Strong TypeScript implementation with enums and interfaces

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
- **Person Management:** Complete CRUD operations for persons
- **Personal Information:** Contact details, addresses, and personal data
- **Form Validation:** Comprehensive validation with reactive forms
- **Cache Management:** Intelligent caching with automatic invalidation

### Employee Module
- **Employee Management:** Full employee lifecycle management
- **Employment Information:** Job details, departments, and positions
- **Professional Data:** Skills, experience, and career information
- **Integration:** Seamless integration with person management

### Client Module
- **Client Management:** Complete client relationship management
- **Client Types:** Individual, Corporate, and Government clients
- **Business Information:** Company details, payment terms, and methods
- **Client Categories:** Basic, Premium, and Enterprise classifications
- **Status Management:** Active, Inactive, and Suspended states
- **Rating System:** 1-5 star client rating system
- **Cache Optimization:** Advanced caching with pattern-based invalidation

### User Module
- **User Management:** Complete user account administration
- **Role Management:** Role-based access control
- **Account Security:** Password management and security features
- **User Profiles:** Comprehensive user profile management

### Shared Components
- **Tab Navigation:** Advanced tab system with visual avatars
- **Icon System:** Comprehensive icon library with FontAwesome integration
- **Form Components:** Reusable form elements with validation
- **Modal Components:** Dialog and modal windows with DaisyUI
- **Table Components:** Data display tables with pagination
- **Loading Components:** Loading indicators and states
- **Feedback Components:** Success, error, and info messages
- **Utility Components:** Common UI elements and helpers

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
- **RxJS:** Reactive programming and observables
- **Angular Router:** Client-side routing with guards
- **Angular Forms:** Reactive forms with validation
- **FontAwesome:** Comprehensive icon library
- **DaisyUI:** Component library for Tailwind CSS
- **Tailwind CSS:** Utility-first CSS framework

## 🏗️ Technical Architecture

### Design Patterns
- **Feature-Based Architecture:** Modular organization by business domains
- **Service Layer Pattern:** Clear separation between API and business logic
- **Repository Pattern:** Data access abstraction
- **Observer Pattern:** Reactive programming with RxJS
- **Component Composition:** Reusable and composable UI components

### State Management
- **Angular Signals:** Modern reactive state management
- **Service-Based State:** Centralized state in services
- **Cache Management:** Intelligent caching with automatic invalidation
- **Form State:** Reactive forms with validation state

### Type Safety
- **TypeScript:** Strong typing throughout the application
- **Interface Definitions:** Comprehensive type definitions
- **Enum Usage:** Type-safe constants and options
- **Generic Types:** Reusable type definitions

### Performance Optimizations
- **Lazy Loading:** Feature modules loaded on demand
- **OnPush Change Detection:** Optimized change detection strategy
- **Tree Shaking:** Unused code elimination
- **Bundle Splitting:** Optimized bundle sizes
- **Cache Strategies:** Intelligent data caching

## 🎯 Key Features

### Business Logic
- **Person Management:** Complete person lifecycle management
- **Employee Management:** Professional employee information
- **Client Management:** Business client relationship management
- **User Management:** System user administration
- **Profile Management:** User profile and preferences

### User Experience
- **Responsive Design:** Mobile-first responsive layouts
- **Intuitive Navigation:** Clear and consistent navigation patterns
- **Visual Feedback:** Loading states, success messages, and error handling
- **Form Validation:** Real-time validation with helpful error messages
- **Accessibility:** WCAG compliant accessibility features

### Developer Experience
- **Type Safety:** Full TypeScript implementation
- **Code Organization:** Clear and maintainable code structure
- **Error Handling:** Comprehensive error management
- **Testing Support:** Unit and integration testing capabilities
- **Documentation:** Comprehensive code documentation
