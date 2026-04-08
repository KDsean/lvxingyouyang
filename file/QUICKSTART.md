# 快速开始指南

## 项目概述

这是一个完整的旅游网站后端系统，基于 Spring Boot 3.2 + MySQL 开发。包含用户认证、酒店、机票、火车、租车、景点、攻略、餐厅、购物等完整的旅游服务模块。

## 环境要求

- Java 17 或更高版本
- Maven 3.6 或更高版本
- MySQL 8.0 或更高版本
- IDE: IntelliJ IDEA 或 Eclipse

## 安装步骤

### 1. 数据库设置

```bash
# 登录 MySQL
mysql -u root -p

# 执行初始化脚本
source init.sql
```

或者直接在 MySQL 客户端中执行 `init.sql` 文件中的 SQL 语句。

### 2. 修改配置文件

编辑 `src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/lvxingyouyang?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
    username: root
    password: your_mysql_password
```

### 3. 编译项目

```bash
mvn clean install
```

### 4. 运行应用

```bash
mvn spring-boot:run
```

或者使用 IDE 直接运行 `LvxingyouyangApplication.java`

### 5. 验证应用

访问 `http://localhost:8080/api/hotels/popular` 应该返回酒店数据。

## 项目结构说明

```
back-end/
├── src/main/java/com/lvxingyouyang/
│   ├── controller/          # REST API 控制器
│   ├── service/             # 业务逻辑服务
│   ├── repository/          # 数据访问层
│   ├── entity/              # JPA 实体类
│   ├── dto/                 # 数据传输对象
│   ├── security/            # JWT 认证相关
│   ├── config/              # Spring 配置类
│   ├── util/                # 工具类
│   └── LvxingyouyangApplication.java  # 主应用类
├── src/main/resources/
│   └── application.yml      # 应用配置
├── pom.xml                  # Maven 依赖配置
├── init.sql                 # 数据库初始化脚本
└── README.md
```

## 核心功能模块

### 1. 认证模块 (AuthService)
- 用户注册和登录
- JWT Token 生成和验证
- 用户信息管理

### 2. 酒店模块 (HotelService)
- 酒店搜索和筛选
- 酒店详情查询
- 推荐和热门酒店

### 3. 机票模块 (FlightService)
- 航班搜索
- 航班详情查询
- 热门航线推荐

### 4. 火车模块 (TrainService)
- 火车票搜索
- 火车详情查询

### 5. 租车模块 (CarRentalService)
- 租车搜索
- 租车详情查询

### 6. 景点模块 (AttractionService)
- 景点搜索
- 景点详情查询
- 推荐景点

### 7. 攻略模块 (GuideService)
- 攻略发布
- 攻略浏览
- 点赞功能

### 8. 餐厅模块 (RestaurantService)
- 餐厅搜索
- 餐厅详情查询
- 推荐餐厅

### 9. 商品模块 (ProductService)
- 商品列表
- 商品搜索
- 购物车管理

### 10. 目的地模块 (DestinationService)
- 目的地列表
- 目的地搜索
- 热力图数据

### 11. 行程模块 (TripPlanService)
- 行程创建和管理
- 行程更新和删除

### 12. 订单模块 (OrderService)
- 订单查询
- 订单取消

## API 使用示例

### 用户注册

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123",
    "phone": "13800138000"
  }'
```

### 用户登录

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'
```

### 搜索酒店

```bash
curl -X GET "http://localhost:8080/api/hotels/search?destination=北京&page=1&pageSize=10"
```

### 获取推荐酒店

```bash
curl -X GET "http://localhost:8080/api/hotels/recommended?limit=10"
```

### 搜索航班

```bash
curl -X GET "http://localhost:8080/api/flights/search?from=北京&to=上海&page=1&pageSize=10"
```

### 获取当前用户信息（需要认证）

```bash
curl -X GET http://localhost:8080/api/auth/user \
  -H "Authorization: Bearer <your_token>"
```

## 技术栈详解

### Spring Boot 3.2
- 最新的 Spring Boot 版本
- 支持 Java 17+ 的虚拟线程
- 改进的性能和安全性

### Spring Data JPA
- 简化数据库操作
- 自动生成 SQL 查询
- 支持分页和排序

### Spring Security + JWT
- 基于 Token 的认证
- 无状态的 API 设计
- 支持跨域请求

### MySQL 8.0
- 支持 JSON 数据类型
- 改进的性能
- 更好的字符集支持

## 常见问题

### Q: 如何修改 JWT 过期时间？
A: 编辑 `application.yml` 中的 `jwt.expiration` 参数（单位：毫秒）

### Q: 如何添加新的 API 端点？
A: 
1. 在 `entity` 中创建实体类
2. 在 `repository` 中创建 Repository 接口
3. 在 `service` 中创建 Service 类
4. 在 `controller` 中创建 Controller 类

### Q: 如何处理数据库连接错误？
A: 检查 `application.yml` 中的数据库配置，确保 MySQL 服务正在运行

### Q: 如何启用 SQL 日志？
A: 在 `application.yml` 中设置 `spring.jpa.show-sql: true`

## 下一步开发建议

1. **添加 Swagger 文档**
   - 集成 Springfox 或 Springdoc-OpenAPI
   - 自动生成 API 文档

2. **实现缓存**
   - 集成 Redis
   - 缓存热门数据

3. **添加日志系统**
   - 使用 SLF4J + Logback
   - 记录关键操作

4. **实现文件上传**
   - 支持图片上传
   - 集成 OSS 或本地存储

5. **添加支付功能**
   - 集成支付宝或微信支付
   - 订单支付流程

6. **实现消息队列**
   - 使用 RabbitMQ 或 Kafka
   - 异步处理订单

7. **添加单元测试**
   - 使用 JUnit 5 和 Mockito
   - 提高代码质量

8. **性能优化**
   - 数据库查询优化
   - 添加适当的索引
   - 实现分页查询

## 联系方式

- 邮箱: 1971283224@qq.com
- 电话: 195-7485-9056

## 许可证

© 2026 旅行有样 Inc. 保留所有权利。
