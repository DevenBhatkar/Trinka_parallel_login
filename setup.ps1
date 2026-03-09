# setup.ps1 - Selenium Grid Setup for Windows (PowerShell)

Write-Host "=== Docker Desktop Required ===" -ForegroundColor Cyan
Write-Host "Make sure Docker Desktop is installed and running before proceeding."
Write-Host "Download from: https://www.docker.com/products/docker-desktop/"
Write-Host ""
Read-Host "Press Enter to continue once Docker Desktop is running"

Write-Host "=== Pulling Selenium Images ===" -ForegroundColor Cyan
docker pull selenium/hub:4.41.0
docker pull selenium/node-chrome:4.41.0

Write-Host "=== Creating Docker Network ===" -ForegroundColor Cyan
docker network create gridnetwork

Write-Host "=== Starting Selenium Hub ===" -ForegroundColor Cyan
docker run -d --name seleniumhub `
  --network gridnetwork `
  -p 4442:4442 -p 4443:4443 -p 4445:4444 `
  selenium/hub:4.41.0

Write-Host "=== Waiting for Hub to start ===" -ForegroundColor Cyan
Start-Sleep -Seconds 10

Write-Host "=== Starting 10 Chrome Nodes ===" -ForegroundColor Cyan
for ($i=1; $i -le 10; $i++) {
  docker run -d `
    --name chromeNode$i `
    --network gridnetwork `
    -p "590${i}:5900" `
    -p "790${i}:7900" `
    -e SE_EVENT_BUS_HOST=seleniumhub `
    -e SE_EVENT_BUS_PUBLISH_PORT=4442 `
    -e SE_EVENT_BUS_SUBSCRIBE_PORT=4443 `
    selenium/node-chrome:4.41.0
  Write-Host "Started chromeNode$i (VNC: 590$i)"
}

Write-Host "=== Waiting for Nodes to register ===" -ForegroundColor Cyan
Start-Sleep -Seconds 15

Write-Host ""
Write-Host "=== Done! ===" -ForegroundColor Green
Write-Host "Hub running at: http://localhost:4445"
Write-Host "VNC ports: 5901-5910 (password: secret)"
Write-Host ""
Write-Host "Now copy the project folder and run: mvn clean test"
