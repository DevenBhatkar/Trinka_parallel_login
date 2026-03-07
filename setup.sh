#!/bin/bash

echo "=== Installing Docker ==="
sudo apt-get update
sudo apt-get install -y docker.io
sudo systemctl start docker
sudo systemctl enable docker
sudo usermod -aG docker $USER

echo "=== Installing Java 11 & Maven ==="
sudo apt-get install -y openjdk-11-jdk maven

echo "=== Pulling Selenium Images ==="
sudo docker pull selenium/hub:4.41.0
sudo docker pull selenium/node-chrome:4.41.0

echo "=== Creating Docker Network ==="
sudo docker network create gridnetwork

echo "=== Starting Selenium Hub ==="
sudo docker run -d --name seleniumhub \
  --network gridnetwork \
  -p 4442:4442 -p 4443:4443 -p 4445:4444 \
  selenium/hub:4.41.0

echo "=== Waiting for Hub to start ==="
sleep 10

echo "=== Starting 10 Chrome Nodes ==="
for i in $(seq 1 10); do
  sudo docker run -d \
    --name chromeNode$i \
    --network gridnetwork \
    -p 590$i:5900 \
    -p 790$i:7900 \
    -e SE_EVENT_BUS_HOST=seleniumhub \
    -e SE_EVENT_BUS_PUBLISH_PORT=4442 \
    -e SE_EVENT_BUS_SUBSCRIBE_PORT=4443 \
    selenium/node-chrome:4.41.0
  echo "Started chromeNode$i (VNC: 590$i)"
done

echo "=== Waiting for Nodes to register ==="
sleep 15

echo "=== Done! ==="
echo "Hub running at: http://localhost:4445"
echo "VNC ports: 5901-5910 (password: secret)"
echo ""
echo "Now copy the project folder and run: mvn clean test"
