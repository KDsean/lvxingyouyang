# 旅行有样 - 旅游网站前端项目

一个基于 Vue 3 + TypeScript + Vite 的现代化旅游网站前端项目。

## 技术栈

- **框架**: Vue 3 + TypeScript
- **构建工具**: Vite
- **UI 框架**: Element Plus + TailwindCSS
- **状态管理**: Pinia
- **路由**: Vue Router
- **HTTP 客户端**: Axios

## 项目结构

```
front-end/
├── public/                  # 静态资源
├── src/
│   ├── api/                # API 接口层
│   │   ├── modules/        # 各模块 API
│   │   │   ├── auth.ts     # 认证相关
│   │   │   ├── hotel.ts    # 酒店相关
│   │   │   ├── flight.ts   # 机票相关
│   │   │   ├── train.ts    # 火车票相关
│   │   │   ├── car.ts      # 租车相关
│   │   │   ├── attraction.ts # 景点相关
│   │   │   ├── guide.ts    # 攻略相关
│   │   │   ├── restaurant.ts # 美食相关
│   │   │   ├── shopping.ts # 购物相关
│   │   │   ├── destination.ts # 目的地相关
│   │   │   ├── ai.ts       # AI 助手相关
│   │   │   └── user.ts     # 用户相关
│   │   ├── request.ts      # Axios 封装
│   │   └── types.ts        # API 类型定义
│   ├── assets/             # 资源文件
│   ├── components/         # 公共组件
│   │   ├── common/         # 通用组件
│   │   │   ├── NavBar.vue  # 导航栏
│   │   │   ├── Footer.vue  # 页脚
│   │   │   └── Sidebar.vue # 侧边栏
│   │   └── business/       # 业务组件
│   ├── composables/        # 组合式函数
│   ├── layouts/            # 布局组件
│   │   └── DefaultLayout.vue
│   ├── router/             # 路由配置
│   │   └── index.ts
│   ├── stores/             # 状态管理
│   │   ├── user.ts         # 用户状态
│   │   └── search.ts       # 搜索状态
│   ├── styles/             # 全局样式
│   │   └── index.css
│   ├── types/              # TypeScript 类型
│   │   └── index.ts
│   ├── utils/              # 工具函数
│   ├── views/              # 页面组件
│   │   ├── Home.vue        # 首页
│   │   ├── Hotels.vue      # 酒店列表
│   │   ├── HotelDetail.vue # 酒店详情
│   │   ├── Flights.vue     # 机票预订
│   │   ├── Trains.vue      # 火车票
│   │   ├── Cars.vue        # 租车自驾
│   │   ├── Attractions.vue # 景点门票
│   │   ├── AttractionDetail.vue # 景点详情
│   │   ├── Guides.vue      # 当地攻略
│   │   ├── GuideDetail.vue # 攻略详情
│   │   ├── Restaurants.vue # 美食林
│   │   ├── RestaurantDetail.vue # 餐厅详情
│   │   ├── Shopping.vue    # 全球购
│   │   ├── Destinations.vue # 目的地探索
│   │   ├── Planning.vue    # AI 行程规划
│   │   ├── History.vue     # 历史规划
│   │   ├── Login.vue       # 登录
│   │   ├── Register.vue    # 注册
│   │   ├── Profile.vue     # 个人中心
│   │   └── NotFound.vue    # 404 页面
│   ├── App.vue
│   └── main.ts
├── .env.development        # 开发环境变量
├── .env.production         # 生产环境变量
├── index.html
├── package.json
├── postcss.config.cjs
├── tailwind.config.js
├── tsconfig.json
├── vite.config.ts
└── README.md
```

## 功能模块

### 1. 首页
- Hero 横幅展示
- 精选酒店推荐
- AI 行程助手入口
- 全球目的地热力图
- 酒店等级对比表格

### 2. 八大服务模块
- **酒店住宿**: 搜索、筛选、预订酒店
- **机票预订**: 单程/往返机票搜索预订
- **火车票**: 车次查询、座位选择
- **租车自驾**: 车型选择、租车预订
- **景点门票**: 景点浏览、门票购买
- **当地攻略**: 旅游攻略浏览、发布
- **美食林**: 餐厅搜索、座位预订
- **全球购**: 特产商品购买

### 3. 目的地探索
- 热门目的地展示
- 按地区浏览
- 当季推荐

### 4. AI 行程规划
- 智能对话式规划
- 快速规划表单
- 行程生成与优化

### 5. 用户系统
- 登录/注册
- 个人信息管理
- 订单管理
- 历史行程

## 开发指南

### 安装依赖

```bash
npm install
```

### 启动开发服务器

```bash
npm run dev
```

访问 http://localhost:3000

### 构建生产版本

```bash
npm run build
```

### 预览生产构建

```bash
npm run preview
```

## 环境变量

### 开发环境 (.env.development)
```
VITE_API_BASE_URL=http://localhost:8080/api
VITE_AI_API_URL=http://localhost:8080/ai
```

### 生产环境 (.env.production)
```
VITE_API_BASE_URL=https://api.lvxingyouyang.com/api
VITE_AI_API_URL=https://api.lvxingyouyang.com/ai
```

## API 接口说明

所有 API 接口都在 `src/api/modules/` 目录下，按功能模块划分：

- `auth.ts`: 用户认证（登录、注册、登出）
- `hotel.ts`: 酒店相关接口
- `flight.ts`: 机票相关接口
- `train.ts`: 火车票相关接口
- `car.ts`: 租车相关接口
- `attraction.ts`: 景点相关接口
- `guide.ts`: 攻略相关接口
- `restaurant.ts`: 餐厅相关接口
- `shopping.ts`: 购物相关接口
- `destination.ts`: 目的地相关接口
- `ai.ts`: AI 助手相关接口
- `user.ts`: 用户信息相关接口

## 状态管理

使用 Pinia 进行状态管理：

- `useUserStore`: 用户状态（登录信息、用户数据）
- `useSearchStore`: 搜索状态（各模块搜索参数）

## 路由配置

路由采用懒加载方式，提高首屏加载速度。主要路由包括：

- `/`: 首页
- `/hotels`: 酒店列表
- `/flights`: 机票预订
- `/trains`: 火车票
- `/cars`: 租车
- `/attractions`: 景点门票
- `/guides`: 攻略
- `/restaurants`: 美食
- `/shopping`: 购物
- `/destinations`: 目的地探索
- `/planning`: AI 行程规划
- `/history`: 历史规划（需登录）
- `/profile`: 个人中心（需登录）
- `/login`: 登录
- `/register`: 注册

## 注意事项

1. 当前项目使用模拟数据，实际使用时需要连接后端 API
2. AI 对话功能需要配置后端 AI 服务
3. 图片资源使用占位图，实际使用时需要替换真实图片
4. 支付功能需要接入第三方支付平台

## 后续开发建议

1. 完善各详情页面的功能
2. 实现真实的 API 对接
3. 添加更多的交互动画
4. 优化移动端适配
5. 添加单元测试
6. 实现 PWA 功能
7. 添加国际化支持

## 联系方式

- 邮箱: 1971283224@qq.com
- 电话: 195-7485-9056
- 地址: 广东省湛江市麻章区湖光镇教育城新坡路1号致远楼众创空间

## 许可证

© 2026 旅行有样 Inc. 保留所有权利。
