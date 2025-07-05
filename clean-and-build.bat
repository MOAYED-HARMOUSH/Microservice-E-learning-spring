@echo off
echo ========================================
echo Cleaning and Building Exam Service
echo ========================================
echo.

cd Exam

echo Step 1: Cleaning project...
call mvn clean -q
if %ERRORLEVEL% neq 0 (
    echo ERROR: Clean failed!
    pause
    exit /b 1
)

echo Step 2: Compiling project...
call mvn compile -q
if %ERRORLEVEL% neq 0 (
    echo ERROR: Compilation failed!
    pause
    exit /b 1
)

echo Step 3: Running tests...
call mvn test -q
if %ERRORLEVEL% neq 0 (
    echo WARNING: Tests failed, but continuing...
)

echo Step 4: Building project...
call mvn package -DskipTests -q
if %ERRORLEVEL% neq 0 (
    echo ERROR: Build failed!
    pause
    exit /b 1
)

echo.
echo ========================================
echo Build completed successfully!
echo ========================================
echo.
echo You can now run the service instances.
echo.
pause 