# 🔧 前端所有接口启用 API 调用 - 完整指南

## 📋 需要修改的文件列表

以下是所有需要启用 API 调用的前端视图文件：

| 文件 | 模块 | API 函数 | 状态 |
|------|------|---------|------|
| Hotels.vue | 酒店 | searchHotels | ✅ 已修复 |
| Flights.vue | 航班 | searchFlights | ⏳ 需要修复 |
| Trains.vue | 火车 | searchTrains | ⏳ 需要修复 |
| Cars.vue | 租车 | searchCars | ⏳ 需要修复 |
| Attractions.vue | 景点 | searchAttractions | ⏳ 需要修复 |
| Restaurants.vue | 餐厅 | searchRestaurants | ⏳ 需要修复 |
| Guides.vue | 攻略 | searchGuides | ⏳ 需要修复 |
| Shopping.vue | 购物 | searchProducts | ⏳ 需要修复 |
| Destinations.vue | 目的地 | searchDestinations | ⏳ 需要修复 |

---

## 🔧 修复方案

### 方案 1：使用 VS Code 批量替换（推荐）

#### 步骤 1：打开查找和替换
- 按 `Ctrl+H` 打开查找和替换
- 启用正则表达式（点击 `.*` 按钮）

#### 步骤 2：替换 handleSearch 函数

**查找：**
```
const handleSearch = \(\) => \{\s+try \{\s+\/\/ const res = await search
```

**替换为：**
```
const handleSearch = async () => {
  try {
    console.log('开始搜索...')
    const res = await search
```

#### 步骤 3：启用 API 调用

**查找：**
```
\/\/ const res = await search(.+?)\n\s+\/\/ (.+?)\.value = res\.data
```

**替换为：**
```
const res = await search$1
    console.log('搜索结果:', res)
    $2.value = res.data
```

---

### 方案 2：手动逐个修复（更安全）

#### Flights.vue 修复

找到 `handleSearch` 函数，替换为：

```typescript
const handleSearch = async () => {
  try {
    console.log('开始搜索航班...')
    const params = {
      from: searchParams.from,
      to: searchParams.to,
      date: searchParams.date,
      returnDate: searchParams.returnDate,
      cabinClass: searchParams.cabinClass,
      passengers: searchParams.passengers,
      tripType: tripType.value
    }
    
    console.log('搜索参数:', params)
    const res = await searchFlights(params)
    console.log('搜索结果:', res)
    flights.value = res.data
    
    ElMessage.success('搜索成功')
  } catch (error) {
    console.error('搜索错误:', error)
    ElMessage.error('搜索失败，请稍后重试')
  }
}
```

并在顶部添加导入：
```typescript
import { searchFlights } from '@/api/modules/flight'
import { onMounted } from 'vue'
```

在 `onMounted` 中添加：
```typescript
onMounted(() => {
  console.log('航班页面已加载，准备获取数据...')
  const today = new Date()
  searchParams.date = today
})
```

#### Trains.vue 修复

```typescript
const handleSearch = async () => {
  try {
    console.log('开始搜索火车...')
    const params = {
      from: searchParams.from,
      to: searchParams.to,
      date: searchParams.date,
      seatType: searchParams.seatType
    }
    
    console.log('搜索参数:', params)
    const res = await searchTrains(params)
    console.log('搜索结果:', res)
    trains.value = res.data
    
    ElMessage.success('搜索成功')
  } catch (error) {
    console.error('搜索错误:', error)
    ElMessage.error('搜索失败，请稍后重试')
  }
}
```

#### Cars.vue 修复

```typescript
const handleSearch = async () => {
  try {
    console.log('开始搜索租车...')
    const params = {
      location: searchParams.location,
      pickupDate: searchParams.pickupDate,
      returnDate: searchParams.returnDate,
      carType: searchParams.carType
    }
    
    console.log('搜索参数:', params)
    const res = await searchCars(params)
    console.log('搜索结果:', res)
    cars.value = res.data
    
    ElMessage.success('搜索成功')
  } catch (error) {
    console.error('搜索错误:', error)
    ElMessage.error('搜索失败，请稍后重试')
  }
}
```

#### Attractions.vue 修复

```typescript
const handleSearch = async () => {
  try {
    console.log('开始搜索景点...')
    const params = {
      city: searchParams.city,
      type: searchParams.type,
      sortBy: sortBy.value
    }
    
    console.log('搜索参数:', params)
    const res = await searchAttractions(params)
    console.log('搜索结果:', res)
    attractions.value = res.data
    hotAttractions.value = res.data.slice(0, 3)
    
    ElMessage.success('搜索成功')
  } catch (error) {
    console.error('搜索错误:', error)
    ElMessage.error('搜索失败，请稍后重试')
  }
}

onMounted(() => {
  console.log('景点页面已加载，准备获取数据...')
  handleSearch()
})
```

#### Restaurants.vue 修复

```typescript
const handleSearch = async () => {
  try {
    console.log('开始搜索餐厅...')
    const params = {
      city: searchParams.city,
      cuisine: searchParams.cuisine,
      priceLevel: searchParams.priceLevel,
      sortBy: sortBy.value
    }
    
    console.log('搜索参数:', params)
    const res = await searchRestaurants(params)
    console.log('搜索结果:', res)
    restaurants.value = res.data
    michelinRestaurants.value = res.data.slice(0, 3)
    
    ElMessage.success('搜索成功')
  } catch (error) {
    console.error('搜索错误:', error)
    ElMessage.error('搜索失败，请稍后重试')
  }
}

onMounted(() => {
  console.log('餐厅页面已加载，准备获取数据...')
  handleSearch()
})
```

#### Guides.vue 修复

```typescript
const handleSearch = async () => {
  try {
    console.log('开始搜索攻略...')
    const params = {
      destination: searchParams.destination,
      keyword: searchParams.keyword,
      sortBy: sortBy.value
    }
    
    console.log('搜索参数:', params)
    const res = await searchGuides(params)
    console.log('搜索结果:', res)
    guides.value = res.data
    
    ElMessage.success('搜索成功')
  } catch (error) {
    console.error('搜索错误:', error)
    ElMessage.error('搜索失败，请稍后重试')
  }
}

onMounted(() => {
  console.log('攻略页面已加载，准备获取数据...')
  handleSearch()
})
```

#### Shopping.vue 修复

```typescript
const handleSearch = async () => {
  try {
    console.log('开始搜索商品...')
    const params = {
      category: searchParams.category,
      keyword: searchParams.keyword,
      sortBy: sortBy.value
    }
    
    console.log('搜索参数:', params)
    const res = await searchProducts(params)
    console.log('搜索结果:', res)
    products.value = res.data
    
    ElMessage.success('搜索成功')
  } catch (error) {
    console.error('搜索错误:', error)
    ElMessage.error('搜索失败，请稍后重试')
  }
}

onMounted(() => {
  console.log('购物页面已加载，准备获取数据...')
  handleSearch()
})
```

#### Destinations.vue 修复

```typescript
const handleSearch = async () => {
  try {
    console.log('开始搜索目的地...')
    const params = {
      country: searchParams.country,
      keyword: searchParams.keyword,
      sortBy: sortBy.value
    }
    
    console.log('搜索参数:', params)
    const res = await searchDestinations(params)
    console.log('搜索结果:', res)
    destinations.value = res.data
    
    ElMessage.success('搜索成功')
  } catch (error) {
    console.error('搜索错误:', error)
    ElMessage.error('搜索失败，请稍后重试')
  }
}

onMounted(() => {
  console.log('目的地页面已加载，准备获取数据...')
  handleSearch()
})
```

---

## ✅ 修复后的验证

### 步骤 1：重启前端服务

```bash
cd front-end
npm run dev
```

### 步骤 2：打开浏览器验证

访问各个页面：
- `http://localhost:3000/hotels`
- `http://localhost:3000/flights`
- `http://localhost:3000/trains`
- `http://localhost:3000/cars`
- `http://localhost:3000/attractions`
- `http://localhost:3000/restaurants`
- `http://localhost:3000/guides`
- `http://localhost:3000/shopping`
- `http://localhost:3000/destinations`

### 步骤 3：打开浏览器开发者工具

1. 按 `F12` 打开开发者工具
2. 点击 **Console** 标签
3. 应该能看到类似的日志：
```
页面已加载，准备获取数据...
开始搜索...
搜索参数: {...}
发送请求: /api/... GET
收到响应: {code: 200, ...}
搜索结果: {...}
搜索成功
```

### 步骤 4：检查 Network 标签

1. 点击 **Network** 标签
2. 刷新页面
3. 应该能看到 `/api/...` 的请求
4. 状态码应该是 **200**

---

## 📝 修复清单

- [ ] Hotels.vue - ✅ 已修复
- [ ] Flights.vue - ⏳ 需要修复
- [ ] Trains.vue - ⏳ 需要修复
- [ ] Cars.vue - ⏳ 需要修复
- [ ] Attractions.vue - ⏳ 需要修复
- [ ] Restaurants.vue - ⏳ 需要修复
- [ ] Guides.vue - ⏳ 需要修复
- [ ] Shopping.vue - ⏳ 需要修复
- [ ] Destinations.vue - ⏳ 需要修复

---

## 🎯 关键修改点

### 1. 导入必要的函数

```typescript
import { searchHotels } from '@/api/modules/hotel'
import { onMounted } from 'vue'
```

### 2. 启用 API 调用

```typescript
// 从注释改为实际调用
const res = await searchHotels(params)
hotels.value = res.data
```

### 3. 添加调试日志

```typescript
console.log('开始搜索...')
console.log('搜索参数:', params)
console.log('搜索结果:', res)
```

### 4. 页面加载时自动获取数据

```typescript
onMounted(() => {
  console.log('页面已加载，准备获取数据...')
  handleSearch()
})
```

---

## 🚀 快速完成

所有修改完成后，重启前端服务：

```bash
cd front-end
npm run dev
```

然后访问各个页面，应该能看到数据正常加载！

---

**所有前端接口都将启用 API 调用！** ✅
