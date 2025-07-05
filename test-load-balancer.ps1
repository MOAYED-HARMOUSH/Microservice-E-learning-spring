# PowerShell script to test load balancing
Write-Host "Testing Load Balancer for Exam Service..." -ForegroundColor Green
Write-Host ""

# Set the gateway URL
$gatewayUrl = "http://localhost:8010"

Write-Host "Testing through API Gateway: $gatewayUrl" -ForegroundColor Cyan
Write-Host ""

# Test 1: Health Check
Write-Host "Test 1: Health Check" -ForegroundColor Yellow
Write-Host "GET $gatewayUrl/api/exams/loadbalancer/health"
try {
    $response = Invoke-RestMethod -Uri "$gatewayUrl/api/exams/loadbalancer/health" -Method GET -ContentType "application/json"
    $response | ConvertTo-Json -Depth 3
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

# Test 2: Get Instances
Write-Host "Test 2: Get Instances" -ForegroundColor Yellow
Write-Host "GET $gatewayUrl/api/exams/loadbalancer/instances"
try {
    $response = Invoke-RestMethod -Uri "$gatewayUrl/api/exams/loadbalancer/instances" -Method GET -ContentType "application/json"
    $response | ConvertTo-Json -Depth 3
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

# Test 3: Get Load Balancer Stats
Write-Host "Test 3: Get Load Balancer Stats" -ForegroundColor Yellow
Write-Host "GET $gatewayUrl/api/exams/loadbalancer/stats"
try {
    $response = Invoke-RestMethod -Uri "$gatewayUrl/api/exams/loadbalancer/stats" -Method GET -ContentType "application/json"
    $response | ConvertTo-Json -Depth 3
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

# Test 4: Multiple Requests to Test Load Distribution
Write-Host "Test 4: Testing Load Distribution (10 requests)..." -ForegroundColor Yellow
Write-Host ""

for ($i = 1; $i -le 10; $i++) {
    Write-Host "Request $i:" -ForegroundColor Cyan
    try {
        $response = Invoke-RestMethod -Uri "$gatewayUrl/api/exams/loadbalancer/health" -Method GET -ContentType "application/json"
        Write-Host "Port: $($response.port)" -ForegroundColor Green
    } catch {
        Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
    }
    Start-Sleep -Seconds 1
}

Write-Host ""
Write-Host "Load balancing test completed!" -ForegroundColor Green
Write-Host ""
Write-Host "To monitor in real-time, you can:" -ForegroundColor Cyan
Write-Host "1. Check Eureka Dashboard: http://localhost:8761" -ForegroundColor White
Write-Host "2. Check individual instances:" -ForegroundColor White
Write-Host "   - Instance 1: http://localhost:8040/actuator/health" -ForegroundColor White
Write-Host "   - Instance 2: http://localhost:8041/actuator/health" -ForegroundColor White
Write-Host "   - Instance 3: http://localhost:8042/actuator/health" -ForegroundColor White
Write-Host ""
Write-Host "Press any key to continue..." -ForegroundColor Yellow
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown") 