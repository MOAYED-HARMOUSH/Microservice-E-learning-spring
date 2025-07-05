# Enhanced Load Balancer Test Script
Write-Host "========================================" -ForegroundColor Green
Write-Host "Enhanced Load Balancer Test" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host ""

$gatewayUrl = "http://localhost:8010"
$headers = @{
    "Content-Type" = "application/json"
}

# Test 1: Health Check
Write-Host "Test 1: Health Check" -ForegroundColor Yellow
Write-Host "GET $gatewayUrl/api/exams/loadbalancer/health"
try {
    $response = Invoke-RestMethod -Uri "$gatewayUrl/api/exams/loadbalancer/health" -Method GET -Headers $headers
    Write-Host "Response:" -ForegroundColor Cyan
    $response | ConvertTo-Json -Depth 3
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

# Test 2: Get Instances
Write-Host "Test 2: Get Instances" -ForegroundColor Yellow
Write-Host "GET $gatewayUrl/api/exams/loadbalancer/instances"
try {
    $response = Invoke-RestMethod -Uri "$gatewayUrl/api/exams/loadbalancer/instances" -Method GET -Headers $headers
    Write-Host "Response:" -ForegroundColor Cyan
    $response | ConvertTo-Json -Depth 3
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

# Test 3: Get Load Balancer Stats
Write-Host "Test 3: Get Load Balancer Stats" -ForegroundColor Yellow
Write-Host "GET $gatewayUrl/api/exams/loadbalancer/stats"
try {
    $response = Invoke-RestMethod -Uri "$gatewayUrl/api/exams/loadbalancer/stats" -Method GET -Headers $headers
    Write-Host "Response:" -ForegroundColor Cyan
    $response | ConvertTo-Json -Depth 3
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

# Test 4: Multiple Requests to Test Load Distribution
Write-Host "Test 4: Testing Load Distribution (10 requests)..." -ForegroundColor Yellow
Write-Host ""

$portCounts = @{}

for ($i = 1; $i -le 10; $i++) {
    Write-Host "Request $i:" -ForegroundColor Cyan -NoNewline
    try {
        $response = Invoke-RestMethod -Uri "$gatewayUrl/api/exams/loadbalancer/health" -Method GET -Headers $headers
        $port = $response.port
        Write-Host " Port $port" -ForegroundColor Green
        
        if ($portCounts.ContainsKey($port)) {
            $portCounts[$port]++
        } else {
            $portCounts[$port] = 1
        }
    } catch {
        Write-Host " Error: $($_.Exception.Message)" -ForegroundColor Red
    }
    Start-Sleep -Seconds 1
}

Write-Host ""
Write-Host "Load Distribution Summary:" -ForegroundColor Magenta
foreach ($port in $portCounts.Keys | Sort-Object) {
    $count = $portCounts[$port]
    $percentage = [math]::Round(($count / 10) * 100, 1)
    Write-Host "Port $port`: $count requests ($percentage%)" -ForegroundColor White
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Green
Write-Host "Test completed!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host ""
Write-Host "To monitor in real-time:" -ForegroundColor Cyan
Write-Host "1. Eureka Dashboard: http://localhost:8761" -ForegroundColor White
Write-Host "2. Individual instances:" -ForegroundColor White
Write-Host "   - Instance 1: http://localhost:8040/actuator/health" -ForegroundColor White
Write-Host "   - Instance 2: http://localhost:8041/actuator/health" -ForegroundColor White
Write-Host "   - Instance 3: http://localhost:8042/actuator/health" -ForegroundColor White
Write-Host ""
Write-Host "Press any key to continue..." -ForegroundColor Yellow
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown") 