# ToComplete - 智能备忘录和计划管理系统

一个全栈的任务管理和时间规划应用，支持智能提醒、拍照打卡、地理位置提醒等功能。

## 🌟 项目特色

### 核心功能
- **📝 智能备忘录**：支持富文本编辑、优先级设置、分类管理
- **📅 精确计划管理**：从分钟级到年度的多层次计划系统
- **🔔 多样化提醒**：时间提醒、地理位置提醒、拍照打卡
- **📊 数据统计**：任务完成率、时间分析、个人效率报告
- **🌙 强制执行**：锁屏模式、专注时间、Tomato Timer集成
- **🤖 AI助手**：智能计划生成、时间安排优化建议

### 技术亮点
- **后端**：Spring Boot 3.5 + Jimmer ORM + Redis + Quartz调度
- **前端Web**：Vue 3 + Element Plus + Pinia状态管理
- **移动端**：Flutter + Material Design 3 + Clean Architecture
- **数据库**：MySQL + Redis缓存 + 本地存储
- **部署**：Docker容器化 + Nginx反向代理

## 🏗️ 项目架构

```
ToComplete/
├── backend/                    # Java Spring Boot后端
│   ├── src/main/java/
│   │   ├── controller/        # REST API控制器
│   │   ├── service/          # 业务逻辑层
│   │   ├── repository/       # 数据访问层
│   │   ├── entity/           # JPA实体类
│   │   ├── dto/              # 数据传输对象
│   │   ├── config/           # 配置类
│   │   └── common/           # 通用组件
│   ├── src/main/resources/
│   │   ├── application.yml   # 应用配置
│   │   └── schema.sql        # 数据库schema
│   └── pom.xml              # Maven依赖
├── frontend/
│   ├── web/                  # Vue.js Web应用
│   │   ├── src/
│   │   │   ├── views/        # 页面组件
│   │   │   ├── components/   # 可复用组件
│   │   │   ├── stores/       # Pinia状态管理
│   │   │   ├── api/          # API请求封装
│   │   │   ├── router/       # 路由配置
│   │   │   └── utils/        # 工具函数
│   │   ├── package.json      # NPM依赖
│   │   └── vite.config.js    # Vite构建配置
│   └── mobile/              # Flutter移动应用
│       ├── lib/
│       │   ├── core/         # 核心模块
│       │   ├── data/         # 数据层
│       │   ├── domain/       # 领域层
│       │   └── presentation/ # 表现层
│       └── pubspec.yaml     # Flutter依赖
├── docs/                    # 项目文档
├── docker/                  # Docker配置
└── README.md               # 项目说明
```

## 🚀 快速开始

### 环境要求
- Java 17+
- Node.js 18+
- Flutter 3.10+
- MySQL 8.0+
- Redis 6.0+
- Docker (可选)

### 1. 后端启动

```bash
# 进入后端目录
cd backend

# 配置数据库连接
cp src/main/resources/application-example.yml src/main/resources/application.yml
# 编辑application.yml，配置数据库和Redis连接

# 启动后端服务
mvn spring-boot:run
```

后端服务将在 http://localhost:8080 启动

### 2. Web前端启动

```bash
# 进入Web前端目录
cd frontend/web/tocomplete-web

# 安装依赖
npm install

# 启动开发服务器
npm run dev
```

Web应用将在 http://localhost:5173 启动

### 3. 移动端开发

```bash
# 安装Flutter（如果尚未安装）
# 访问 https://flutter.dev/docs/get-started/install

# 进入移动端目录
cd frontend/mobile/tocomplete_mobile

# 获取依赖
flutter pub get

# 运行Android模拟器或连接设备
flutter run
```

### 4. Docker部署（推荐）

```bash
# 构建并启动所有服务
docker-compose up -d

# 查看服务状态
docker-compose ps

# 查看日志
docker-compose logs -f
```

## 📱 功能截图

### Web端界面
- 现代化的Vue.js界面设计
- Element Plus组件库
- 响应式布局支持
- 深色/浅色主题切换

### 移动端界面
- Material Design 3设计规范
- 原生性能的Flutter应用
- 跨平台兼容（Android/iOS）
- 本地通知和后台同步

## 🔧 开发工具推荐

### 后端开发
- **IDE**: IntelliJ IDEA
- **数据库工具**: DataGrip / Navicat
- **API测试**: Postman / Apifox
- **版本控制**: Git

### 前端开发
- **IDE**: VS Code
- **浏览器扩展**: Vue DevTools
- **设计工具**: Figma
- **包管理**: npm / yarn

### 移动端开发
- **IDE**: Android Studio / VS Code
- **模拟器**: Android Emulator / iOS Simulator
- **调试工具**: Flutter Inspector
- **设备测试**: 真机调试

## 📊 数据库设计

### 核心表结构

```sql
-- 用户表
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 备忘录表
CREATE TABLE memos (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    content TEXT,
    priority TINYINT DEFAULT 2,
    status TINYINT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- 计划表
CREATE TABLE plans (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    start_time DATETIME,
    end_time DATETIME,
    plan_type TINYINT DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

## 🔐 安全特性

- **JWT认证**：无状态的用户认证
- **密码加密**：BCrypt安全哈希
- **CORS配置**：跨域请求安全控制
- **SQL注入防护**：参数化查询
- **XSS防护**：输入验证和输出转义
- **HTTPS支持**：生产环境SSL/TLS

## 🌐 API文档

### 认证接口
```
POST /api/auth/login     # 用户登录
POST /api/auth/register  # 用户注册
POST /api/auth/logout    # 用户登出
POST /api/auth/refresh   # 刷新Token
```

### 备忘录接口
```
GET    /api/memos           # 获取备忘录列表
POST   /api/memos           # 创建备忘录
GET    /api/memos/{id}      # 获取备忘录详情
PUT    /api/memos/{id}      # 更新备忘录
DELETE /api/memos/{id}      # 删除备忘录
```

### 计划接口
```
GET    /api/plans           # 获取计划列表
POST   /api/plans           # 创建计划
GET    /api/plans/{id}      # 获取计划详情
PUT    /api/plans/{id}      # 更新计划
DELETE /api/plans/{id}      # 删除计划
```

## 🧪 测试

### 后端测试
```bash
# 运行单元测试
mvn test

# 运行集成测试
mvn verify

# 生成测试报告
mvn site
```

### 前端测试
```bash
# Web端测试
cd frontend/web/tocomplete-web
npm run test

# 移动端测试
cd frontend/mobile/tocomplete_mobile
flutter test
```

## 📦 部署指南

### 生产环境部署

1. **数据库初始化**
```sql
-- 创建数据库
CREATE DATABASE tocomplete CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 导入schema
mysql -u root -p tocomplete < docs/schema.sql
```

2. **后端部署**
```bash
# 打包应用
mvn clean package -Pprod

# 运行JAR包
java -jar target/tocomplete-backend-1.0.0.jar --spring.profiles.active=prod
```

3. **前端部署**
```bash
# Web端构建
cd frontend/web/tocomplete-web
npm run build

# 部署到Nginx
cp -r dist/* /var/www/html/
```

4. **移动端发布**
```bash
# Android APK构建
flutter build apk --release

# iOS构建（需要Mac环境）
flutter build ios --release
```

### Docker Compose部署

```yaml
version: '3.8'
services:
  backend:
    build: ./backend
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=docker
    depends_on:
      - mysql
      - redis

  frontend:
    build: ./frontend/web
    ports:
      - "80:80"
    depends_on:
      - backend

  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: tocomplete123
      MYSQL_DATABASE: tocomplete
    volumes:
      - mysql_data:/var/lib/mysql

  redis:
    image: redis:7-alpine
    ports:
      - "6379:6379"

volumes:
  mysql_data:
```

## 🤝 贡献指南

1. Fork 本仓库
2. 创建功能分支 (`git checkout -b feature/amazing-feature`)
3. 提交更改 (`git commit -m 'Add some amazing feature'`)
4. 推送到分支 (`git push origin feature/amazing-feature`)
5. 开启 Pull Request

### 代码规范
- **Java**: 遵循Google Java Style Guide
- **JavaScript**: 使用ESLint + Prettier
- **Dart**: 遵循Effective Dart Style Guide

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

## 👥 团队

- **后端开发**: Spring Boot + Jimmer ORM架构
- **前端开发**: Vue.js + Flutter跨平台解决方案
- **UI/UX设计**: Material Design + Element Plus设计系统
- **运维部署**: Docker + Nginx + MySQL生产环境

## 📞 联系我们

- 项目地址: [GitHub Repository](https://github.com/your-username/tocomplete)
- 问题反馈: [Issues](https://github.com/your-username/tocomplete/issues)
- 功能请求: [Feature Requests](https://github.com/your-username/tocomplete/discussions)

---

**ToComplete** - 让时间管理变得简单高效！ 🚀 