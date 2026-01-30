@echo off
echo ========================================
echo Starting Backend (Spring Boot)
echo ========================================
cd backend
start cmd /k "mvnw.cmd spring-boot:run"

timeout /t 5

echo.
echo ========================================
echo Starting Frontend (Vue.js)
echo ========================================
cd ..\frontend
start cmd /k "npm run serve"

echo.
echo ========================================
echo Application is starting...
echo Backend: http://localhost:8080
echo Frontend: http://localhost:8081
echo ========================================
