# Hướng Dẫn Triển Khai - Shopee Affiliate Link Shortener

## Mục Lục
1. [Yêu Cầu Hệ Thống](#yêu-cầu-hệ-thống)
2. [Cài Đặt Development](#cài-đặt-development)
3. [Triển Khai Production](#triển-khai-production)
4. [Docker Deployment](#docker-deployment)
5. [CI/CD Pipeline](#cicd-pipeline)
6. [Monitoring & Logging](#monitoring--logging)

---

## Yêu Cầu Hệ Thống

### Development
- **Java**: JDK 17 hoặc cao hơn
- **Node.js**: v18.x hoặc cao hơn
- **PostgreSQL**: 15.x
- **Redis**: 7.x
- **Maven**: 3.8+
- **Docker**: 20.10+ (optional)

### Production Server
- **OS**: Ubuntu 22.04 LTS hoặc CentOS 8+
- **RAM**: Tối thiểu 4GB (khuyến nghị 8GB)
- **CPU**: 2 cores (khuyến nghị 4 cores)
- **Disk**: 50GB SSD
- **Network**: 100Mbps

---

## Cài Đặt Development

### 1. Clone Repository
```bash
git clone https://github.com/yourname/WEB_Getshortlink.git
cd WEB_Getshortlink
```

### 2. Cấu Hình Environment
```bash
# Copy file .env.example
cp .env.example .env

# Chỉnh sửa file .env với thông tin của bạn
nano .env
```

### 3. Cài Đặt Database

#### PostgreSQL
```bash
# Ubuntu/Debian
sudo apt update
sudo apt install postgresql postgresql-contrib

# Start PostgreSQL
sudo systemctl start postgresql
sudo systemctl enable postgresql

# Create database
sudo -u postgres psql
CREATE DATABASE affiliate_db;
CREATE USER postgres WITH PASSWORD 'postgres123';
GRANT ALL PRIVILEGES ON DATABASE affiliate_db TO postgres;
\q

# Import schema
psql -U postgres -d affiliate_db -f database/schema.sql
```

#### Redis
```bash
# Ubuntu/Debian
sudo apt install redis-server

# Start Redis
sudo systemctl start redis-server
sudo systemctl enable redis-server

# Set password
redis-cli
CONFIG SET requirepass "redis123"
CONFIG REWRITE
exit
```

### 4. Backend Setup

```bash
cd backend

# Install dependencies
./mvnw clean install

# Run application
./mvnw spring-boot:run

# Or build JAR
./mvnw clean package
java -jar target/shortlink-0.0.1-SNAPSHOT.jar
```

Backend sẽ chạy tại: `http://localhost:8080`

### 5. Frontend Setup

```bash
cd frontend

# Install dependencies
npm install

# Run development server
npm run dev

# Build for production
npm run build
```

Frontend sẽ chạy tại: `http://localhost:5173`

---

## Triển Khai Production

### 1. Chuẩn Bị Server

```bash
# Update system
sudo apt update && sudo apt upgrade -y

# Install Java
sudo apt install openjdk-17-jdk -y

# Install Nginx
sudo apt install nginx -y

# Install Node.js
curl -fsSL https://deb.nodesource.com/setup_18.x | sudo -E bash -
sudo apt install -y nodejs

# Install PostgreSQL & Redis
sudo apt install postgresql redis-server -y
```

### 2. Build Application

#### Backend
```bash
cd backend
./mvnw clean package -DskipTests
```

#### Frontend
```bash
cd frontend
npm install
npm run build
```

### 3. Deploy Backend

```bash
# Create application directory
sudo mkdir -p /opt/shortlink
sudo cp backend/target/shortlink-0.0.1-SNAPSHOT.jar /opt/shortlink/

# Create systemd service
sudo nano /etc/systemd/system/shortlink.service
```

File `shortlink.service`:
```ini
[Unit]
Description=Shopee Affiliate Link Shortener
After=network.target

[Service]
Type=simple
User=www-data
WorkingDirectory=/opt/shortlink
ExecStart=/usr/bin/java -jar /opt/shortlink/shortlink-0.0.1-SNAPSHOT.jar
Restart=on-failure
RestartSec=10
StandardOutput=syslog
StandardError=syslog
SyslogIdentifier=shortlink

Environment="SPRING_PROFILES_ACTIVE=prod"
Environment="JAVA_OPTS=-Xms512m -Xmx2g"

[Install]
WantedBy=multi-user.target
```

```bash
# Start service
sudo systemctl daemon-reload
sudo systemctl start shortlink
sudo systemctl enable shortlink

# Check status
sudo systemctl status shortlink
```

### 4. Deploy Frontend

```bash
# Copy build files to Nginx
sudo cp -r frontend/dist/* /var/www/html/

# Configure Nginx
sudo nano /etc/nginx/sites-available/shortlink
```

File Nginx config:
```nginx
server {
    listen 80;
    server_name yourdomain.com www.yourdomain.com;

    root /var/www/html;
    index index.html;

    # Frontend
    location / {
        try_files $uri $uri/ /index.html;
    }

    # Backend API
    location /api/ {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    # Short link redirect
    location ~ ^/[a-zA-Z0-9]+$ {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

```bash
# Enable site
sudo ln -s /etc/nginx/sites-available/shortlink /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl restart nginx
```

### 5. SSL Certificate (Let's Encrypt)

```bash
# Install Certbot
sudo apt install certbot python3-certbot-nginx -y

# Get certificate
sudo certbot --nginx -d yourdomain.com -d www.yourdomain.com

# Auto-renewal
sudo certbot renew --dry-run
```

---

## Docker Deployment

### 1. Build Images

```bash
# Build all services
docker-compose build

# Or build individually
docker-compose build backend
docker-compose build frontend
```

### 2. Start Services

```bash
# Start all services
docker-compose up -d

# Check logs
docker-compose logs -f

# Check status
docker-compose ps
```

### 3. Stop Services

```bash
# Stop all
docker-compose down

# Stop and remove volumes
docker-compose down -v
```

### 4. Production Docker Compose

Create `docker-compose.prod.yml`:
```yaml
version: '3.8'

services:
  postgres:
    image: postgres:15-alpine
    environment:
      POSTGRES_PASSWORD: ${DB_PASSWORD}
    volumes:
      - postgres_data:/var/lib/postgresql/data
    restart: always

  redis:
    image: redis:7-alpine
    command: redis-server --requirepass ${REDIS_PASSWORD}
    volumes:
      - redis_data:/data
    restart: always

  backend:
    build: ./backend
    environment:
      SPRING_PROFILES_ACTIVE: prod
      SPRING_DATASOURCE_PASSWORD: ${DB_PASSWORD}
      SPRING_REDIS_PASSWORD: ${REDIS_PASSWORD}
    depends_on:
      - postgres
      - redis
    restart: always

  frontend:
    build: ./frontend
    ports:
      - "80:80"
      - "443:443"
    volumes:
      - ./ssl:/etc/nginx/ssl:ro
    depends_on:
      - backend
    restart: always

volumes:
  postgres_data:
  redis_data:
```

Deploy:
```bash
docker-compose -f docker-compose.prod.yml up -d
```

---

## CI/CD Pipeline

### GitHub Actions

Create `.github/workflows/deploy.yml`:
```yaml
name: Deploy to Production

on:
  push:
    branches: [main]

jobs:
  build-and-deploy:
    runs-on: ubuntu-latest
    
    steps:
      - uses: actions/checkout@v3
      
      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'temurin'
      
      - name: Build Backend
        run: |
          cd backend
          ./mvnw clean package -DskipTests
      
      - name: Set up Node.js
        uses: actions/setup-node@v3
        with:
          node-version: '18'
      
      - name: Build Frontend
        run: |
          cd frontend
          npm ci
          npm run build
      
      - name: Deploy to Server
        uses: appleboy/ssh-action@master
        with:
          host: ${{ secrets.SERVER_HOST }}
          username: ${{ secrets.SERVER_USER }}
          key: ${{ secrets.SSH_PRIVATE_KEY }}
          script: |
            cd /opt/shortlink
            git pull
            docker-compose down
            docker-compose up -d --build
```

---

## Monitoring & Logging

### 1. Application Logs

```bash
# Backend logs
sudo journalctl -u shortlink -f

# Nginx logs
sudo tail -f /var/log/nginx/access.log
sudo tail -f /var/log/nginx/error.log

# Docker logs
docker-compose logs -f backend
```

### 2. Database Backup

```bash
# Create backup script
sudo nano /opt/backup-db.sh
```

```bash
#!/bin/bash
BACKUP_DIR="/opt/backups"
DATE=$(date +%Y%m%d_%H%M%S)
FILENAME="affiliate_db_$DATE.sql"

mkdir -p $BACKUP_DIR
pg_dump -U postgres affiliate_db > $BACKUP_DIR/$FILENAME
gzip $BACKUP_DIR/$FILENAME

# Keep only last 7 days
find $BACKUP_DIR -name "*.sql.gz" -mtime +7 -delete

echo "Backup completed: $FILENAME.gz"
```

```bash
# Make executable
sudo chmod +x /opt/backup-db.sh

# Add to crontab (daily at 2 AM)
sudo crontab -e
0 2 * * * /opt/backup-db.sh
```

### 3. Health Checks

```bash
# Backend health
curl http://localhost:8080/actuator/health

# Database connection
psql -U postgres -d affiliate_db -c "SELECT 1"

# Redis connection
redis-cli -a redis123 ping
```

### 4. Performance Monitoring

Install Prometheus & Grafana:
```bash
# Prometheus
docker run -d -p 9090:9090 \
  -v ./prometheus.yml:/etc/prometheus/prometheus.yml \
  prom/prometheus

# Grafana
docker run -d -p 3000:3000 grafana/grafana
```

---

## Troubleshooting

### Backend không start
```bash
# Check logs
sudo journalctl -u shortlink -n 100

# Check port
sudo netstat -tulpn | grep 8080

# Check Java process
ps aux | grep java
```

### Database connection error
```bash
# Check PostgreSQL status
sudo systemctl status postgresql

# Check connection
psql -U postgres -h localhost -d affiliate_db

# Reset password
sudo -u postgres psql
ALTER USER postgres PASSWORD 'new_password';
```

### Redis connection error
```bash
# Check Redis status
sudo systemctl status redis-server

# Test connection
redis-cli -a redis123 ping

# Check config
redis-cli CONFIG GET requirepass
```

---

## Rollback

```bash
# Stop current version
sudo systemctl stop shortlink

# Restore previous JAR
sudo cp /opt/shortlink/backup/shortlink-previous.jar /opt/shortlink/shortlink-0.0.1-SNAPSHOT.jar

# Restore database
gunzip < /opt/backups/affiliate_db_20240115.sql.gz | psql -U postgres affiliate_db

# Start service
sudo systemctl start shortlink
```

---

## Security Checklist

- [ ] Change default passwords
- [ ] Enable firewall (ufw)
- [ ] Configure SSL/TLS
- [ ] Set up fail2ban
- [ ] Regular security updates
- [ ] Database backups
- [ ] Monitor logs
- [ ] Rate limiting enabled
- [ ] CORS configured properly
- [ ] Environment variables secured

---

## Support

Nếu gặp vấn đề, vui lòng:
1. Check logs
2. Review documentation
3. Search GitHub Issues
4. Contact support team
