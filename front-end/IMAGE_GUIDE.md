# 前端图片使用说明

## 图片存储位置

所有图片存放在 `public/images/` 目录下，按类型分类：

```
public/
└── images/
    ├── hotels/          # 酒店图片
    ├── attractions/     # 景点图片
    ├── destinations/    # 目的地图片
    ├── restaurants/     # 餐厅图片
    ├── guides/          # 攻略图片
    ├── products/        # 商品图片
    ├── cars/            # 车辆图片
    └── avatars/         # 用户头像
```

## 如何添加图片

1. 将图片文件放入对应的文件夹
2. 建议使用有意义的文件名，例如：
   - `sanya-hotel-1.jpg`
   - `beijing-forbidden-city.jpg`
   - `shanghai-restaurant.jpg`

## 在代码中使用图片

### 方法一：使用工具函数（推荐）

```vue
<script setup lang="ts">
import { getHotelImage, getAttractionImage } from '@/utils/image'
</script>

<template>
  <!-- 酒店图片 -->
  <img :src="getHotelImage('sanya-hotel-1.jpg')" alt="三亚酒店" />
  
  <!-- 景点图片 -->
  <img :src="getAttractionImage('forbidden-city.jpg')" alt="故宫" />
  
  <!-- 如果没有图片，会显示占位图 -->
  <img :src="getHotelImage()" alt="默认占位图" />
</template>
```

### 方法二：直接使用路径

```vue
<template>
  <img src="/images/hotels/sanya-hotel-1.jpg" alt="三亚酒店" />
</template>
```

## 可用的图片函数

- `getHotelImage(filename)` - 酒店图片
- `getAttractionImage(filename)` - 景点图片
- `getRestaurantImage(filename)` - 餐厅图片
- `getDestinationImage(filename)` - 目的地图片
- `getGuideImage(filename)` - 攻略图片
- `getProductImage(filename)` - 商品图片
- `getCarImage(filename)` - 车辆图片
- `getAvatarImage(filename)` - 用户头像

## 开发阶段占位图

如果还没有准备好图片，可以使用占位图：

```vue
<script setup lang="ts">
import { getPlaceholderImage } from '@/utils/image'
</script>

<template>
  <!-- 生成 800x600 的占位图，显示文字 "酒店图片" -->
  <img :src="getPlaceholderImage(800, 600, '酒店图片')" alt="占位图" />
</template>
```

## 图片命名建议

1. 使用小写字母和连字符
2. 使用有意义的名称
3. 包含类型和序号

示例：
- `sanya-hotel-1.jpg`
- `sanya-hotel-2.jpg`
- `beijing-forbidden-city.jpg`
- `shanghai-bund-hotel.jpg`
- `chengdu-hotpot-restaurant.jpg`

## 图片格式建议

- 使用 `.jpg` 格式（照片）
- 使用 `.png` 格式（图标、透明背景）
- 使用 `.webp` 格式（更小的文件大小）

## 图片尺寸建议

- 酒店图片：800x600 或 1200x800
- 景点图片：800x600 或 1200x800
- 目的地横幅：1920x600
- 商品图片：400x400 或 600x600
- 用户头像：200x200

## 优化建议

1. 压缩图片以减小文件大小
2. 使用适当的分辨率（不要过大）
3. 考虑使用图片懒加载
4. 为不同设备准备不同尺寸的图片

## 示例：更新首页图片

假设你已经准备好了以下图片：
- `public/images/destinations/hero-banner.jpg` - 首页横幅
- `public/images/hotels/sanya-hotel.jpg` - 三亚酒店
- `public/images/hotels/shanghai-hotel.jpg` - 上海酒店

在 `Home.vue` 中使用：

```vue
<script setup lang="ts">
import { getDestinationImage, getHotelImage } from '@/utils/image'
</script>

<template>
  <!-- 首页横幅 -->
  <img :src="getDestinationImage('hero-banner.jpg')" alt="旅行有样" />
  
  <!-- 酒店图片 -->
  <img :src="getHotelImage('sanya-hotel.jpg')" alt="三亚酒店" />
  <img :src="getHotelImage('shanghai-hotel.jpg')" alt="上海酒店" />
</template>
```

## 注意事项

1. `public` 文件夹中的文件会被直接复制到构建输出目录
2. 图片路径以 `/` 开头表示从根目录开始
3. 不需要在路径中包含 `public`，直接从 `images` 开始
4. 如果图片不存在，会显示默认占位图
