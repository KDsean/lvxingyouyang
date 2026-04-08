# 旅行有样 - 后端服务

基于 Spring Boot 3.2 + MySQL 的旅游网站后端 API 服务。

## 技术栈

- **框架**: Spring Boot 3.2.0
- **数据库**: MySQL 8.0+
- **ORM**: Spring Data JPA
- **安全**: Spring Security + JWT
- **构建工具**: Maven
- **Java 版本**: 17+

## 项目结构

```
back-end/
├── src/main/java/com/lvxingyouyang/
│   ├── controller/          # 控制层
│   │   ├── AuthController.java
│   │   ├── HotelController.java
│   │   ├── FlightController.java
│   │   ├── TrainController.java
│   │   ├── CarRentalController.java
│   │   ├── AttractionController.java
│   │   ├── GuideController.java
│   │   ├── RestaurantController.java
│   │   ├── ProductController.java
│   │   ├── DestinationController.java
│   │   ├── TripPlanController.java
│   │   └── OrderController.java
│   ├── service/             # 业务逻辑层
│   │   ├── AuthService.java
│   │   ├── HotelService.java
│   │   ├── FlightService.java
│   │   ├── TrainService.java
│   │   ├── CarRentalService.java
│   │   ├── AttractionService.java
│   │   ├── GuideService.java
│   │   ├── RestaurantService.java
│   │   ├── ProductService.java
│   │   ├── DestinationService.java
│   │   ├── TripPlanService.java
│   │   └── OrderService.java
│   ├── repository/          # 数据访问层
│   │   ├── UserRepository.java
│   │   ├── HotelRepository.java
│   │   ├── FlightRepository.java
│   │   ├── TrainRepository.java
│   │   ├── CarRentalRepository.java
│   │   ├── AttractionRepository.java
│   │   ├── GuideRepository.java
│   │   ├── RestaurantRepository.java
│   │   ├── ProductRepository.java
│   │   ├── DestinationRepository.java
│   │   ├── TripPlanRepository.java
│   │   └── OrderRepository.java
│   ├── entity/              # 实体类
│   │   ├── User.java
│   │   ├── Hotel.java
│   │   ├── Flight.java
│   │   ├── Train.java
│   │   ├── CarRental.java
│   │   ├── Attraction.java
│   │   ├── Guide.java
│   │   ├── Restaurant.java
│   │   ├── Product.java
│   │   ├── Destination.java
│   │   ├── TripPlan.java
│   │   └── Order.java
│   ├── dto/                 # 数据传输对象
│   │   ├── ApiResponse.java
│   │   ├── PageResponse.java
│   │   ├── LoginRequest.java
│   │   ├── LoginResponse.java
│   │   ├── RegisterRequest.java
│   │   └── UserDTO.java
│   ├── security/            # 安全相关
│   │   ├── JwtTokenProvider.java
│   │   └── JwtAuthenticationFilter.java
│   ├── config/              # 配置类
│   │   └── SecurityConfig.java
│   └── LvxingyouyangApplication.java
├── src/main/resources/
│   └── application.yml      # 应用配置
├── pom.xml                  # Maven 配置
└── README.md
```

## 快速开始

### 前置要求

- Java 17+
- Maven 3.6+
- MySQL 8.0+

### 安装步骤

1. **克隆项目**
```bash
cd back-end
```

2. **创建数据库**
```sql
CREATE DATABASE lvxingyouyang CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

3. **修改数据库配置**

编辑 `src/main/resources/application.yml`：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/lvxingyouyang?useSSL=false&serverTimezone=UTC
    username: root
    password: your_password
```

4. **编译项目**
```bash
mvn clean install
```

5. **运行应用**
```bash
mvn spring-boot:run
```

应用将在 `http://localhost:8080/api` 启动

## API 文档

### 认证模块 (`/auth`)

- `POST /auth/register` - 用户注册
- `POST /auth/login` - 用户登录
- `GET /auth/user` - 获取当前用户信息（需认证）
- `PUT /auth/profile` - 更新用户信息（需认证）
- `POST /auth/logout` - 登出

### 酒店模块 (`/hotels`)

- `GET /hotels/search` - 搜索酒店
- `GET /hotels/{id}` - 获取酒店详情
- `GET /hotels/recommended` - 获取推荐酒店
- `GET /hotels/popular` - 获取热门酒店
- `POST /hotels/book` - 预订酒店

### 机票模块 (`/flights`)

- `GET /flights/search` - 搜索航班
- `GET /flights/{id}` - 获取航班详情
- `GET /flights/popular-routes` - 获取热门航线
- `POST /flights/book` - 预订机票

### 火车模块 (`/trains`)

- `GET /trains/search` - 搜索火车票
- `GET /trains/{id}` - 获取火车详情
- `GET /trains/stations` - 获取车站列表
- `POST /trains/book` - 预订火车票

### 租车模块 (`/cars`)

- `GET /cars/search` - 搜索租车
- `GET /cars/{id}` - 获取租车详情
- `GET /cars/popular-locations` - 获取热门地点
- `POST /cars/book` - 预订租车

### 景点模块 (`/attractions`)

- `GET /attractions/search` - 搜索景点
- `GET /attractions/{id}` - 获取景点详情
- `GET /attractions/popular` - 获取热门景点
- `GET /attractions/recommended` - 获取推荐景点
- `POST /attractions/buy-ticket` - 购买门票

### 攻略模块 (`/guides`)

- `GET /guides` - 获取攻略列表
- `GET /guides/{id}` - 获取攻略详情
- `POST /guides` - 发布攻略
- `POST /guides/{id}/like` - 点赞攻略
- `GET /guides/popular` - 获取热门攻略
- `GET /guides/search` - 搜索攻略

### 餐厅模块 (`/restaurants`)

- `GET /restaurants/search` - 搜索餐厅
- `GET /restaurants/{id}` - 获取餐厅详情
- `GET /restaurants/popular` - 获取热门餐厅
- `GET /restaurants/recommended` - 获取推荐餐厅
- `POST /restaurants/book` - 预订餐厅

### 商品模块 (`/products`)

- `GET /products` - 获取商品列表
- `GET /products/{id}` - 获取商品详情
- `GET /products/popular` - 获取热门商品
- `GET /products/recommended` - 获取推荐商品
- `POST /products/cart/add` - 添加到购物车
- `GET /products/cart` - 获取购物车
- `POST /products/orders/create` - 创建订单

### 目的地模块 (`/destinations`)

- `GET /destinations` - 获取目的地列表
- `GET /destinations/{id}` - 获取目的地详情
- `GET /destinations/popular` - 获取热门目的地
- `GET /destinations/search` - 搜索目的地
- `GET /destinations/heatmap` - 获取热力图数据

### 行程模块 (`/trips`)

- `GET /trips` - 获取我的行程（需认证）
- `GET /trips/{id}` - 获取行程详情（需认证）
- `POST /trips` - 创建行程（需认证）
- `PUT /trips/{id}` - 更新行程（需认证）
- `DELETE /trips/{id}` - 删除行程（需认证）

### 订单模块 (`/orders`)

- `GET /orders` - 获取我的订单（需认证）
- `GET /orders/{id}` - 获取订单详情（需认证）
- `POST /orders/{id}/cancel` - 取消订单（需认证）

## 认证方式

使用 JWT Token 进行认证。登录后获取 token，在请求头中添加：

```
Authorization: Bearer <token>
```

## 响应格式

所有 API 响应统一格式：

```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

## 错误处理

- `200` - 成功
- `400` - 请求参数错误
- `401` - 未授权
- `403` - 禁止访问
- `404` - 资源不存在
- `500` - 服务器错误

## 环境变量配置

### application.yml

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/lvxingyouyang
    username: root
    password: root

jwt:
  secret: your-secret-key
  expiration: 86400000  # 24小时
```

## 开发建议

1. 完善异常处理和全局错误处理
2. 添加请求日志和审计日志
3. 实现缓存机制（Redis）
4. 添加单元测试和集成测试
5. 实现支付接口集成
6. 添加文件上传功能
7. 实现消息队列（RabbitMQ/Kafka）
8. 添加API文档（Swagger/OpenAPI）

## 联系方式

- 邮箱: 1971283224@qq.com
- 电话: 195-7485-9056

## 许可证

© 2026 旅行有样 Inc. 保留所有权利。
