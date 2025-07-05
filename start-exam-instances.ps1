# PowerShell script to start multiple Exam Service instances
Write-Host "Starting Multiple Exam Service Instances for Load Balancing..." -ForegroundColor Green
Write-Host ""

# Set Java options for better performance
$env:JAVA_OPTS = "-Xmx512m -Xms256m"

# Get the current directory
$currentDir = Get-Location

# Start Exam Service Instance 1 (Port 8040)
Write-Host "Starting Exam Service Instance 1 on port 8040..." -ForegroundColor Yellow
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$currentDir\Exam'; `$env:SERVER_PORT='8040'; mvn spring-boot:run -Dspring-boot.run.jvmArguments='$env:JAVA_OPTS'" -WindowStyle Normal

# Wait 15 seconds for first instance to start
Write-Host "Waiting 15 seconds for first instance to start..." -ForegroundColor Cyan
Start-Sleep -Seconds 15

# Start Exam Service Instance 2 (Port 8041)
Write-Host "Starting Exam Service Instance 2 on port 8041..." -ForegroundColor Yellow
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$currentDir\Exam'; `$env:SERVER_PORT='8041'; mvn spring-boot:run -Dspring-boot.run.jvmArguments='$env:JAVA_OPTS'" -WindowStyle Normal

# Wait 15 seconds for second instance to start
Write-Host "Waiting 15 seconds for second instance to start..." -ForegroundColor Cyan
Start-Sleep -Seconds 15

# Start Exam Service Instance 3 (Port 8042)
Write-Host "Starting Exam Service Instance 3 on port 8042..." -ForegroundColor Yellow
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$currentDir\Exam'; `$env:SERVER_PORT='8042'; mvn spring-boot:run -Dspring-boot.run.jvmArguments='$env:JAVA_OPTS'" -WindowStyle Normal

Write-Host ""
Write-Host "All Exam Service instances are starting..." -ForegroundColor Green
Write-Host ""
Write-Host "Instance 1: http://localhost:8040" -ForegroundColor White
Write-Host "Instance 2: http://localhost:8041" -ForegroundColor White
Write-Host "Instance 3: http://localhost:8042" -ForegroundColor White
Write-Host ""
Write-Host "Load Balancer will distribute requests across these instances." -ForegroundColor Green
Write-Host ""
Write-Host "To test load balancing, run: .\test-load-balancer.bat" -ForegroundColor Cyan
Write-Host ""
Write-Host "Press any key to continue..." -ForegroundColor Yellow
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown") 