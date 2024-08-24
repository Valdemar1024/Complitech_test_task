Database: PostgreSQL with Liquibase.

To start application:
1. ./gradlew clean build
2. cd test-local 
3. docker compose up --build -d
4. Start client

To stop application:
"docker compose down -v"

Configuration file for application: /test-local/app.env

Client Repository: https://github.com/Valdemar1024/Complitech_test_task_client
