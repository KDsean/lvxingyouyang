-- =============================================
-- mock_data.sql
-- 旅行有样全表测试数据（兼容 MySQL 5.7/8.0）
-- 执行前请确保已执行 init.sql 完成建表
-- 执行方式：mysql -uroot -p lvxingyouyang < mock_data.sql
-- =============================================

USE `lvxingyouyang`;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 清空旧数据（从子表到父表）
TRUNCATE TABLE trip_plans;
TRUNCATE TABLE product_tags;
TRUNCATE TABLE products;
TRUNCATE TABLE guide_tags;
TRUNCATE TABLE guide_images;
TRUNCATE TABLE guides;
TRUNCATE TABLE restaurant_specialties;
TRUNCATE TABLE restaurant_images;
TRUNCATE TABLE restaurants;
TRUNCATE TABLE car_features;
TRUNCATE TABLE cars;
TRUNCATE TABLE train_seat_types;
TRUNCATE TABLE trains;
TRUNCATE TABLE hotel_tags;
TRUNCATE TABLE hotel_facilities;
TRUNCATE TABLE hotel_images;
TRUNCATE TABLE hotels;
TRUNCATE TABLE flights;
TRUNCATE TABLE attraction_tags;
TRUNCATE TABLE attraction_images;
TRUNCATE TABLE attractions;
TRUNCATE TABLE destination_seasons;
TRUNCATE TABLE destination_tags;
TRUNCATE TABLE destinations;
TRUNCATE TABLE users;

-- =============================================
-- 数字种子表（1~1000）
-- =============================================
DROP TEMPORARY TABLE IF EXISTS tmp_nums;
CREATE TEMPORARY TABLE tmp_nums (n INT PRIMARY KEY) ENGINE=Memory;
INSERT INTO tmp_nums(n)
SELECT a.n + b.n*10 + c.n*100 + 1
FROM
  (SELECT 0 n UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
   UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) a
CROSS JOIN
  (SELECT 0 n UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
   UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) b
CROSS JOIN
  (SELECT 0 n UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
   UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) c;

-- =============================================
-- 1. users（200条）
-- =============================================
INSERT INTO users (username, email, password, phone, avatar, created_at, updated_at)
SELECT
  CONCAT('user_', LPAD(n,4,'0')),
  CONCAT('user_', LPAD(n,4,'0'), '@travel.com'),
  '$2a$10$7EqJtq98hPqEX7fNZaFWoOHiA4w7h5gM8m6KzW8nVh9qYw3dF5e2K',
  CONCAT('13', LPAD(100000000+n,9,'0')),
  CONCAT('https://picsum.photos/seed/u',n,'/200/200'),
  DATE_SUB(NOW(), INTERVAL (200-n) DAY),
  DATE_SUB(NOW(), INTERVAL (200-n) DAY)
FROM tmp_nums WHERE n <= 200;

-- =============================================
-- 2. destinations（80条）
-- =============================================
INSERT INTO destinations (name, country, description, image, popularity_score, created_at)
SELECT
  CONCAT(ELT((n%10)+1,'北京','上海','杭州','成都','西安','大理','丽江','三亚','重庆','厦门'),'·',LPAD(n,3,'0')),
  '中国',
  CONCAT('这里是第',n,'个热门旅游目的地，拥有独特的自然风光与人文景观，四季皆宜出游。'),
  CONCAT('https://picsum.photos/seed/dst',n,'/1200/600'),
  ROUND(6.0 + (n%40)/10.0, 1),
  DATE_SUB(NOW(), INTERVAL n DAY)
FROM tmp_nums WHERE n <= 80;

INSERT INTO destination_tags (destination_id, tag)
SELECT id, ELT((id%6)+1,'亲子游','美食天堂','摄影胜地','海滨度假','历史文化','自然风光') FROM destinations;
INSERT INTO destination_tags (destination_id, tag)
SELECT id, ELT((id%8)+1,'热门目的地','小众秘境','周末出逃','深度游','避暑好去处','赏花踏青','冬季暖阳','金秋旅行') FROM destinations;

INSERT INTO destination_seasons (destination_id, season)
SELECT id, ELT((id%4)+1,'春季','夏季','秋季','冬季') FROM destinations;
INSERT INTO destination_seasons (destination_id, season)
SELECT id, ELT(((id+2)%4)+1,'春季','夏季','秋季','冬季') FROM destinations WHERE id%2=1;

-- =============================================
-- 3. attractions（300条）
-- =============================================
INSERT INTO attractions (name, city, type, rating, price, open_time, description, created_at)
SELECT
  CONCAT(ELT((n%15)+1,'古城','博物馆','公园','寺庙','广场','湖泊','山脉','海湾','古镇','遗址','瀑布','草原','沙漠','花海','温泉'),'-',LPAD(n,4,'0')),
  ELT((n%12)+1,'北京','上海','杭州','成都','西安','大理','丽江','三亚','重庆','厦门','广州','深圳'),
  ELT((n%4)+1,'culture','nature','entertainment','museum'),
  ROUND(3.5 + (n%15)/10.0, 1),
  ROUND((n%20)*12.0, 2),
  ELT((n%5)+1,'08:00-17:00','09:00-18:00','全天开放','07:30-20:00','09:00-20:30'),
  CONCAT('位于',ELT((n%12)+1,'北京','上海','杭州','成都','西安','大理','丽江','三亚','重庆','厦门','广州','深圳'),'的热门景点，每年吸引游客超过',50+(n%200),'万人次，适合家庭出游与摄影爱好者。'),
  DATE_SUB(NOW(), INTERVAL n HOUR)
FROM tmp_nums WHERE n <= 300;

INSERT INTO attraction_images (attraction_id, image_url)
SELECT id, CONCAT('https://picsum.photos/seed/at',id,'a/900/600') FROM attractions;
INSERT INTO attraction_images (attraction_id, image_url)
SELECT id, CONCAT('https://picsum.photos/seed/at',id,'b/900/600') FROM attractions WHERE id%2=0;
INSERT INTO attraction_images (attraction_id, image_url)
SELECT id, CONCAT('https://picsum.photos/seed/at',id,'c/900/600') FROM attractions WHERE id%3=0;

INSERT INTO attraction_tags (attraction_id, tag)
SELECT id, ELT((id%8)+1,'必打卡','拍照圣地','亲子友好','历史文化','自然风光','夜景美丽','网红景点','小众推荐') FROM attractions;
INSERT INTO attraction_tags (attraction_id, tag)
SELECT id, ELT((id%6)+1,'半日游','一日游','深度游','免门票','高性价比','世界遗产') FROM attractions;

-- =============================================
-- 4. hotels（200条）
-- =============================================
INSERT INTO hotels (name, location, city, address, rating, review_count, price, description, star_level, created_at)
SELECT
  CONCAT(ELT((n%10)+1,'如家','汉庭','全季','维也纳','希尔顿','万豪','洲际','维多利亚','悦榕庄','安缦'),'酒店-',LPAD(n,4,'0')),
  ELT((n%8)+1,'市中心','古城区','高铁站旁','机场附近','景区内','海边','大学城','会展中心旁'),
  ELT((n%12)+1,'北京','上海','杭州','成都','西安','大理','丽江','三亚','重庆','厦门','广州','深圳'),
  CONCAT(ELT((n%8)+1,'中山','人民','解放','建设','文化','学院','滨江','景观'),'路',FLOOR(1+n%200),'号'),
  ROUND(3.6 + (n%14)/10.0, 1),
  FLOOR(50 + (n*7)%8000),
  ROUND(99 + (n%50)*28.0, 2),
  CONCAT('酒店装修精致，服务一流，毗邻',ELT((n%6)+1,'火车站','地铁站','商业中心','景区','机场','海滩'),'，出行便利，入住体验极佳。'),
  (n%5)+1,
  DATE_SUB(NOW(), INTERVAL n DAY)
FROM tmp_nums WHERE n <= 200;

INSERT INTO hotel_images (hotel_id, image_url)
SELECT id, CONCAT('https://picsum.photos/seed/ht',id,'a/1000/700') FROM hotels;
INSERT INTO hotel_images (hotel_id, image_url)
SELECT id, CONCAT('https://picsum.photos/seed/ht',id,'b/1000/700') FROM hotels WHERE id%2=0;

INSERT INTO hotel_facilities (hotel_id, facility)
SELECT id, ELT((id%8)+1,'免费WiFi','停车场','健身房','游泳池','含早餐','洗衣服务','机场接送','会议室') FROM hotels;
INSERT INTO hotel_facilities (hotel_id, facility)
SELECT id, ELT((id%8)+1,'24小时前台','无烟房','家庭房','温泉泡池','商务中心','儿童乐园','酒吧','行李寄存') FROM hotels;
INSERT INTO hotel_facilities (hotel_id, facility)
SELECT id, ELT((id%6)+1,'餐厅','SPA','棋牌室','电影院','超市','咖啡厅') FROM hotels WHERE id%3=0;

INSERT INTO hotel_tags (hotel_id, tag)
SELECT id, ELT((id%6)+1,'高性价比','豪华五星','亲子推荐','商务出行','景观客房','网红打卡') FROM hotels;
INSERT INTO hotel_tags (hotel_id, tag)
SELECT id, ELT((id%6)+1,'民宿风格','海景房','山景房','古城风情','现代简约','温泉度假') FROM hotels WHERE id%2=1;

-- =============================================
-- 5. flights（500条）
-- =============================================
INSERT INTO flights (airline, flight_number, departure_airport, departure_city,
  departure_time, arrival_airport, arrival_city, arrival_time,
  price, cabin_class, available_seats, created_at)
SELECT
  ELT((n%8)+1,'中国国际航空','中国东方航空','中国南方航空','海南航空','四川航空','深圳航空','厦门航空','春秋航空'),
  CONCAT(ELT((n%8)+1,'CA','MU','CZ','HU','3U','ZH','MF','9C'), LPAD(1000+n,4,'0')),
  ELT((n%12)+1,'PEK','PVG','HGH','CTU','XIY','DLU','LJG','SYX','CKG','XMN','CAN','SZX'),
  ELT((n%12)+1,'北京','上海','杭州','成都','西安','大理','丽江','三亚','重庆','厦门','广州','深圳'),
  DATE_FORMAT(DATE_ADD('2026-04-01 06:00:00', INTERVAL (n*37) MINUTE), '%Y-%m-%d %H:%i'),
  ELT(((n+5)%12)+1,'PEK','PVG','HGH','CTU','XIY','DLU','LJG','SYX','CKG','XMN','CAN','SZX'),
  ELT(((n+5)%12)+1,'北京','上海','杭州','成都','西安','大理','丽江','三亚','重庆','厦门','广州','深圳'),
  DATE_FORMAT(DATE_ADD('2026-04-01 08:30:00', INTERVAL (n*37) MINUTE), '%Y-%m-%d %H:%i'),
  ROUND(280 + (n%35)*55.0, 2),
  ELT((n%3)+1,'经济舱','超级经济舱','商务舱'),
  10 + (n%200),
  DATE_SUB(NOW(), INTERVAL n HOUR)
FROM tmp_nums WHERE n <= 500;

-- =============================================
-- 6. trains（400条）
-- =============================================
INSERT INTO trains (train_number, departure_station, departure_city,
  departure_time, arrival_station, arrival_city, arrival_time, duration, created_at)
SELECT
  CONCAT(ELT((n%4)+1,'G','D','K','Z'), LPAD(100+n,4,'0')),
  CONCAT(ELT((n%10)+1,'北京南','上海虹桥','杭州东','成都东','西安北','大理站','丽江站','三亚站','重庆西','厦门北')),
  ELT((n%10)+1,'北京','上海','杭州','成都','西安','大理','丽江','三亚','重庆','厦门'),
  DATE_FORMAT(DATE_ADD('2026-04-01 07:00:00', INTERVAL (n*29) MINUTE), '%Y-%m-%d %H:%i'),
  CONCAT(ELT(((n+3)%10)+1,'北京南','上海虹桥','杭州东','成都东','西安北','大理站','丽江站','三亚站','重庆西','厦门北')),
  ELT(((n+3)%10)+1,'北京','上海','杭州','成都','西安','大理','丽江','三亚','重庆','厦门'),
  DATE_FORMAT(DATE_ADD('2026-04-01 10:00:00', INTERVAL (n*29) MINUTE), '%Y-%m-%d %H:%i'),
  CONCAT((n%8)+2,'小时',(n%6)*10,'分'),
  DATE_SUB(NOW(), INTERVAL n HOUR)
FROM tmp_nums WHERE n <= 400;

INSERT INTO train_seat_types (train_id, seat_types)
SELECT id, ELT((id%6)+1,'二等座','一等座','商务座','硬座','硬卧','软卧') FROM trains;
INSERT INTO train_seat_types (train_id, seat_types)
SELECT id, ELT((id%4)+1,'无座','高级软卧','动卧','餐车') FROM trains WHERE id%3=0;

-- =============================================
-- 7. cars（150条）
-- =============================================
INSERT INTO cars (brand, model, type, seats, transmission, price_per_day, image, location, created_at)
SELECT
  ELT((n%12)+1,'丰田','本田','大众','别克','宝马','奥迪','比亚迪','特斯拉','吉利','长安','哈弗','理想'),
  CONCAT(ELT((n%12)+1,'卡罗拉','雅阁','帕萨特','君越','3系','A4L','汉EV','Model3','星越L','CS75','H6','L9'),'-',LPAD(n,3,'0')),
  ELT((n%5)+1,'经济型','舒适型','SUV型','商务型','豪华型'),
  ELT((n%4)+1,'4','5','6','7'),
  ELT((n%2)+1,'自动挡','手动挡'),
  ROUND(99 + (n%30)*25.0, 2),
  CONCAT('https://picsum.photos/seed/car',n,'/1000/600'),
  ELT((n%12)+1,'北京朝阳','上海浦东','杭州西湖','成都锦江','西安雁塔','大理古城','丽江古城','三亚海棠','重庆渝中','厦门思明','广州天河','深圳南山'),
  DATE_SUB(NOW(), INTERVAL n DAY)
FROM tmp_nums WHERE n <= 150;

INSERT INTO car_features (car_id, feature)
SELECT id, ELT((id%8)+1,'GPS导航','倒车影像','蓝牙音响','CarPlay','儿童座椅','全景天窗','定速巡航','无钥匙进入') FROM cars;
INSERT INTO car_features (car_id, feature)
SELECT id, ELT((id%6)+1,'行车记录仪','座椅加热','ETC','车载充电器','ABS防抱死','ESP车身稳定') FROM cars WHERE id%2=0;

-- =============================================
-- 8. restaurants（200条）
-- =============================================
INSERT INTO restaurants (name, cuisine, city, rating, price_level, address, open_hours, created_at)
SELECT
  CONCAT(ELT((n%12)+1,'老字号','大牌子','名厨','家常','私房','招牌','地道','特色','传统','网红','精品','御膳'),'餐厅-',LPAD(n,4,'0')),
  ELT((n%10)+1,'川菜','粤菜','湘菜','浙菜','鲁菜','闽菜','火锅','烧烤','西餐','日料'),
  ELT((n%12)+1,'北京','上海','杭州','成都','西安','大理','丽江','三亚','重庆','厦门','广州','深圳'),
  ROUND(3.6 + (n%14)/10.0, 1),
  (n%5)+1,
  CONCAT(ELT((n%8)+1,'美食街','小吃街','文化路','幸福里','春熙路','南锣鼓巷','宽窄巷子','中山路'),FLOOR(1+n%100),'号'),
  ELT((n%4)+1,'10:00-22:00','11:00-23:00','09:00-21:00','全天营业'),
  DATE_SUB(NOW(), INTERVAL n HOUR)
FROM tmp_nums WHERE n <= 200;

INSERT INTO restaurant_images (restaurant_id, image_url)
SELECT id, CONCAT('https://picsum.photos/seed/rs',id,'a/900/650') FROM restaurants;
INSERT INTO restaurant_images (restaurant_id, image_url)
SELECT id, CONCAT('https://picsum.photos/seed/rs',id,'b/900/650') FROM restaurants WHERE id%2=0;

INSERT INTO restaurant_specialties (restaurant_id, specialty)
SELECT id, ELT((id%12)+1,'招牌烤鸭','夫妻肺片','小笼包','白切鸡','烧鹅','毛血旺','东坡肉','糖醋鲤鱼','佛跳墙','牛肉面','过桥米线','白灼虾') FROM restaurants;
INSERT INTO restaurant_specialties (restaurant_id, specialty)
SELECT id, ELT((id%8)+1,'招牌汤品','特色凉菜','私房红烧肉','秘制烧烤','手工饺子','当季时蔬','主厨沙拉','特色甜品') FROM restaurants WHERE id%2=1;
INSERT INTO restaurant_specialties (restaurant_id, specialty)
SELECT id, ELT((id%6)+1,'老火靓汤','清蒸海鲜','脆皮乳鸽','砂锅粥','招牌炒饭','特色煲仔饭') FROM restaurants WHERE id%3=0;

-- =============================================
-- 9. guides（300条）
-- =============================================
INSERT INTO guides (title, destination, author_id, author_name, author_avatar, content, view_count, like_count, created_at)
SELECT
  CONCAT(ELT((n%10)+1,'超全','深度','穷游','自驾','亲子','情侣','摄影','美食','避坑','保姆级'),ELT((n%12)+1,'北京','上海','杭州','成都','西安','大理','丽江','三亚','重庆','厦门','广州','深圳'),'旅游攻略-',LPAD(n,3,'0')),
  ELT((n%12)+1,'北京','上海','杭州','成都','西安','大理','丽江','三亚','重庆','厦门','广州','深圳'),
  ((n-1)%200)+1,
  CONCAT('旅行达人_', LPAD(((n-1)%200)+1,4,'0')),
  CONCAT('https://picsum.photos/seed/ga',((n-1)%200)+1,'/120/120'),
  CONCAT('# ',ELT((n%12)+1,'北京','上海','杭州','成都','西安','大理','丽江','三亚','重庆','厦门','广州','深圳'),'旅游全攻略\n\n## 交通\n从出发地乘坐高铁或飞机约需',2+(n%6),'小时。\n\n## 住宿\n推荐入住',ELT((n%5)+1,'五星级酒店','精品民宿','青年旅社','度假村','特色客栈'),'，均价约',100+(n%50)*20,'元/晚。\n\n## 必游景点\n第1天游览市区核心景点，第2天深度体验当地文化，第3天品味特色美食。\n\n## 美食推荐\n当地特色美食不可错过，人均消费约',50+(n%30)*10,'元。\n\n## 预算参考\n5天行程总预算约',800+(n%20)*200,'元（含交通、住宿、餐饮、门票）。'),
  100 + (n*37)%50000,
  10 + (n*13)%5000,
  DATE_SUB(NOW(), INTERVAL n HOUR)
FROM tmp_nums WHERE n <= 300;

INSERT INTO guide_images (guide_id, image_url)
SELECT id, CONCAT('https://picsum.photos/seed/gi',id,'a/1200/800') FROM guides;
INSERT INTO guide_images (guide_id, image_url)
SELECT id, CONCAT('https://picsum.photos/seed/gi',id,'b/1200/800') FROM guides WHERE id%2=0;
INSERT INTO guide_images (guide_id, image_url)
SELECT id, CONCAT('https://picsum.photos/seed/gi',id,'c/1200/800') FROM guides WHERE id%3=0;

INSERT INTO guide_tags (guide_id, tag)
SELECT id, ELT((id%8)+1,'亲子','摄影','美食','穷游','自驾','情侣','毕业旅行','周末游') FROM guides;
INSERT INTO guide_tags (guide_id, tag)
SELECT id, ELT((id%8)+1,'避坑指南','必吃清单','最优路线','交通攻略','住宿推荐','行李清单','拍照打卡','详细预算') FROM guides;
INSERT INTO guide_tags (guide_id, tag)
SELECT id, ELT((id%6)+1,'春季出游','夏日避暑','秋色美景','冬日暖阳','节假日','小长假') FROM guides WHERE id%2=1;

-- =============================================
-- 10. products（300条）
-- =============================================
INSERT INTO products (name, category, price, original_price, image, description, stock, sales, created_at)
SELECT
  CONCAT(ELT((n%15)+1,'云南','杭州','北京','成都','西安','厦门','重庆','广州','苏州','贵州','内蒙古','新疆','西藏','广西','山东'),'·',ELT((n%10)+1,'特产','手工艺品','茶叶','零食','丝绸','白酒','首饰','纪念品','调味料','糕点'),'-',LPAD(n,4,'0')),
  ELT((n%6)+1,'food','craft','fashion','tea','snack','souvenir'),
  ROUND(19 + (n%80)*6.5, 2),
  ROUND(29 + (n%90)*7.0, 2),
  CONCAT('https://picsum.photos/seed/pd',n,'/800/800'),
  CONCAT('这是来自',ELT((n%15)+1,'云南','杭州','北京','成都','西安','厦门','重庆','广州','苏州','贵州','内蒙古','新疆','西藏','广西','山东'),'的特色',ELT((n%10)+1,'特产','手工艺品','茶叶','零食','丝绸','白酒','首饰','纪念品','调味料','糕点'),'，品质上乘，适合馈赠亲友。'),
  50 + (n*3)%5000,
  (n*11)%20000,
  DATE_SUB(NOW(), INTERVAL n DAY)
FROM tmp_nums WHERE n <= 300;

INSERT INTO product_tags (product_id, tag)
SELECT id, ELT((id%8)+1,'热销爆款','新品上市','地方特产','纯手工','精美礼盒','高性价比','强烈推荐','限时特惠') FROM products;
INSERT INTO product_tags (product_id, tag)
SELECT id, ELT((id%10)+1,'云南','杭州','北京','成都','西安','厦门','重庆','广州','苏州','贵州') FROM products;
INSERT INTO product_tags (product_id, tag)
SELECT id, ELT((id%6)+1,'自用','送礼','收藏','尝鲜','节日优选','伴手礼') FROM products WHERE id%2=1;

-- =============================================
-- 11. trip_plans（500条）
-- =============================================
INSERT INTO trip_plans (
  user_id, title, destination, origin, trip_days, interests,
  start_date, end_date, budget, plan_details, created_at, updated_at
)
SELECT
  ((n-1)%200)+1,
  CONCAT(ELT((n%8)+1,'浪漫','探险','亲子','深度','美食','休闲','文化','自驾'),ELT((n%12)+1,'北京','上海','杭州','成都','西安','大理','丽江','三亚','重庆','厦门','广州','深圳'),'之旅-',LPAD(n,4,'0')),
  ELT((n%12)+1,'北京','上海','杭州','成都','西安','大理','丽江','三亚','重庆','厦门','广州','深圳'),
  ELT(((n+6)%12)+1,'北京','上海','杭州','成都','西安','大理','丽江','三亚','重庆','厦门','广州','深圳'),
  (n%7)+1,
  ELT((n%6)+1,'美食,摄影','历史文化,博物馆','自然风光,徒步','海边,度假','亲子,休闲','购物,城市漫游'),
  DATE_FORMAT(DATE_ADD('2026-05-01', INTERVAL n DAY), '%Y-%m-%d'),
  DATE_FORMAT(DATE_ADD('2026-05-01', INTERVAL n+((n%7)+1) DAY), '%Y-%m-%d'),
  ROUND(1200 + (n%20)*450.0, 2),
  CONCAT('【第1天】抵达目的地，办理入住，在周边散步感受当地风情。\n【第2天】上午游览核心景点，下午品尝当地美食，晚上逛夜市。\n',IF((n%7)+1>=3,'【第3天】深度体验当地文化，参观博物馆或艺术馆。\n',''),IF((n%7)+1>=4,'【第4天】前往周边小镇或自然景区一日游。\n',''),IF((n%7)+1>=5,'【第5天】购物，采购纪念品和特产。\n',''),'【最后一天】整理行李，返回出发地，结束愉快旅程。'),
  DATE_SUB(NOW(), INTERVAL n HOUR),
  DATE_SUB(NOW(), INTERVAL (n DIV 2) HOUR)
FROM tmp_nums WHERE n <= 500;

SET FOREIGN_KEY_CHECKS = 1;

-- =============================================
-- 数据量核查
-- =============================================
SELECT 'users'                  AS tbl, COUNT(*) AS total FROM users
UNION ALL SELECT 'destinations',           COUNT(*) FROM destinations
UNION ALL SELECT 'destination_tags',       COUNT(*) FROM destination_tags
UNION ALL SELECT 'destination_seasons',    COUNT(*) FROM destination_seasons
UNION ALL SELECT 'attractions',            COUNT(*) FROM attractions
UNION ALL SELECT 'attraction_images',      COUNT(*) FROM attraction_images
UNION ALL SELECT 'attraction_tags',        COUNT(*) FROM attraction_tags
UNION ALL SELECT 'hotels',                 COUNT(*) FROM hotels
UNION ALL SELECT 'hotel_images',           COUNT(*) FROM hotel_images
UNION ALL SELECT 'hotel_facilities',       COUNT(*) FROM hotel_facilities
UNION ALL SELECT 'hotel_tags',             COUNT(*) FROM hotel_tags
UNION ALL SELECT 'flights',                COUNT(*) FROM flights
UNION ALL SELECT 'trains',                 COUNT(*) FROM trains
UNION ALL SELECT 'train_seat_types',       COUNT(*) FROM train_seat_types
UNION ALL SELECT 'cars',                   COUNT(*) FROM cars
UNION ALL SELECT 'car_features',           COUNT(*) FROM car_features
UNION ALL SELECT 'restaurants',            COUNT(*) FROM restaurants
UNION ALL SELECT 'restaurant_images',      COUNT(*) FROM restaurant_images
UNION ALL SELECT 'restaurant_specialties', COUNT(*) FROM restaurant_specialties
UNION ALL SELECT 'guides',                 COUNT(*) FROM guides
UNION ALL SELECT 'guide_images',       COUNT(*) FROM guide_images
UNION ALL SELECT 'guide_tags',         COUNT(*) FROM guide_tags
UNION ALL SELECT 'products',           COUNT(*) FROM products
UNION ALL SELECT 'product_tags',       COUNT(*) FROM product_tags
UNION ALL SELECT 'trip_plans',         COUNT(*) FROM trip_plans;            