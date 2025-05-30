# ToComplete 后端服务

一个功能强大的备忘录和计划管理应用后端服务，基于 Spring Boot 和 Jimmer ORM 构建。

## 核心功能

### 基础功能
- ✅ 用户注册和登录（JWT认证）
- ✅ 备忘录 CRUD 操作
- ✅ 分类和标签管理
- ✅ 优先级和状态管理
- ✅ 多形式提醒设置
- ✅ 拍照打卡功能
- ✅ 地理位置提醒

### 高级功能
- 📅 长期计划管理（日/周/月/季/年）
- 🤖 AI 智能计划生成
- 🔒 专注模式和强制执行
- 📊 进度跟踪和统计
- 🌐 多端数据同步

## 技术栈

- **框架**: Spring Boot 3.5.0
- **ORM**: Jimmer 0.8.130
- **数据库**: MySQL 8.0+
- **缓存**: Redis
- **认证**: JWT
- **构建工具**: Gradle
- **Java版本**: JDK 21

## 主要依赖

```gradle
// Spring Boot核心
implementation("org.springframework.boot:spring-boot-starter-web")
implementation("org.springframework.boot:spring-boot-starter-security")
implementation("org.springframework.boot:spring-boot-starter-validation")
implementation("org.springframework.boot:spring-boot-starter-data-redis")
implementation("org.springframework.boot:spring-boot-starter-quartz")

// Jimmer ORM
implementation("org.babyfish.jimmer:jimmer-spring-boot-starter:0.8.130")
annotationProcessor("org.babyfish.jimmer:jimmer-apt:0.8.130")

// JWT
implementation("io.jsonwebtoken:jjwt-api:0.12.3")
runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.3")
runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.3")

// 工具类
implementation("cn.hutool:hutool-all:5.8.23")
compileOnly("org.projectlombok:lombok")
```

## 项目结构

```
src/main/java/org/napbad/tocomplete/
├── entity/              # Jimmer实体定义
│   ├── BaseEntity.java
│   ├── User.java
│   ├── Memo.java
│   ├── Plan.java
│   ├── Category.java
│   ├── Tag.java
│   ├── SubTask.java
│   ├── PlanItem.java
│   └── UserSetting.java
├── repository/          # 数据访问层
│   ├── UserRepository.java
│   ├── MemoRepository.java
│   └── PlanRepository.java
├── service/             # 业务逻辑层
│   ├── UserService.java
│   └── impl/
│       └── UserServiceImpl.java
├── controller/          # 控制器层
│   └── UserController.java
├── dto/                 # 数据传输对象
│   ├── UserDto.java
│   ├── MemoDto.java
│   ├── CategoryDto.java
│   ├── TagDto.java
│   └── SubTaskDto.java
├── common/              # 通用类
│   ├── Result.java
│   └── PageResult.java
├── util/                # 工具类
│   └── JwtUtil.java
├── config/              # 配置类
│   └── SecurityConfig.java
└── ToCompleteApplication.java
```

## 数据库设计

### 核心表结构

- **users**: 用户基本信息
- **user_settings**: 用户个性化设置
- **categories**: 备忘录分类
- **tags**: 标签管理
- **memos**: 备忘录主表
- **memo_tags**: 备忘录标签关联
- **sub_tasks**: 子任务
- **plans**: 计划管理
- **plan_items**: 计划具体事项

## API 接口

### 用户管理
- `POST /api/users/register` - 用户注册
- `POST /api/users/login` - 用户登录
- `GET /api/users/{id}` - 获取用户信息
- `PUT /api/users/{id}` - 更新用户信息
- `PUT /api/users/{id}/password` - 修改密码
- `GET /api/users/check-username` - 检查用户名是否存在
- `GET /api/users/check-email` - 检查邮箱是否存在

### 备忘录管理
- `POST /api/memos` - 创建备忘录
- `GET /api/memos` - 获取备忘录列表
- `GET /api/memos/{id}` - 获取备忘录详情
- `PUT /api/memos/{id}` - 更新备忘录
- `DELETE /api/memos/{id}` - 删除备忘录
- `PUT /api/memos/{id}/complete` - 完成备忘录

## 快速开始

### 1. 环境要求
- JDK 21+
- MySQL 8.0+
- Redis 6.0+

### 2. 数据库配置
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/tocomplete
    username: root
    password: your_password
  data:
    redis:
      host: localhost
      port: 6379
```

### 3. 运行项目
```bash
# 克隆项目
git clone <repository-url>

# 进入项目目录
cd backend/server/toComplete

# 构建项目
./gradlew build

# 运行项目
./gradlew bootRun
```

### 4. 访问API文档
启动后访问: http://localhost:8080/openapi-ui

## 测试

### 运行单元测试
```bash
./gradlew test
```

### 测试覆盖率
项目包含全面的单元测试，覆盖率目标：
- 核心业务逻辑: 100%
- Service层: 95%+
- Controller层: 90%+

### 主要测试用例
- 用户注册/登录功能
- 备忘录CRUD操作
- JWT认证和授权
- 数据验证和异常处理

## 开发规范

### 代码风格
- 使用 Lombok 减少样板代码
- 统一的异常处理机制
- RESTful API 设计规范
- 清晰的分层架构

### 数据库规范
- 统一的字段命名（下划线分隔）
- 软删除标记（is_deleted）
- 创建和更新时间戳
- 适当的索引设计

## 部署说明

### Docker 部署
```dockerfile
FROM openjdk:21-jdk-slim
COPY build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### 生产环境配置
- 配置 SSL 证书
- 设置 Redis 集群
- 配置 MySQL 主从复制
- 启用日志收集

## 贡献指南

1. Fork 项目
2. 创建功能分支
3. 提交代码
4. 创建 Pull Request

## 许可证

MIT License 