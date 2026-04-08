// 图片工具函数 - 使用前端存储的图片
// 图片存放在 public/images/ 目录下

// 默认占位图（使用 picsum 避免 via.placeholder.com 被墙）
const DEFAULT_PLACEHOLDER = 'https://picsum.photos/seed/default/400/300'

/**
 * 绑定到 img 元素的 onerror 处理器，图片加载失败时自动回退到占位图
 * 用法：<img :src="url" @error="onImageError" />
 */
export const onImageError = (e: Event) => {
  const img = e.target as HTMLImageElement
  if (img && img.src !== DEFAULT_PLACEHOLDER) {
    img.src = DEFAULT_PLACEHOLDER
  }
}

/**
 * 检查 URL 是否来自已知失效/占位域名
 */
const isBrokenUrl = (url: string): boolean => {
  const brokenDomains = ['modao.cc', 'via.placeholder.com']
  return brokenDomains.some(domain => url.includes(domain))
}

// 获取图片路径，如果不存在则返回占位图
export const getImageUrl = (path: string, usePlaceholder: boolean = true) => {
  if (!path) {
    return usePlaceholder ? DEFAULT_PLACEHOLDER : ''
  }
  // 如果来自已知失效域名，直接用占位图
  if (isBrokenUrl(path)) {
    return DEFAULT_PLACEHOLDER
  }
  // 如果是完整URL，直接返回
  if (path.startsWith('http://') || path.startsWith('https://')) {
    return path
  }
  // 如果是相对路径，添加前缀
  return path.startsWith('/') ? path : `/${path}`
}

// 酒店图片
export const getHotelImage = (filename?: string) => {
  if (!filename) return DEFAULT_PLACEHOLDER
  return getImageUrl(`images/hotels/${filename}`)
}

// 景点图片
export const getAttractionImage = (filename?: string) => {
  if (!filename) return DEFAULT_PLACEHOLDER
  return getImageUrl(`images/attractions/${filename}`)
}

// 餐厅图片
export const getRestaurantImage = (filename?: string) => {
  if (!filename) return DEFAULT_PLACEHOLDER
  return getImageUrl(`images/restaurants/${filename}`)
}

// 目的地图片
export const getDestinationImage = (filename?: string) => {
  if (!filename) return DEFAULT_PLACEHOLDER
  return getImageUrl(`images/destinations/${filename}`)
}

// 攻略图片
export const getGuideImage = (filename?: string) => {
  if (!filename) return DEFAULT_PLACEHOLDER
  return getImageUrl(`images/guides/${filename}`)
}

// 商品图片
export const getProductImage = (filename?: string) => {
  if (!filename) return DEFAULT_PLACEHOLDER
  return getImageUrl(`images/products/${filename}`)
}

// 用户头像
export const getAvatarImage = (filename?: string) => {
  if (!filename) return DEFAULT_PLACEHOLDER
  return getImageUrl(`images/avatars/${filename}`)
}

// 车辆图片
export const getCarImage = (filename?: string) => {
  if (!filename) return DEFAULT_PLACEHOLDER
  return getImageUrl(`images/cars/${filename}`)
}

// 通用占位图生成器（用于开发阶段）
export const getPlaceholderImage = (width: number = 400, height: number = 300, text?: string) => {
  const displayText = text || `${width}x${height}`
  return `https://picsum.photos/seed/${encodeURIComponent(displayText)}/${width}/${height}`
}
