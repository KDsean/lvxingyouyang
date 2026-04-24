import type { Attraction, CarRental, Hotel, Product, Restaurant } from '@/types'

const fallbackImage = 'https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?auto=format&fit=crop&w=1200&q=80'

const hasBadImage = (url?: string) => !url || url.includes('placeholder.svg') || url.includes('picsum.photos') || url.includes('loremflickr.com')

const firstImage = (images?: string[]) => images?.[0]

const includesAny = (text: string, keywords: string[]) => {
  return keywords.some(keyword => text.includes(keyword))
}

const hotelCityImages: Record<string, string> = {
  北京: 'https://images.unsplash.com/photo-1542314831-068cd1dbfeeb?auto=format&fit=crop&w=1200&q=80',
  上海: 'https://images.unsplash.com/photo-1582719508461-905c673771fd?auto=format&fit=crop&w=1200&q=80',
  杭州: 'https://images.unsplash.com/photo-1445019980597-93fa8acb246c?auto=format&fit=crop&w=1200&q=80',
  成都: 'https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?auto=format&fit=crop&w=1200&q=80',
  西安: 'https://images.unsplash.com/photo-1564501049412-61c2a3083791?auto=format&fit=crop&w=1200&q=80',
  大理: 'https://images.unsplash.com/photo-1551882547-ff40c63fe5fa?auto=format&fit=crop&w=1200&q=80',
  丽江: 'https://images.unsplash.com/photo-1590490360182-c33d57733427?auto=format&fit=crop&w=1200&q=80',
  三亚: 'https://images.unsplash.com/photo-1566073771259-6a8506099945?auto=format&fit=crop&w=1200&q=80',
  重庆: 'https://images.unsplash.com/photo-1551632436-cbf8dd35adfa?auto=format&fit=crop&w=1200&q=80',
  厦门: 'https://images.unsplash.com/photo-1521783988139-89397d761dce?auto=format&fit=crop&w=1200&q=80',
  广州: 'https://images.unsplash.com/photo-1571896349842-33c89424de2d?auto=format&fit=crop&w=1200&q=80',
  深圳: 'https://images.unsplash.com/photo-1568084680786-a84f91d1153c?auto=format&fit=crop&w=1200&q=80'
}

export const getHotelImage = (hotel: Partial<Hotel>) => {
  const fallback = firstImage(hotel.images)

  const text = `${hotel.name || ''} ${hotel.city || ''} ${hotel.location || ''}`
  const cityKey = Object.keys(hotelCityImages).find(city => text.includes(city))
  if (cityKey) return hotelCityImages[cityKey]

  if (!hasBadImage(fallback)) return fallback || fallbackImage
  if (includesAny(text, ['三亚', '海棠', '海景', '海边'])) return 'https://images.unsplash.com/photo-1566073771259-6a8506099945?auto=format&fit=crop&w=1200&q=80'
  if (includesAny(text, ['上海', '外滩', '江景'])) return 'https://images.unsplash.com/photo-1582719508461-905c673771fd?auto=format&fit=crop&w=1200&q=80'
  if (includesAny(text, ['丽江', '古城', '雪山'])) return 'https://images.unsplash.com/photo-1590490360182-c33d57733427?auto=format&fit=crop&w=1200&q=80'
  if (includesAny(text, ['大理'])) return 'https://images.unsplash.com/photo-1551882547-ff40c63fe5fa?auto=format&fit=crop&w=1200&q=80'
  if (includesAny(text, ['北京'])) return 'https://images.unsplash.com/photo-1542314831-068cd1dbfeeb?auto=format&fit=crop&w=1200&q=80'
  return 'https://images.unsplash.com/photo-1542314831-068cd1dbfeeb?auto=format&fit=crop&w=1200&q=80'
}

export const getAttractionImage = (attraction: Partial<Attraction>) => {
  const text = `${attraction.name || ''} ${attraction.city || ''} ${attraction.type || ''}`

  if (includesAny(text, ['故宫博物院', '故宫'])) return 'https://images.weserv.nl/?url=https%3A%2F%2Fupload.wikimedia.org%2Fwikipedia%2Fcommons%2F0%2F00%2FSunset_of_the_Forbidden_City_2006.JPG&w=1200&h=800&fit=cover&output=jpg'
  if (includesAny(text, ['长城', '八达岭'])) return 'https://images.unsplash.com/photo-1508804185872-d7badad00f7d?auto=format&fit=crop&w=1200&q=80'
  if (includesAny(text, ['西湖'])) return 'https://images.weserv.nl/?url=upload.wikimedia.org/wikipedia/commons/0/07/20090524_Hangzhou_West_Lake_7531.jpg&w=1200&h=800&fit=cover&output=jpg'
  if (includesAny(text, ['九寨沟'])) return 'https://images.unsplash.com/photo-1750751829894-850e9d23b3d5?auto=format&fit=crop&w=1200&q=80'
  if (includesAny(text, ['兵马俑'])) return 'https://images.weserv.nl/?url=upload.wikimedia.org/wikipedia/commons/3/38/Xian_China_Terracotta-Army-Museum-01.jpg&w=1200&h=800&fit=cover&output=jpg'
  if (includesAny(text, ['张家界'])) return 'https://images.weserv.nl/?url=https%3A%2F%2Fupload.wikimedia.org%2Fwikipedia%2Fcommons%2Fthumb%2Fc%2Fce%2FZhangjiajie_National_Forest_Park_38099-Zhangjiajie_%252848757253473%2529.jpg%2F1280px-Zhangjiajie_National_Forest_Park_38099-Zhangjiajie_%252848757253473%2529.jpg&w=1200&h=800&fit=cover&output=jpg'
  if (includesAny(text, ['布达拉宫'])) return 'https://images.weserv.nl/?url=https%3A%2F%2Fupload.wikimedia.org%2Fwikipedia%2Fcommons%2Fthumb%2F4%2F4f%2FPotala.jpg%2F1280px-Potala.jpg&w=1200&h=800&fit=cover&output=jpg'
  if (includesAny(text, ['黄山'])) return 'https://images.unsplash.com/photo-1724380231024-3f6f11b4997d?auto=format&fit=crop&w=1200&q=80'
  if (includesAny(text, ['丽江古城'])) return 'https://images.weserv.nl/?url=https%3A%2F%2Fupload.wikimedia.org%2Fwikipedia%2Fcommons%2Fthumb%2Ff%2Ffb%2FLijiang_Yunnan_Old-town-01.jpg%2F1280px-Lijiang_Yunnan_Old-town-01.jpg&w=1200&h=800&fit=cover&output=jpg'
  if (includesAny(text, ['鼓浪屿'])) return 'https://images.weserv.nl/?url=upload.wikimedia.org/wikipedia/commons/b/b6/Gulangyu_Island_from_Zhongshan_Road%2C_Xiamen.jpg&w=1200&h=800&fit=cover&output=jpg'

  const fallback = firstImage(attraction.images)
  if (!hasBadImage(fallback)) return fallback || fallbackImage

  if (includesAny(text, ['北京', '故宫', '博物馆'])) return 'https://images.weserv.nl/?url=https%3A%2F%2Fupload.wikimedia.org%2Fwikipedia%2Fcommons%2Fe%2Fef%2FThe_Forbidden_City_-_View_from_Coal_Hill.jpg&w=1200&h=800&fit=cover&output=jpg'
  if (includesAny(text, ['上海'])) return 'https://images.weserv.nl/?url=upload.wikimedia.org/wikipedia/commons/d/df/Pudong_Shanghai_November_2017_panorama.jpg&w=1200&h=800&fit=cover&output=jpg'
  if (includesAny(text, ['杭州'])) return 'https://images.weserv.nl/?url=upload.wikimedia.org/wikipedia/commons/0/07/20090524_Hangzhou_West_Lake_7531.jpg&w=1200&h=800&fit=cover&output=jpg'
  if (includesAny(text, ['成都'])) return 'https://images.weserv.nl/?url=upload.wikimedia.org/wikipedia/commons/5/54/Chengdu-pandas-d10.jpg&w=1200&h=800&fit=cover&output=jpg'
  if (includesAny(text, ['西安'])) return 'https://images.weserv.nl/?url=upload.wikimedia.org/wikipedia/commons/3/38/Xian_China_Terracotta-Army-Museum-01.jpg&w=1200&h=800&fit=cover&output=jpg'
  if (includesAny(text, ['大理'])) return 'https://images.weserv.nl/?url=upload.wikimedia.org/wikipedia/commons/4/44/Dali_Yunnan_China_Ornamented-Door-01.jpg&w=1200&h=800&fit=cover&output=jpg'
  if (includesAny(text, ['丽江'])) return 'https://images.weserv.nl/?url=https%3A%2F%2Fupload.wikimedia.org%2Fwikipedia%2Fcommons%2Fthumb%2Ff%2Ffb%2FLijiang_Yunnan_Old-town-01.jpg%2F1280px-Lijiang_Yunnan_Old-town-01.jpg&w=1200&h=800&fit=cover&output=jpg'
  if (includesAny(text, ['三亚'])) return 'https://images.weserv.nl/?url=https%3A%2F%2Fupload.wikimedia.org%2Fwikipedia%2Fcommons%2Fthumb%2Fe%2Feb%2FSanya_Beach_Night_-_panoramio.jpg%2F1280px-Sanya_Beach_Night_-_panoramio.jpg&w=1200&h=800&fit=cover&output=jpg'
  if (includesAny(text, ['重庆'])) return 'https://images.weserv.nl/?url=upload.wikimedia.org/wikipedia/commons/8/86/Hongya_Cave_20180520.jpg&w=1200&h=800&fit=cover&output=jpg'
  if (includesAny(text, ['厦门'])) return 'https://images.weserv.nl/?url=upload.wikimedia.org/wikipedia/commons/b/b6/Gulangyu_Island_from_Zhongshan_Road%2C_Xiamen.jpg&w=1200&h=800&fit=cover&output=jpg'
  if (includesAny(text, ['广州'])) return 'https://images.weserv.nl/?url=https%3A%2F%2Fupload.wikimedia.org%2Fwikipedia%2Fcommons%2Fthumb%2F2%2F24%2FCanton_Tower_20241027.jpg%2F1280px-Canton_Tower_20241027.jpg&w=1200&h=800&fit=cover&output=jpg'
  if (includesAny(text, ['深圳'])) return 'https://images.weserv.nl/?url=https%3A%2F%2Fupload.wikimedia.org%2Fwikipedia%2Fcommons%2Fthumb%2F9%2F91%2FSkyline_of_Shenzhen_from_Hong_Kong.jpg%2F1280px-Skyline_of_Shenzhen_from_Hong_Kong.jpg&w=1200&h=800&fit=cover&output=jpg'
  return 'https://images.weserv.nl/?url=https%3A%2F%2Fupload.wikimedia.org%2Fwikipedia%2Fcommons%2Fe%2Fef%2FThe_Forbidden_City_-_View_from_Coal_Hill.jpg&w=1200&h=800&fit=cover&output=jpg'
}

export const getRestaurantImage = (restaurant: Partial<Restaurant>) => {
  const fallback = firstImage(restaurant.images)
  if (!hasBadImage(fallback)) return fallback || fallbackImage

  const text = `${restaurant.name || ''} ${restaurant.cuisine || ''} ${(restaurant.specialties || []).join(' ')}`
  if (includesAny(text, ['烤鸭', '京菜'])) return 'https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?auto=format&fit=crop&w=1200&q=80'
  if (includesAny(text, ['川菜', '火锅', 'sichuan', 'hotpot'])) return 'https://images.unsplash.com/photo-1551218808-94e220e084d2?auto=format&fit=crop&w=1200&q=80'
  if (includesAny(text, ['粤菜', 'cantonese'])) return 'https://images.unsplash.com/photo-1559847844-5315695dadae?auto=format&fit=crop&w=1200&q=80'
  if (includesAny(text, ['日料', 'japanese'])) return 'https://images.unsplash.com/photo-1553621042-f6e147245754?auto=format&fit=crop&w=1200&q=80'
  if (includesAny(text, ['西餐', 'western'])) return 'https://images.unsplash.com/photo-1559339352-11d035aa65de?auto=format&fit=crop&w=1200&q=80'
  return 'https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?auto=format&fit=crop&w=1200&q=80'
}

export const getProductImage = (product: Partial<Product>) => {
  const text = `${product.name || ''} ${product.category || ''} ${(product.tags || []).join(' ')}`
  const wikiProductImage = (path: string) =>
    `https://images.weserv.nl/?url=https://upload.wikimedia.org/wikipedia/commons/${path}&w=1000&h=1000&fit=cover&output=jpg`
  const productImageRules: { keywords: string[]; image: string }[] = [
    {
      keywords: ['云南普洱茶饼', '普洱茶饼'],
      image: wikiProductImage('a/a3/Yunnan_Sourcing_%22Golden_Pig%22_Raw_Pu-erh_Tea_Cake_-_inner_packaging_-_WikiTea.jpg')
    },
    {
      keywords: ['景德镇手绘青花瓷茶具套装', '青花瓷茶具'],
      image: wikiProductImage('6/69/Blue_and_white_jar_Jingdezhen_1271_1368.jpg')
    },
    {
      keywords: ['西藏牦牛肉干', '牦牛肉干'],
      image: wikiProductImage('8/8c/Air-dried_ground_beef.jpg')
    },
    {
      keywords: ['苏州丝绸真丝围巾', '真丝围巾'],
      image: wikiProductImage('thumb/c/c9/Silk_scarf.JPG/1280px-Silk_scarf.JPG')
    },
    {
      keywords: ['新疆和田大枣', '和田大枣'],
      image: wikiProductImage('2/28/Dried_jujube.jpg')
    },
    {
      keywords: ['四川麻辣火锅底料', '火锅底料'],
      image: wikiProductImage('2/2f/Chongqing.Original_Sichuan_hotpot_base.jpg')
    },
    {
      keywords: ['杭州西湖龙井茶', '龙井茶'],
      image: wikiProductImage('thumb/9/9e/Longjing_tea_leaves.jpg/1280px-Longjing_tea_leaves.jpg')
    },
    {
      keywords: ['北京稻香村糕点礼盒', '稻香村糕点'],
      image: wikiProductImage('b/b0/%E5%8C%97%E4%BA%AC%E7%A8%BB%E9%A6%99%E6%9D%91%E7%B3%95%E7%82%B9%E7%A4%BC%E7%9B%92.jpg')
    },
    {
      keywords: ['内蒙古奶酪条', '奶酪条'],
      image: 'https://images.unsplash.com/photo-1452195100486-9cc805987862?auto=format&fit=crop&w=1000&q=80'
    },
    {
      keywords: ['西安兵马俑陶瓷摆件', '兵马俑陶瓷摆件'],
      image: wikiProductImage('1/1f/General_Terracotta_Warrior.jpg')
    },
    {
      keywords: ['贵州茅台镇酱香白酒', '酱香白酒'],
      image: wikiProductImage('1/15/Baijiu_in_Haikou_2018_09_11.jpg')
    },
    {
      keywords: ['苗族手工刺绣挎包', '苗族手工刺绣'],
      image: wikiProductImage('5/5b/Miao_embroidery_-_Yunnan_Provincial_Museum-_DSC02190.JPG')
    },
    {
      keywords: ['广西螺蛳粉', '螺蛳粉'],
      image: wikiProductImage('thumb/2/2a/Luosifen.jpg/1000px-Luosifen.jpg')
    },
    {
      keywords: ['云南鲜花饼礼盒', '鲜花饼'],
      image: wikiProductImage('thumb/c/cb/%E4%BA%91%E5%8D%97%E7%8E%AB%E7%91%B0%E9%B2%9C%E8%8A%B1%E9%A5%BC-2164122.jpg/1000px-%E4%BA%91%E5%8D%97%E7%8E%AB%E7%91%B0%E9%B2%9C%E8%8A%B1%E9%A5%BC-2164122.jpg')
    },
    {
      keywords: ['景泰蓝珐琅彩手镯', '景泰蓝', '珐琅彩手镯'],
      image: wikiProductImage('2/2a/Cloisonne_Enamel.jpg')
    }
  ]
  const matchedRule = productImageRules.find(rule => includesAny(text, rule.keywords))
  if (matchedRule) return matchedRule.image

  const fallback = product.image
  if (!hasBadImage(fallback)) return fallback || fallbackImage

  if (includesAny(text, ['茶叶', 'tea'])) return 'https://images.unsplash.com/photo-1544787219-7f47ccb76574?auto=format&fit=crop&w=1000&q=80'
  if (includesAny(text, ['手工艺', 'craft'])) return 'https://images.unsplash.com/photo-1452860606245-08befc0ff44b?auto=format&fit=crop&w=1000&q=80'
  if (includesAny(text, ['丝绸', 'fashion'])) return 'https://images.unsplash.com/photo-1483985988355-763728e1935b?auto=format&fit=crop&w=1000&q=80'
  if (includesAny(text, ['白酒', '酒'])) return 'https://images.unsplash.com/photo-1569529465841-dfecdab7503b?auto=format&fit=crop&w=1000&q=80'
  if (includesAny(text, ['首饰', 'jewelry'])) return 'https://images.unsplash.com/photo-1515562141207-7a88fb7ce338?auto=format&fit=crop&w=1000&q=80'
  if (includesAny(text, ['零食', '糕点', '特产', 'food'])) return 'https://images.unsplash.com/photo-1542838132-92c53300491e?auto=format&fit=crop&w=1000&q=80'
  return 'https://images.unsplash.com/photo-1542838132-92c53300491e?auto=format&fit=crop&w=1000&q=80'
}

export const getCarImage = (car: Partial<CarRental>) => {
  const fallback = car.image
  if (!hasBadImage(fallback)) return fallback || fallbackImage

  const text = `${car.brand || ''} ${car.model || ''} ${car.type || ''}`.toLowerCase()
  if (includesAny(text, ['tesla', 'model 3', '特斯拉'])) return 'https://images.weserv.nl/?url=upload.wikimedia.org/wikipedia/commons/9/97/Tesla_Model_3%2C_EMS_2024%2C_Essen_%28P1032260%29.jpg&w=1000&h=650&fit=cover&output=jpg'
  if (includesAny(text, ['benz', 'mercedes', '奔驰', 'e级'])) return 'https://images.weserv.nl/?url=upload.wikimedia.org/wikipedia/commons/0/06/MERCEDES-BENZ_E-CLASS_%28W213%29_China.jpg&w=1000&h=650&fit=cover&output=jpg'
  if (includesAny(text, ['honda', 'cr-v', '本田'])) return 'https://images.weserv.nl/?url=upload.wikimedia.org/wikipedia/commons/4/4d/Honda_CR-V_%286th_generation%29_hybrid_1X7A0866.jpg&w=1000&h=650&fit=cover&output=jpg'
  if (includesAny(text, ['toyota', '凯美瑞', '丰田'])) return 'https://images.weserv.nl/?url=upload.wikimedia.org/wikipedia/commons/3/38/TOYOTA_CAMRY_%28XV70%29_China_%284%29.jpg&w=1000&h=650&fit=cover&output=jpg'
  if (includesAny(text, ['volkswagen', '大众', '朗逸'])) return 'https://images.weserv.nl/?url=upload.wikimedia.org/wikipedia/commons/c/ce/2018_SAIC-Volkswagen_Lavida_%28front%29.jpg&w=1000&h=650&fit=cover&output=jpg'
  if (includesAny(text, ['gl8', '别克', 'buick'])) return 'https://images.weserv.nl/?url=upload.wikimedia.org/wikipedia/commons/4/44/BUICK_GL8_CENTURY_%28BUICK_GL8_FOURTH_GENENRATION%29_China.jpg&w=1000&h=650&fit=cover&output=jpg'
  if (includesAny(text, ['suv'])) return 'https://images.weserv.nl/?url=upload.wikimedia.org/wikipedia/commons/4/4d/Honda_CR-V_%286th_generation%29_hybrid_1X7A0866.jpg&w=1000&h=650&fit=cover&output=jpg'
  return 'https://images.weserv.nl/?url=upload.wikimedia.org/wikipedia/commons/c/ce/2018_SAIC-Volkswagen_Lavida_%28front%29.jpg&w=1000&h=650&fit=cover&output=jpg'
}
