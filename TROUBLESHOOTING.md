# Troubleshooting Guide - Socket Hang Up Error

## Quick Fix (Most Common Issues)

### 1. Check if Container is Running
```bash
docker ps
```

**Expected Output:**
```
CONTAINER ID   IMAGE                           STATUS         PORTS
xxxxxxxxx      restaurant-management-backend   Up X seconds   0.0.0.0:8081->8080/tcp
```

**If container is NOT listed:**
```bash
# Start the container
docker-compose up -d

# Wait 15-20 seconds for startup
```

---

### 2. Check Container Logs
```bash
docker logs restaurant-management-backend-1 --tail 50
```

**✅ Success Indicators:**
```
Tomcat started on port 8080 (http) with context path '/'
Started RestaurantManagementApplication in X seconds
```

**❌ Error Indicators:**
- `Connection refused` - Database connection issues
- `OutOfMemoryError` - Insufficient Docker memory
- Container keeps restarting - Check logs for Java errors

---

### 3. Wait for Full Startup (Important!)

The application needs time to:
1. Connect to Supabase PostgreSQL (can take 10-30 seconds)
2. Initialize Hibernate/JPA
3. Start Tomcat web server

**Wait at least 20-30 seconds after seeing "Started RestaurantManagementApplication"**

---

## Step-by-Step Diagnostic

### Step 1: Restart Container
```bash
# Stop and remove containers
docker-compose down

# Rebuild and start fresh
docker-compose up -d

# Watch logs in real-time
docker logs -f restaurant-management-backend-1
```

Press `Ctrl+C` to stop watching logs when you see:
```
Started RestaurantManagementApplication in X seconds
```

---

### Step 2: Test Connection from Command Line

**Test if server is responding:**
```bash
curl http://localhost:8081/api/v1/categories
```

**Expected Response:**
```json
{
  "success": true,
  "status": 200,
  "data": [...]
}
```

**If you get an error:**
- `Connection refused` → Container not running or port issue
- `Empty reply` → Container starting but not ready yet
- `timeout` → Firewall or network issue

---

### Step 3: Verify Port Mapping

```bash
docker port restaurant-management-backend-1
```

**Expected Output:**
```
8080/tcp -> 0.0.0.0:8081
```

**If different or empty:**
- Check `docker-compose.yml` has correct port mapping: `8081:8080`
- Another service might be using port 8081

---

### Step 4: Check Container Health

```bash
# See container status
docker ps -a | grep backend

# If STATUS shows "Exited", check why it stopped
docker logs restaurant-management-backend-1 --tail 100
```

Common exit reasons:
- **Exit code 0**: Normal shutdown (manual stop)
- **Exit code 1**: Application crash (check logs for Java exception)
- **Exit code 137**: Out of memory (increase Docker memory limit)
- **Exit code 143**: SIGTERM (manual stop or system shutdown)

---

## Common Issues & Solutions

### Issue 1: Database Connection Timeout
**Symptoms:** Logs show "Connection timeout" or "Unable to connect to database"

**Solution:**
```bash
# Check if you can reach Supabase
ping aws-1-ap-southeast-2.pooler.supabase.com

# Verify credentials in src/main/resources/application.yml
# Make sure database URL, username, and password are correct
```

---

### Issue 2: Port Already in Use
**Symptoms:** `Bind for 0.0.0.0:8081 failed: port is already allocated`

**Solution:**
```bash
# Find what's using port 8081
netstat -ano | findstr :8081

# Option 1: Stop the other service
# Option 2: Change port in docker-compose.yml to 8082:8080
```

---

### Issue 3: Docker Out of Memory
**Symptoms:** Container keeps restarting, logs show `OutOfMemoryError`

**Solution:**
1. Open Docker Desktop
2. Settings → Resources
3. Increase Memory to at least 4GB
4. Click "Apply & Restart"
5. Run `docker-compose up -d` again

---

### Issue 4: Stale Container/Image
**Symptoms:** Changes not reflecting, old errors persist

**Solution:**
```bash
# Nuclear option - clean rebuild
docker-compose down
docker system prune -f
docker-compose build --no-cache
docker-compose up -d
```

---

## Testing Process for Team Members

### 1. Initial Setup
```bash
# Clone repository (if not done)
git clone <repository-url>
cd Restaurant-management

# Start services
docker-compose up -d

# Wait and watch logs
docker logs -f restaurant-management-backend-1

# Wait for: "Started RestaurantManagementApplication"
# Then wait 10 more seconds
```

---

### 2. Test in Postman

**Test Public Endpoint First (No Auth Required):**
```
GET http://localhost:8081/api/v1/categories
```
- Should return: `"success": true`
- If this works, API is ready!

**Then Test Register:**
```
POST http://localhost:8081/api/v1/auth/register
Headers: Content-Type: application/json
Body (raw JSON):
{
  "username": "yourname",
  "password": "password123",
  "fullName": "Your Name",
  "phone": "0912345678",
  "email": "you@example.com"
}
```

---

### 3. If Still Getting Socket Hang Up

**Check Postman Settings:**
1. Settings → General
2. Turn OFF "SSL certificate verification"
3. Turn OFF "Automatically follow redirects"
4. Request timeout: Set to 10000ms (10 seconds)

**Try from different tool:**
```bash
# Using curl
curl -X POST http://localhost:8081/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"test","password":"pass123","fullName":"Test","phone":"0900000000","email":"test@test.com"}'

# Using PowerShell
Invoke-WebRequest -Uri http://localhost:8081/api/v1/auth/register -Method POST -ContentType "application/json" -Body '{"username":"test","password":"pass123","fullName":"Test","phone":"0900000000","email":"test@test.com"}'
```

If curl/PowerShell works but Postman doesn't → Postman configuration issue

---

## Environment Checklist

Before asking for help, verify:

- [ ] Docker Desktop is running
- [ ] Container status is "Up" (not "Exited")
- [ ] Waited at least 30 seconds after container started
- [ ] Can access `http://localhost:8081/api/v1/categories` in browser
- [ ] Logs show "Started RestaurantManagementApplication"
- [ ] No errors in container logs
- [ ] Port 8081 is not used by another application
- [ ] Using correct URL: `http://localhost:8081` (not https, not 8080)

---

## Quick Reference Commands

```bash
# Start services
docker-compose up -d

# Stop services
docker-compose down

# View logs
docker logs restaurant-management-backend-1 --tail 50

# Follow logs in real-time
docker logs -f restaurant-management-backend-1

# Restart services
docker-compose restart

# Rebuild from scratch
docker-compose down
docker-compose build --no-cache
docker-compose up -d

# Check running containers
docker ps

# Check all containers (including stopped)
docker ps -a

# Remove all stopped containers
docker container prune

# Check Docker disk usage
docker system df
```

---

## Still Having Issues?

1. **Collect diagnostic info:**
```bash
# Save logs to file
docker logs restaurant-management-backend-1 > logs.txt

# Get container details
docker inspect restaurant-management-backend-1 > container-info.txt

# Get compose configuration
docker-compose config > compose-config.txt
```

2. **Share these files with the team lead**

3. **Include:**
   - Operating system (Windows/Mac/Linux)
   - Docker Desktop version
   - Error message from Postman
   - Output of `docker ps`
   - Last 50 lines of container logs

---

## Success Checklist

You know everything is working when:

✅ `docker ps` shows container as "Up"
✅ Logs show "Started RestaurantManagementApplication"
✅ `GET http://localhost:8081/api/v1/categories` returns JSON in browser
✅ Postman can successfully register a new user
✅ You can login and get a JWT token

---

**Last Updated:** 2026-03-18
**For Questions:** Check API_TESTING_GUIDE.md or contact team lead
