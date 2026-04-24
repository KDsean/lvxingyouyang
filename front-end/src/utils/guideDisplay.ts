import type { Guide } from '@/types'

type GuideLike = Partial<Guide> & {
  authorName?: string
  authorAvatar?: string
}

const fallbackGuideImage = 'https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?auto=format&fit=crop&w=1200&q=80'

const guideImageRules: { keywords: string[]; image: string }[] = [
  {
    keywords: ['上海', '外滩', '浦东'],
    image: 'https://images.weserv.nl/?url=upload.wikimedia.org/wikipedia/commons/d/df/Pudong_Shanghai_November_2017_panorama.jpg&w=1200&h=800&fit=cover&output=jpg'
  },
  {
    keywords: ['杭州', '西湖', '灵隐'],
    image: 'https://images.weserv.nl/?url=upload.wikimedia.org/wikipedia/commons/0/07/20090524_Hangzhou_West_Lake_7531.jpg&w=1200&h=800&fit=cover&output=jpg'
  },
  {
    keywords: ['成都', '熊猫'],
    image: 'https://images.weserv.nl/?url=upload.wikimedia.org/wikipedia/commons/5/54/Chengdu-pandas-d10.jpg&w=1200&h=800&fit=cover&output=jpg'
  },
  {
    keywords: ['西安', '兵马俑', '长安'],
    image: 'https://images.weserv.nl/?url=upload.wikimedia.org/wikipedia/commons/3/38/Xian_China_Terracotta-Army-Museum-01.jpg&w=1200&h=800&fit=cover&output=jpg'
  },
  {
    keywords: ['大理', '洱海', '苍山'],
    image: 'https://images.weserv.nl/?url=upload.wikimedia.org/wikipedia/commons/4/44/Dali_Yunnan_China_Ornamented-Door-01.jpg&w=1200&h=800&fit=cover&output=jpg'
  },
  {
    keywords: ['丽江', '玉龙雪山'],
    image: 'https://images.weserv.nl/?url=https%3A%2F%2Fupload.wikimedia.org%2Fwikipedia%2Fcommons%2Fthumb%2Ff%2Ffb%2FLijiang_Yunnan_Old-town-01.jpg%2F1280px-Lijiang_Yunnan_Old-town-01.jpg&w=1200&h=800&fit=cover&output=jpg'
  },
  {
    keywords: ['北京', '故宫', '长城'],
    image: 'https://images.weserv.nl/?url=https%3A%2F%2Fupload.wikimedia.org%2Fwikipedia%2Fcommons%2F0%2F00%2FSunset_of_the_Forbidden_City_2006.JPG&w=1200&h=800&fit=cover&output=jpg'
  },
  {
    keywords: ['三亚', '亚龙湾', '海棠湾'],
    image: 'https://upload.wikimedia.org/wikipedia/commons/9/96/Beach_of_Sanya.jpg'
  },
  {
    keywords: ['厦门', '鼓浪屿'],
    image: 'https://images.weserv.nl/?url=upload.wikimedia.org/wikipedia/commons/b/b6/Gulangyu_Island_from_Zhongshan_Road%2C_Xiamen.jpg&w=1200&h=800&fit=cover&output=jpg'
  },
  {
    keywords: ['哈尔滨', '冰雪', '滑雪'],
    image: 'https://upload.wikimedia.org/wikipedia/commons/5/51/Harbin_Ice_and_Snow_Festival_2013.jpg'
  },
  {
    keywords: ['重庆', '洪崖洞'],
    image: 'https://images.weserv.nl/?url=upload.wikimedia.org/wikipedia/commons/8/86/Hongya_Cave_20180520.jpg&w=1200&h=800&fit=cover&output=jpg'
  },
  {
    keywords: ['广州', '广州塔'],
    image: 'https://images.weserv.nl/?url=https%3A%2F%2Fupload.wikimedia.org%2Fwikipedia%2Fcommons%2Fthumb%2F2%2F24%2FCanton_Tower_20241027.jpg%2F1280px-Canton_Tower_20241027.jpg&w=1200&h=800&fit=cover&output=jpg'
  },
  {
    keywords: ['深圳'],
    image: 'https://images.weserv.nl/?url=https%3A%2F%2Fupload.wikimedia.org%2Fwikipedia%2Fcommons%2Fthumb%2F9%2F91%2FSkyline_of_Shenzhen_from_Hong_Kong.jpg%2F1280px-Skyline_of_Shenzhen_from_Hong_Kong.jpg&w=1200&h=800&fit=cover&output=jpg'
  }
]

const isBadGuideImage = (url?: string) => {
  return !url || url.includes('placeholder.svg') || url.includes('picsum.photos') || url.includes('loremflickr.com')
}

export const getGuideImage = (guide: GuideLike) => {
  const text = `${guide.destination || ''} ${guide.title || ''} ${guide.content || ''}`
  const matched = guideImageRules.find(rule => rule.keywords.some(keyword => text.includes(keyword)))
  if (matched) return matched.image

  const fallback = guide.images?.[0]
  return isBadGuideImage(fallback) ? fallbackGuideImage : fallback || fallbackGuideImage
}

export const getGuideAuthor = (guide: GuideLike) => {
  return guide.author || {
    id: '',
    name: guide.authorName || '旅行达人',
    avatar: guide.authorAvatar || 'https://api.dicebear.com/7.x/initials/svg?seed=Travel'
  }
}
