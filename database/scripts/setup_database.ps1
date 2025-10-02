# Script to set up the Tasks Application database, user and extensions.
# This script creates the database, user, and enables required extensions.

# Database Configuration
$DB_HOST = "localhost"
$DB_PORT = "5432"
$DB_NAME = "tasks_app_db"
$DB_SUPERUSER = "postgres"
$DB_SUPERUSER_PASSWORD = "jmvelez" # Use with extreme caution in production. Consider env vars or secrets management.

# New user for the application
$APP_USER = "tasks_app_user"
$APP_PASSWORD = "tasks_app_user" # Use with extreme caution in production. Consider env vars or secrets management.

# psql -h localhost -p 5432 tasks_app_db -U postgres
# psql -h localhost -p 5432 tasks_app_db -U tasks_app_user


# Path to psql.exe (adjust if your PostgreSQL installation path is different)
# Typical path: "C:\Program Files\PostgreSQL\17\bin\psql.exe"
$PSQL_PATH = "C:\Program Files\PostgreSQL\17\bin\psql.exe" # Make sure this matches your PostgreSQL version and path

# --- STEP 1: CREATE DATABASE (IF IT DOES NOT EXIST) ---
Write-Host "Creating database '$DB_NAME'..."
$env:PGPASSWORD = $DB_SUPERUSER_PASSWORD
& $PSQL_PATH -h "$DB_HOST" -p "$DB_PORT" -U "$DB_SUPERUSER" -d postgres -c "CREATE DATABASE `"$DB_NAME`";" 2>$null

if ($LASTEXITCODE -ne 0) {
    Write-Host "Warning: The database '$DB_NAME' might already exist or there was an error creating it."
} else {
    Write-Host "Database '$DB_NAME' created successfully."
}
Remove-Item Env:PGPASSWORD

# --- STEP 2: CREATE THE APPLICATION USER (IF IT DOES NOT EXIST) ---
Write-Host "Creating the application user '$APP_USER'..."
$env:PGPASSWORD = $DB_SUPERUSER_PASSWORD
& $PSQL_PATH -h "$DB_HOST" -p "$DB_PORT" -U "$DB_SUPERUSER" -d postgres -c "CREATE USER `"$APP_USER`" WITH PASSWORD `'$APP_PASSWORD`';" 2>$null

if ($LASTEXITCODE -ne 0) {
    Write-Host "Warning: The user '$APP_USER' might already exist or there was an error creating it."
} else {
    Write-Host "User '$APP_USER' created successfully."
}
Remove-Item Env:PGPASSWORD

# --- STEP 3: GRANT ALL PERMISSIONS TO THE APPLICATION USER ---
Write-Host "Granting all permissions to '$APP_USER' on database '$DB_NAME'..."

# 3.1: Grant all privileges on the database to the app user
$env:PGPASSWORD = $DB_SUPERUSER_PASSWORD
& $PSQL_PATH -h "$DB_HOST" -p "$DB_PORT" -U "$DB_SUPERUSER" -d "$DB_NAME" -c "GRANT ALL PRIVILEGES ON DATABASE `"$DB_NAME`" TO `"$APP_USER`";"
Remove-Item Env:PGPASSWORD

# 3.2: Grant all privileges on the 'public' schema to the app user
$env:PGPASSWORD = $DB_SUPERUSER_PASSWORD
& $PSQL_PATH -h "$DB_HOST" -p "$DB_PORT" -U "$DB_SUPERUSER" -d "$DB_NAME" -c "GRANT ALL ON SCHEMA public TO `"$APP_USER`";"
Remove-Item Env:PGPASSWORD

# 3.3: Grant all privileges on all existing tables in public schema
$env:PGPASSWORD = $DB_SUPERUSER_PASSWORD
& $PSQL_PATH -h "$DB_HOST" -p "$DB_PORT" -U "$DB_SUPERUSER" -d "$DB_NAME" -c "GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO `"$APP_USER`";"
Remove-Item Env:PGPASSWORD

# 3.4: Grant all privileges on all existing sequences in public schema
$env:PGPASSWORD = $DB_SUPERUSER_PASSWORD
& $PSQL_PATH -h "$DB_HOST" -p "$DB_PORT" -U "$DB_SUPERUSER" -d "$DB_NAME" -c "GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO `"$APP_USER`";"
Remove-Item Env:PGPASSWORD

Write-Host "Permissions for '$APP_USER' granted successfully on '$DB_NAME'."

# --- STEP 4: ENABLE THE 'pgcrypto' EXTENSION ---
Write-Host "Enabling the 'pgcrypto' extension..."
$env:PGPASSWORD = $DB_SUPERUSER_PASSWORD
& $PSQL_PATH -h "$DB_HOST" -p "$DB_PORT" -U "$DB_SUPERUSER" -d "$DB_NAME" -c "CREATE EXTENSION IF NOT EXISTS pgcrypto;"
Remove-Item Env:PGPASSWORD

if ($LASTEXITCODE -ne 0) {
    Write-Host "Error enabling the 'pgcrypto' extension. Aborting."
    exit 1
} else {
    Write-Host "'pgcrypto' extension enabled successfully."
}

Write-Host ""
Write-Host "====================================================="
Write-Host "DATABASE SETUP COMPLETED SUCCESSFULLY!"
Write-Host "====================================================="
Write-Host "Database: $DB_NAME"
Write-Host "User: $APP_USER"
Write-Host "Extensions: pgcrypto"
Write-Host ""
Write-Host "Next steps:"
Write-Host "1. Run migrations: psql -h $DB_HOST -p $DB_PORT $DB_NAME -U $APP_USER -f migrate.sql"
Write-Host "2. Check status: psql -h $DB_HOST -p $DB_PORT $DB_NAME -U $APP_USER -f status.sql"
Write-Host "====================================================="