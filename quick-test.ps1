# Quick Load Balancer Test
Write-Host "Quick Load Balancer Test" -ForegroundColor Green
Write-Host "=========================" -ForegroundColor Green
Write-Host ""

$gatewayUrl = "http://localhost:8010"

# Quick health check
Write-Host "Testing Load Balancer Health..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$gatewayUrl/api/exams/loadbalancer/health" -Method GET
    Write-Host "✅ Load Balancer is working!" -ForegroundColor Green
    Write-Host "Current instance: Port $($response.port)" -ForegroundColor Cyan
} catch {
    Write-Host "❌ Load Balancer test failed: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "Testing Load Distribution (5 requests)..." -ForegroundColor Yellow
Write-Host ""

for ($i = 1; $i -le 5; $i++) {
    try {
        $response = Invoke-RestMethod -Uri "$gatewayUrl/api/exams/loadbalancer/health" -Method GET
        Write-Host "Request $i`: Port $($response.port)" -ForegroundColor White
    } catch {
        Write-Host "Request $i`: Error" -ForegroundColor Red
    }
    Start-Sleep -Milliseconds 500
}

Write-Host ""
Write-Host "✅ Test completed!" -ForegroundColor Green
Write-Host ""
Write-Host "If you see different ports, the load balancer is working correctly!" -ForegroundColor Cyan 