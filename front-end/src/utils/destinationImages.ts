const destinationImageMap: Record<string, string> = {
  北京: 'https://loremflickr.com/1200/800/beijing,forbidden-city?lock=1101',
  上海: 'https://loremflickr.com/1200/800/shanghai,skyline?lock=1102',
  杭州: 'https://loremflickr.com/1200/800/hangzhou,west-lake?lock=1103',
  成都: 'https://loremflickr.com/1200/800/chengdu,panda?lock=1104',
  西安: 'https://loremflickr.com/1200/800/xian,terracotta-army?lock=1105',
  大理: 'https://loremflickr.com/1200/800/dali,yunnan?lock=1106',
  丽江: 'https://loremflickr.com/1200/800/lijiang,old-town?lock=1107',
  三亚: 'https://loremflickr.com/1200/800/sanya,beach?lock=1108',
  重庆: 'https://loremflickr.com/1200/800/chongqing,skyline?lock=1109',
  厦门: 'https://loremflickr.com/1200/800/xiamen,gulangyu?lock=1110',
  广州: 'https://loremflickr.com/1200/800/guangzhou,canton-tower?lock=1111',
  深圳: 'https://loremflickr.com/1200/800/shenzhen,skyline?lock=1112'
}

export const getDestinationKey = (name?: string) => {
  const normalizedName = name || ''
  return normalizedName
    .replace(/·\d+$/, '')
    .replace(/路\d+$/, '')
    .split('·')[0]
    .split('路')[0]
}

export const getDestinationImage = (name?: string, fallback?: string) => {
  return destinationImageMap[getDestinationKey(name)] || fallback || 'https://loremflickr.com/1200/800/travel,landmark?lock=1199'
}
