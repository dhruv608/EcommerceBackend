# 🚀 Deployment Guide - Spring Boot E-commerce Backend

## 📋 Table of Contents
- [Deployment Options](#-deployment-options)
- [Option 1: Traditional VPS Deployment](#-option-1-traditional-vps-deployment)
- [Option 2: Docker Deployment](#-option-2-docker-deployment)
- [Option 3: Cloud Platform Deployment](#-option-3-cloud-platform-deployment)
- [Pre-Deployment Checklist](#-pre-deployment-checklist)
- [Environment Configuration](#-environment-configuration)

---

## 🌐 Deployment Options

### Option 1: Traditional VPS (Recommended for beginners)
- DigitalOcean, Vultr, Linode
- Full control over server
- Cost-effective ($5-10/month)

### Option 2: Docker Container
- Portable and scalable
- Easy to deploy anywhere
- Best for microservices

### Option 3: Cloud Platform
- Heroku, AWS, Google Cloud
- Managed deployment
- Easier but more expensive

---

## 🖥️ Option 1: Traditional VPS Deployment

### Step 1: Server Setup
```bash
# Update server
sudo apt update && sudo apt upgrade -y

# Install Java 17
sudo apt install openjdk-17-jdk -y

# Install Maven
sudo apt install maven -y

# Install Nginx (for reverse proxy)
sudo apt install nginx -y

# Install PostgreSQL (if not using Neon)
sudo apt install postgresql postgresql-contrib -y
```

### Step 2: Database Configuration
```bash
# If using local PostgreSQL
sudo -u postgres createdb ecommerce
sudo -u postgres createuser --interactive
```

### Step 3: Deploy Application
```bash
# Clone your project
git clone <your-repo-url>
cd StoreBackend

# Build the project
./mvnw clean package -DskipTests

# Run the application
java -jar target/backend-1.0.jar
```

### Step 4: Nginx Reverse Proxy
```nginx
# /etc/nginx/sites-available/ecommerce
server {
    listen 80;
    server_name your-domain.com;

    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

---

## 🐳 Option 2: Docker Deployment

### Step 1: Create Dockerfile
```dockerfile
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/backend-1.0.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Step 2: Create docker-compose.yml
```yaml
version: '3.8'
services:
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_DATASOURCE_URL=jdbc:postgresql://neondb_owner:password@ep-green-leaf-aimjvu4j-pooler.c-4.us-east-1.aws.neon.tech/neondb
      - SPRING_DATASOURCE_USERNAME=neondb_owner
      - SPRING_DATASOURCE_PASSWORD=your-password
    depends_on:
      - db

  nginx:
    image: nginx:alpine
    ports:
      - "80:80"
    volumes:
      - ./nginx.conf:/etc/nginx/nginx.conf
    depends_on:
      - app
```

### Step 3: Deploy with Docker
```bash
# Build and run
docker-compose up -d

# Check logs
docker-compose logs -f app
```

---

## ☁️ Option 3: Cloud Platform Deployment

### Heroku Deployment
```bash
# Install Heroku CLI
# Login to Heroku
heroku login

# Create app
heroku create your-app-name

# Set environment variables
heroku config:set SPRING_DATASOURCE_URL=jdbc:postgresql://neondb_owner:password@ep-green-leaf-aimjvu4j-pooler.c-4.us-east-1.aws.neon.tech/neondb

# Deploy
git push heroku main
```

### AWS Elastic Beanstalk
```bash
# Install EB CLI
eb init your-app-name
eb create production-environment
eb deploy
```

---

## ✅ Pre-Deployment Checklist

### 1. Code Preparation
- [ ] Remove development dependencies
- [ ] Set production database URL
- [ ] Disable debug mode
- [ ] Set proper logging levels

### 2. Security
- [ ] Change default passwords
- [ ] Set up environment variables
- [ ] Configure firewall
- [ ] Enable HTTPS

### 3. Performance
- [ ] Optimize database connections
- [ ] Set up caching
- [ ] Configure memory settings
- [ ] Monitor resource usage

---

## 🔧 Environment Configuration

### Production application.properties
```properties
# Server Configuration
server.port=8080
server.address=0.0.0.0

# Database (Neon PostgreSQL)
spring.datasource.url=jdbc:postgresql://ep-green-leaf-aimjvu4j-pooler.c-4.us-east-1.aws.neon.tech/neondb
spring.datasource.username=neondb_owner
spring.datasource.password=${DATABASE_PASSWORD}
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=false

# Logging
logging.level.root=WARN
logging.level.com.store.backend=INFO
logging.level.org.springframework.web=WARN

# File Upload
spring.servlet.multipart.enabled=true
spring.servlet.multipart.max-file-size=5MB
spring.servlet.multipart.max-request-size=10MB
```

### Environment Variables
```bash
# Set these in your hosting environment
export DATABASE_PASSWORD="your-neon-password"
export SPRING_PROFILES_ACTIVE=production
```

---

## 🚀 Quick Deploy Script

### deploy.sh
```bash
#!/bin/bash

echo "Starting deployment..."

# Build project
echo "Building application..."
./mvnw clean package -DskipTests

# Stop existing service
echo "Stopping existing service..."
sudo systemctl stop ecommerce || true

# Copy new jar
echo "Deploying new version..."
sudo cp target/backend-1.0.jar /opt/ecommerce/app.jar

# Start service
echo "Starting service..."
sudo systemctl start ecommerce

# Check status
echo "Checking service status..."
sudo systemctl status ecommerce

echo "Deployment completed!"
```

---

## 📊 Monitoring & Maintenance

### Health Check Endpoint
```bash
curl http://your-domain.com/actuator/health
```

### Log Monitoring
```bash
# View application logs
sudo journalctl -u ecommerce -f

# View Nginx logs
sudo tail -f /var/log/nginx/access.log
```

### Backup Strategy
```bash
# Database backup (if using local PostgreSQL)
pg_dump ecommerce > backup_$(date +%Y%m%d).sql

# Application backup
cp /opt/ecommerce/app.jar /opt/ecommerce/backup/app_$(date +%Y%m%d).jar
```

---

## 🛠️ Troubleshooting

### Common Issues
1. **Port already in use**
   ```bash
   sudo netstat -tulpn | grep :8080
   sudo kill -9 <PID>
   ```

2. **Database connection failed**
   - Check database URL
   - Verify credentials
   - Check network connectivity

3. **Memory issues**
   ```bash
   # Increase heap size
   java -Xmx512m -Xms256m -jar app.jar
   ```

4. **File permissions**
   ```bash
   sudo chown -R $USER:$USER /opt/ecommerce
   chmod +x deploy.sh
   ```

---

## 🎯 Recommended Deployment Path

### For Beginners:
1. **DigitalOcean Droplet** ($5/month)
2. **Manual deployment** (learn the process)
3. **Nginx reverse proxy**
4. **Neon PostgreSQL** (you already have this)

### For Production:
1. **Docker containerization**
2. **CI/CD pipeline**
3. **Load balancing**
4. **SSL certificate**

---

## 📞 Support

Your application is ready for deployment! Choose the option that best fits your needs and budget.

**Need help with specific deployment?** Let me know which option you prefer and I'll provide detailed steps! 🚀
