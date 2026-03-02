# 🚀 Render Deployment Guide - Spring Boot E-commerce Backend

## 📋 Table of Contents
- [Prerequisites](#-prerequisites)
- [Deployment Steps](#-deployment-steps)
- [Environment Configuration](#-environment-configuration)
- [Post-Deployment](#-post-deployment)

---

## ✅ Prerequisites

### 1. Render Account
- Sign up at [render.com](https://render.com)
- Verify your email address
- Add a payment method (free tier available)

### 2. Git Repository
- Push your code to GitHub/GitLab
- Make sure all files are committed

### 3. Neon Database (Already have this!)
- Your Neon PostgreSQL database is ready
- Connection string: `ep-green-leaf-aimjvu4j-pooler.c-4.us-east-1.aws.neon.tech/neondb`

---

## 🚀 Deployment Steps

### Step 1: Create New Web Service

1. **Login to Render Dashboard**
2. Click **"New +"** → **"Web Service"**
3. **Connect your Git repository**
   - Choose GitHub/GitLab
   - Select your `StoreBackend` repository
   - Click **"Connect"**

### Step 2: Configure Service

**Basic Settings:**
- **Name:** `ecommerce-backend`
- **Region:** Choose closest to your users
- **Branch:** `main` (or your default branch)
- **Runtime:** **Native** (Spring Boot)

**Build Settings:**
- **Build Command:** `./mvnw clean package -DskipTests`
- **Start Command:** `java -jar target/backend-1.0.jar --spring.profiles.active=production`

**Instance:**
- **Instance Type:** **Free** (to start)
- **Health Check Path:** `/actuator/health`
- **Port:** `8080`

### Step 5: Deploy!

Click **"Create Web Service"** and Render will:
1. Build your application
2. Deploy to cloud
3. Provide a public URL
4. Set up SSL automatically

---

## 📁 Required Files (Already Created)

### ✅ `Dockerfile`
```dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/backend-1.0.jar app.jar
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=production"]
```

### ✅ `render.yaml` (Optional but recommended)
```yaml
services:
  type: web
  name: ecommerce-backend
  env:
    - key: SPRING_PROFILES_ACTIVE
      value: production
    - key: SPRING_DATASOURCE_URL
      value: jdbc:postgresql://ep-green-leaf-aimjvu4j-pooler.c-4.us-east-1.aws.neon.tech/neondb?sslmode=require&channel_binding=require&user=neondb_owner&password=npg_7EbcpYaZXU0r
    - key: SPRING_DATASOURCE_USERNAME
      value: neondb_owner
    - key: SPRING_DATASOURCE_PASSWORD
      value: npg_7EbcpYaZXU0r
    - key: SERVER_PORT
      value: 8080
  buildCommand: ./mvnw clean package -DskipTests
  startCommand: java -jar target/backend-1.0.jar --spring.profiles.active=production
  healthCheckPath: /actuator/health
  instanceType: free
  port: 8080
  autoDeploy: true
  plan: free
```

---

## 🌐 Post-Deployment

### Your Live Application
After deployment, you'll get:
- **🌐 Public URL:** `https://ecommerce-backend.onrender.com`
- **📊 Health Check:** `https://ecommerce-backend.onrender.com/actuator/health`
- **📋 Logs:** Available in Render dashboard
- **🔒 SSL:** Automatically configured

### Test Your APIs
```bash
# Test health
curl https://ecommerce-backend.onrender.com/actuator/health

# Test products
curl https://ecommerce-backend.onrender.com/products

# Test cart
curl -X POST "https://ecommerce-backend.onrender.com/cart/add?userId=1" \
  -H "Content-Type: application/json" \
  -d '{"productId": 1, "quantity": 2}'
```

---

## 💡 Render Features You Get

✅ **Free Tier** - 750 hours/month  
✅ **Auto SSL** - HTTPS automatically  
✅ **Auto-Deploy** - Git push triggers  
✅ **Health Monitoring** - Automatic restarts  
✅ **Custom Domain** - Your own domain  
✅ **Logs** - Real-time application logs  
✅ **Backups** - Automatic database backups  

---

## 🔄 Updates & Maintenance

### Automatic Updates
- Push to Git → Auto-deploy
- Render handles restarts
- Zero downtime updates

### Manual Updates
- Go to Render dashboard
- Click "Manual Deploy"
- Choose branch/commit

---

## 📊 Monitoring

### Health Dashboard
- Service status
- Response times
- Error rates
- Resource usage

### Logs
- Application logs
- Build logs
- Error tracking

---

## 🎯 Next Steps

1. **Deploy now** using the steps above
2. **Test all APIs** with the provided URL
3. **Set up custom domain** (optional)
4. **Monitor performance** in Render dashboard

---

## 🆘 Troubleshooting

### Common Issues
1. **Build fails** - Check Maven dependencies
2. **Database connection** - Verify Neon credentials
3. **Health check fails** - Check application logs

### Get Help
- Render documentation: render.com/docs
- Application logs in dashboard
- Render status page: status.render.com

---

## 🎉 Success!

Your e-commerce backend will be live at:
**`https://ecommerce-backend.onrender.com`**

With all these APIs working:
- 🔐 Authentication (3 endpoints)
- 🛍️ Products (8 endpoints)  
- 📂 Categories (4 endpoints)
- 🛒 Cart (7 endpoints)
- 📸 Image upload

**Ready to deploy! 🚀**
