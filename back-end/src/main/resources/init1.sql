-- 创建数据库
CREATE DATABASE IF NOT EXISTS lvxingyouyang CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE lvxingyouyang;

-- 用户表
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    avatar VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 酒店表
CREATE TABLE IF NOT EXISTS hotels (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    location VARCHAR(100),
    city VARCHAR(50) NOT NULL,
    address VARCHAR(255),
    rating DOUBLE,
    review_count INT,
    price DOUBLE NOT NULL,
    description LONGTEXT,
    star_level INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 酒店图片表
CREATE TABLE IF NOT EXISTS hotel_images (
    hotel_id BIGINT NOT NULL,
    image_url VARCHAR(255),
    FOREIGN KEY (hotel_id) REFERENCES hotels(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 酒店设施表
CREATE TABLE IF NOT EXISTS hotel_facilities (
    hotel_id BIGINT NOT NULL,
    facility VARCHAR(100),
    FOREIGN KEY (hotel_id) REFERENCES hotels(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 酒店标签表
CREATE TABLE IF NOT EXISTS hotel_tags (
    hotel_id BIGINT NOT NULL,
    tag VARCHAR(50),
    FOREIGN KEY (hotel_id) REFERENCES hotels(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 航班表
CREATE TABLE IF NOT EXISTS flights (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    airline VARCHAR(50) NOT NULL,
    flight_number VARCHAR(20) NOT NULL,
    departure_airport VARCHAR(10),
    departure_city VARCHAR(50),
    departure_time VARCHAR(20),
    arrival_airport VARCHAR(10),
    arrival_city VARCHAR(50),
    arrival_time VARCHAR(20),
    price DOUBLE NOT NULL,
    cabin_class VARCHAR(20),
    available_seats INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 火车表
CREATE TABLE IF NOT EXISTS trains (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    train_number VARCHAR(20) NOT NULL,
    departure_station VARCHAR(50),
    departure_city VARCHAR(50),
    departure_time VARCHAR(20),
    arrival_station VARCHAR(50),
    arrival_city VARCHAR(50),
    arrival_time VARCHAR(20),
    duration VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 火车座位类型表
CREATE TABLE IF NOT EXISTS train_seat_types (
    train_id BIGINT NOT NULL,
    seat_types VARCHAR(100),
    FOREIGN KEY (train_id) REFERENCES trains(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 租车表
CREATE TABLE IF NOT EXISTS cars (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    brand VARCHAR(50) NOT NULL,
    model VARCHAR(50) NOT NULL,
    type VARCHAR(50),
    seats INT,
    transmission VARCHAR(20),
    price_per_day DOUBLE NOT NULL,
    image VARCHAR(255),
    location VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 租车特性表
CREATE TABLE IF NOT EXISTS car_features (
    car_id BIGINT NOT NULL,
    feature VARCHAR(100),
    FOREIGN KEY (car_id) REFERENCES cars(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 景点表
CREATE TABLE IF NOT EXISTS attractions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    city VARCHAR(50) NOT NULL,
    type VARCHAR(50),
    rating DOUBLE,
    price DOUBLE NOT NULL,
    open_time VARCHAR(100),
    description LONGTEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 景点图片表
CREATE TABLE IF NOT EXISTS attraction_images (
    attraction_id BIGINT NOT NULL,
    image_url VARCHAR(255),
    FOREIGN KEY (attraction_id) REFERENCES attractions(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 景点标签表
CREATE TABLE IF NOT EXISTS attraction_tags (
    attraction_id BIGINT NOT NULL,
    tag VARCHAR(50),
    FOREIGN KEY (attraction_id) REFERENCES attractions(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 攻略表
CREATE TABLE IF NOT EXISTS guides (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    destination VARCHAR(100),
    author_id BIGINT,
    author_name VARCHAR(50),
    author_avatar VARCHAR(255),
    content LONGTEXT,
    view_count BIGINT DEFAULT 0,
    like_count BIGINT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 攻略图片表
CREATE TABLE IF NOT EXISTS guide_images (
    guide_id BIGINT NOT NULL,
    image_url VARCHAR(255),
    FOREIGN KEY (guide_id) REFERENCES guides(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 攻略标签表
CREATE TABLE IF NOT EXISTS guide_tags (
    guide_id BIGINT NOT NULL,
    tag VARCHAR(50),
    FOREIGN KEY (guide_id) REFERENCES guides(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 餐厅表
CREATE TABLE IF NOT EXISTS restaurants (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    cuisine VARCHAR(50),
    city VARCHAR(50) NOT NULL,
    rating DOUBLE,
    price_level INT,
    address VARCHAR(255),
    open_hours VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 餐厅图片表
CREATE TABLE IF NOT EXISTS restaurant_images (
    restaurant_id BIGINT NOT NULL,
    image_url VARCHAR(255),
    FOREIGN KEY (restaurant_id) REFERENCES restaurants(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 餐厅特色菜表
CREATE TABLE IF NOT EXISTS restaurant_specialties (
    restaurant_id BIGINT NOT NULL,
    specialty VARCHAR(100),
    FOREIGN KEY (restaurant_id) REFERENCES restaurants(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 商品表
CREATE TABLE IF NOT EXISTS products (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(50),
    price DOUBLE NOT NULL,
    original_price DOUBLE,
    image VARCHAR(255),
    description LONGTEXT,
    stock INT,
    sales INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 商品标签表
CREATE TABLE IF NOT EXISTS product_tags (
    product_id BIGINT NOT NULL,
    tag VARCHAR(50),
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 目的地表
CREATE TABLE IF NOT EXISTS destinations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    country VARCHAR(50),
    description LONGTEXT,
    image VARCHAR(255),
    popularity_score DOUBLE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 目的地标签表
CREATE TABLE IF NOT EXISTS destination_tags (
    destination_id BIGINT NOT NULL,
    tag VARCHAR(50),
    FOREIGN KEY (destination_id) REFERENCES destinations(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 目的地季节表
CREATE TABLE IF NOT EXISTS destination_seasons (
    destination_id BIGINT NOT NULL,
    season VARCHAR(50),
    FOREIGN KEY (destination_id) REFERENCES destinations(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 行程表
CREATE TABLE IF NOT EXISTS trip_plans (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    title VARCHAR(100) NOT NULL,
    destination VARCHAR(100),
    start_date VARCHAR(20),
    end_date VARCHAR(20),
    budget DOUBLE,
    plan_details LONGTEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 订单表
CREATE TABLE IF NOT EXISTS orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    type VARCHAR(50) NOT NULL,
    item_id BIGINT,
    item_name VARCHAR(100),
    amount DOUBLE NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'pending',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    paid_at TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 创建索引
CREATE INDEX idx_user_username ON users(username);
CREATE INDEX idx_user_email ON users(email);
CREATE INDEX idx_hotel_city ON hotels(city);
CREATE INDEX idx_flight_route ON flights(departure_city, arrival_city);
CREATE INDEX idx_train_route ON trains(departure_city, arrival_city);
CREATE INDEX idx_attraction_city ON attractions(city);
CREATE INDEX idx_restaurant_city ON restaurants(city);
CREATE INDEX idx_guide_destination ON guides(destination);
CREATE INDEX idx_destination_country ON destinations(country);
CREATE INDEX idx_trip_user ON trip_plans(user_id);
CREATE INDEX idx_order_user ON orders(user_id);
