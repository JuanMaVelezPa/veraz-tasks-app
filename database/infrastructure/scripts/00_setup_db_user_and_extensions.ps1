# Script to set up the application user and enable extensions for the Tasks Application database.
# This script assumes the 'tasks_app_db' database is already created manually.

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

# --- STEP 1: CREATE THE APPLICATION USER (IF IT DOES NOT EXIST) ---
Write-Host "Creating the application user '$APP_USER'..."
# Temporarily set the superuser password for the connection
$env:PGPASSWORD = $DB_SUPERUSER_PASSWORD
& $PSQL_PATH -h "$DB_HOST" -p "$DB_PORT" -U "$DB_SUPERUSER" -d postgres -c "CREATE USER \"$APP_USER\" WITH PASSWORD '$APP_PASSWORD';" 2>$null

# Check if the command was successful
if ($LASTEXITCODE -ne 0) {
    Write-Host "Warning: The user '$APP_USER' might already exist or there was an error creating it."
} else {
    Write-Host "User '$APP_USER' created successfully."
}
Remove-Item Env:PGPASSWORD # Clean the environment variable for the password for security

# --- STEP 2: GRANT ALL PERMISSIONS TO THE APPLICATION USER ---
Write-Host "Granting all permissions to '$APP_USER' on database '$DB_NAME' and its schema..."

# 2.1: Grant all privileges on the database object to the app user, connecting as superuser
$env:PGPASSWORD = $DB_SUPERUSER_PASSWORD
& $PSQL_PATH -h "$DB_HOST" -p "$DB_PORT" -U "$DB_SUPERUSER" -d "$DB_NAME" -c "GRANT ALL PRIVILEGES ON DATABASE \"$DB_NAME\" TO \"$APP_USER\";"
Remove-Item Env:PGPASSWORD

# 2.2: Grant all privileges on the 'public' schema to the app user, connecting as superuser
$env:PGPASSWORD = $DB_SUPERUSER_PASSWORD
& $PSQL_PATH -h "$DB_HOST" -p "$DB_PORT" -U "$DB_SUPERUSER" -d "$DB_NAME" -c "GRANT ALL ON SCHEMA public TO \"$APP_USER\";"
Remove-Item Env:PGPASSWORD

# 2.3: Set default privileges for future objects created by APP_USER in 'public' schema
Write-Host "Setting default privileges for future objects created by '$APP_USER' in 'public' schema..."
$env:PGPASSWORD = $APP_PASSWORD # Connect as APP_USER to set default privileges for APP_USER

# Using a here-string for multi-line SQL commands
$sql_commands = @"
ALTER DEFAULT PRIVILEGES FOR USER "$APP_USER" IN SCHEMA public GRANT ALL ON TABLES TO "$APP_USER";
ALTER DEFAULT PRIVILEGES FOR USER "$APP_USER" IN SCHEMA public GRANT ALL ON SEQUENCES TO "$APP_USER";
ALTER DEFAULT PRIVILEGES FOR USER "$APP_USER" IN SCHEMA public GRANT ALL ON FUNCTIONS TO "$APP_USER";
ALTER DEFAULT PRIVILEGES FOR USER "$APP_USER" IN SCHEMA public GRANT ALL ON TYPES TO "$APP_USER";
"@

& $PSQL_PATH -h "$DB_HOST" -p "$DB_PORT" -U "$APP_USER" -d "$DB_NAME" -c "$sql_commands"
Remove-Item Env:PGPASSWORD

if ($LASTEXITCODE -ne 0) {
    Write-Host "Error: Could not grant all permissions to '$APP_USER'."
    exit 1
} else {
    Write-Host "Permissions for '$APP_USER' granted successfully on '$DB_NAME'."
}

# --- STEP 3: ENABLE THE 'pgcrypto' EXTENSION ---
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

Write-Host "Database user and extension setup completed for '$DB_NAME'."