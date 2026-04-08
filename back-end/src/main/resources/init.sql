-- init.sql
-- 旅行有样（lvxingyouyang）数据库初始化脚本
-- 说明：
-- 1) 该脚本根据 back-end 的 JPA Entity 推导生成（Spring Data JPA / Hibernate）
-- 2) 字符集统一使用 utf8mb4，便于存储中文与 Emoji
-- 3) 包含主表 + @ElementCollection 产生的集合表 + 常用索引/外键
--
-- 建议执行方式：
--   mysql -uroot -p < init.sql

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

CREATE DATABASE IF NOT EXISTS `lvxingyouyang`
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_0900_ai_ci;

USE `lvxingyouyang`;

-- =========================================================
-- 用户表：users
-- 对应实体：com.lvxingyouyang.entity.User
-- =========================================================
CREATE TABLE IF NOT EXISTS `users` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(255) NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `phone` VARCHAR(255) NULL,
  `avatar` VARCHAR(255) NULL,
  `created_at` DATETIME(6) NULL,
  `updated_at` DATETIME(6) NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_users_username` (`username`),
  UNIQUE KEY `uk_users_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =========================================================
-- 行程规划历史：trip_plans
-- 对应实体：com.lvxingyouyang.entity.TripPlan
-- =========================================================
CREATE TABLE IF NOT EXISTS `trip_plans` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `title` VARCHAR(255) NOT NULL,
  `destination` VARCHAR(255) NULL,
  `origin` VARCHAR(255) NULL,
  `trip_days` INT NULL,
  `interests` VARCHAR(255) NULL,
  `start_date` VARCHAR(255) NULL,
  `end_date` VARCHAR(255) NULL,
  `budget` DOUBLE NULL,
  `plan_details` LONGTEXT NULL,
  `created_at` DATETIME(6) NULL,
  `updated_at` DATETIME(6) NULL,
  PRIMARY KEY (`id`),
  KEY `idx_trip_plans_user_id` (`user_id`),
  CONSTRAINT `fk_trip_plans_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- =========================================================
-- 目的地：destinations
-- 对应实体：com.lvxingyouyang.entity.Destination
-- =========================================================
CREATE TABLE IF NOT EXISTS `destinations` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `country` VARCHAR(255) NULL,
  `description` VARCHAR(255) NULL,
  `image` VARCHAR(255) NULL,
  `popularity_score` DOUBLE NULL,
  `created_at` DATETIME(6) NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `destination_tags` (
  `destination_id` BIGINT NOT NULL,
  `tag` VARCHAR(255) NULL,
  KEY `idx_destination_tags_destination_id` (`destination_id`),
  CONSTRAINT `fk_destination_tags_destination_id`
    FOREIGN KEY (`destination_id`) REFERENCES `destinations` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `destination_seasons` (
  `destination_id` BIGINT NOT NULL,
  `season` VARCHAR(255) NULL,
  KEY `idx_destination_seasons_destination_id` (`destination_id`),
  CONSTRAINT `fk_destination_seasons_destination_id`
    FOREIGN KEY (`destination_id`) REFERENCES `destinations` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =========================================================
-- 景点：attractions
-- 对应实体：com.lvxingyouyang.entity.Attraction
-- =========================================================
CREATE TABLE IF NOT EXISTS `attractions` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `city` VARCHAR(255) NOT NULL,
  `type` VARCHAR(255) NULL,
  `rating` DOUBLE NULL,
  `price` DOUBLE NOT NULL,
  `open_time` VARCHAR(255) NULL,
  `description` VARCHAR(255) NULL,
  `created_at` DATETIME(6) NULL,
  PRIMARY KEY (`id`),
  KEY `idx_attractions_city` (`city`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `attraction_images` (
  `attraction_id` BIGINT NOT NULL,
  `image_url` VARCHAR(255) NULL,
  KEY `idx_attraction_images_attraction_id` (`attraction_id`),
  CONSTRAINT `fk_attraction_images_attraction_id`
    FOREIGN KEY (`attraction_id`) REFERENCES `attractions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `attraction_tags` (
  `attraction_id` BIGINT NOT NULL,
  `tag` VARCHAR(255) NULL,
  KEY `idx_attraction_tags_attraction_id` (`attraction_id`),
  CONSTRAINT `fk_attraction_tags_attraction_id`
    FOREIGN KEY (`attraction_id`) REFERENCES `attractions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =========================================================
-- 航班：flights
-- 对应实体：com.lvxingyouyang.entity.Flight
-- =========================================================
CREATE TABLE IF NOT EXISTS `flights` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `airline` VARCHAR(255) NOT NULL,
  `flight_number` VARCHAR(255) NOT NULL,
  `departure_airport` VARCHAR(255) NULL,
  `departure_city` VARCHAR(255) NULL,
  `departure_time` VARCHAR(255) NULL,
  `arrival_airport` VARCHAR(255) NULL,
  `arrival_city` VARCHAR(255) NULL,
  `arrival_time` VARCHAR(255) NULL,
  `price` DOUBLE NOT NULL,
  `cabin_class` VARCHAR(255) NULL,
  `available_seats` INT NULL,
  `created_at` DATETIME(6) NULL,
  PRIMARY KEY (`id`),
  KEY `idx_flights_route` (`departure_city`, `arrival_city`),
  KEY `idx_flights_flight_number` (`flight_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =========================================================
-- 酒店：hotels
-- 对应实体：com.lvxingyouyang.entity.Hotel
-- =========================================================
CREATE TABLE IF NOT EXISTS `hotels` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `location` VARCHAR(255) NULL,
  `city` VARCHAR(255) NOT NULL,
  `address` VARCHAR(255) NULL,
  `rating` DOUBLE NULL,
  `review_count` INT NULL,
  `price` DOUBLE NOT NULL,
  `description` VARCHAR(255) NULL,
  `star_level` INT NULL,
  `created_at` DATETIME(6) NULL,
  PRIMARY KEY (`id`),
  KEY `idx_hotels_city` (`city`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `hotel_images` (
  `hotel_id` BIGINT NOT NULL,
  `image_url` VARCHAR(255) NULL,
  KEY `idx_hotel_images_hotel_id` (`hotel_id`),
  CONSTRAINT `fk_hotel_images_hotel_id`
    FOREIGN KEY (`hotel_id`) REFERENCES `hotels` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `hotel_facilities` (
  `hotel_id` BIGINT NOT NULL,
  `facility` VARCHAR(255) NULL,
  KEY `idx_hotel_facilities_hotel_id` (`hotel_id`),
  CONSTRAINT `fk_hotel_facilities_hotel_id`
    FOREIGN KEY (`hotel_id`) REFERENCES `hotels` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `hotel_tags` (
  `hotel_id` BIGINT NOT NULL,
  `tag` VARCHAR(255) NULL,
  KEY `idx_hotel_tags_hotel_id` (`hotel_id`),
  CONSTRAINT `fk_hotel_tags_hotel_id`
    FOREIGN KEY (`hotel_id`) REFERENCES `hotels` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =========================================================
-- 火车：trains
-- 对应实体：com.lvxingyouyang.entity.Train
-- =========================================================
CREATE TABLE IF NOT EXISTS `trains` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `train_number` VARCHAR(255) NOT NULL,
  `departure_station` VARCHAR(255) NULL,
  `departure_city` VARCHAR(255) NULL,
  `departure_time` VARCHAR(255) NULL,
  `arrival_station` VARCHAR(255) NULL,
  `arrival_city` VARCHAR(255) NULL,
  `arrival_time` VARCHAR(255) NULL,
  `duration` VARCHAR(255) NULL,
  `created_at` DATETIME(6) NULL,
  PRIMARY KEY (`id`),
  KEY `idx_trains_route` (`departure_city`, `arrival_city`),
  KEY `idx_trains_train_number` (`train_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- seatTypes 未显式 @Column，Spring/Hibernate 默认命名一般为 seat_types
CREATE TABLE IF NOT EXISTS `train_seat_types` (
  `train_id` BIGINT NOT NULL,
  `seat_types` VARCHAR(255) NULL,
  KEY `idx_train_seat_types_train_id` (`train_id`),
  CONSTRAINT `fk_train_seat_types_train_id`
    FOREIGN KEY (`train_id`) REFERENCES `trains` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =========================================================
-- 租车：cars
-- 对应实体：com.lvxingyouyang.entity.CarRental
-- =========================================================
CREATE TABLE IF NOT EXISTS `cars` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `brand` VARCHAR(255) NOT NULL,
  `model` VARCHAR(255) NOT NULL,
  `type` VARCHAR(255) NULL,
  `seats` INT NULL,
  `transmission` VARCHAR(255) NULL,
  `price_per_day` DOUBLE NOT NULL,
  `image` VARCHAR(255) NULL,
  `location` VARCHAR(255) NULL,
  `created_at` DATETIME(6) NULL,
  PRIMARY KEY (`id`),
  KEY `idx_cars_location` (`location`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `car_features` (
  `car_id` BIGINT NOT NULL,
  `feature` VARCHAR(255) NULL,
  KEY `idx_car_features_car_id` (`car_id`),
  CONSTRAINT `fk_car_features_car_id`
    FOREIGN KEY (`car_id`) REFERENCES `cars` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =========================================================
-- 餐厅：restaurants
-- 对应实体：com.lvxingyouyang.entity.Restaurant
-- =========================================================
CREATE TABLE IF NOT EXISTS `restaurants` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `cuisine` VARCHAR(255) NULL,
  `city` VARCHAR(255) NOT NULL,
  `rating` DOUBLE NULL,
  `price_level` INT NULL,
  `address` VARCHAR(255) NULL,
  `open_hours` VARCHAR(255) NULL,
  `created_at` DATETIME(6) NULL,
  PRIMARY KEY (`id`),
  KEY `idx_restaurants_city` (`city`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `restaurant_images` (
  `restaurant_id` BIGINT NOT NULL,
  `image_url` VARCHAR(255) NULL,
  KEY `idx_restaurant_images_restaurant_id` (`restaurant_id`),
  CONSTRAINT `fk_restaurant_images_restaurant_id`
    FOREIGN KEY (`restaurant_id`) REFERENCES `restaurants` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `restaurant_specialties` (
  `restaurant_id` BIGINT NOT NULL,
  `specialty` VARCHAR(255) NULL,
  KEY `idx_restaurant_specialties_restaurant_id` (`restaurant_id`),
  CONSTRAINT `fk_restaurant_specialties_restaurant_id`
    FOREIGN KEY (`restaurant_id`) REFERENCES `restaurants` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =========================================================
-- 攻略：guides
-- 对应实体：com.lvxingyouyang.entity.Guide
-- =========================================================
CREATE TABLE IF NOT EXISTS `guides` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(255) NOT NULL,
  `destination` VARCHAR(255) NULL,
  `author_id` BIGINT NULL,
  `author_name` VARCHAR(255) NULL,
  `author_avatar` VARCHAR(255) NULL,
  `content` LONGTEXT NULL,
  `view_count` BIGINT NULL,
  `like_count` BIGINT NULL,
  `created_at` DATETIME(6) NULL,
  PRIMARY KEY (`id`),
  KEY `idx_guides_destination` (`destination`),
  KEY `idx_guides_author_id` (`author_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `guide_images` (
  `guide_id` BIGINT NOT NULL,
  `image_url` VARCHAR(255) NULL,
  KEY `idx_guide_images_guide_id` (`guide_id`),
  CONSTRAINT `fk_guide_images_guide_id`
    FOREIGN KEY (`guide_id`) REFERENCES `guides` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `guide_tags` (
  `guide_id` BIGINT NOT NULL,
  `tag` VARCHAR(255) NULL,
  KEY `idx_guide_tags_guide_id` (`guide_id`),
  CONSTRAINT `fk_guide_tags_guide_id`
    FOREIGN KEY (`guide_id`) REFERENCES `guides` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =========================================================
-- 商品：products
-- 对应实体：com.lvxingyouyang.entity.Product
-- =========================================================
CREATE TABLE IF NOT EXISTS `products` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `category` VARCHAR(255) NULL,
  `price` DOUBLE NOT NULL,
  `original_price` DOUBLE NULL,
  `image` VARCHAR(255) NULL,
  `description` VARCHAR(255) NULL,
  `stock` INT NULL,
  `sales` INT NULL,
  `created_at` DATETIME(6) NULL,
  PRIMARY KEY (`id`),
  KEY `idx_products_category` (`category`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `product_tags` (
  `product_id` BIGINT NOT NULL,
  `tag` VARCHAR(255) NULL,
  KEY `idx_product_tags_product_id` (`product_id`),
  CONSTRAINT `fk_product_tags_product_id`
    FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS = 1;
