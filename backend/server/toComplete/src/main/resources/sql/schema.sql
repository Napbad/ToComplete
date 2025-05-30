-- 用户表
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nickname VARCHAR(50),
    avatar_url VARCHAR(255),
    phone VARCHAR(20),
    status INT NOT NULL DEFAULT 1 COMMENT '用户状态：0-禁用，1-正常',
    last_login_at DATETIME,
    created_at DATETIME NOT NULL,
    updated_at DATETIME,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    INDEX idx_username (username),
    INDEX idx_email (email),
    INDEX idx_status (status)
);

-- 分类表
CREATE TABLE IF NOT EXISTS categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(255),
    color VARCHAR(7),
    icon VARCHAR(50),
    user_id BIGINT NOT NULL,
    sort_order INT NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL,
    updated_at DATETIME,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES users(id),
    INDEX idx_user_id (user_id)
);

-- 备忘录表
CREATE TABLE IF NOT EXISTS memos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    content TEXT,
    priority INT NOT NULL DEFAULT 2,
    status INT NOT NULL DEFAULT 0,
    category_id BIGINT,
    user_id BIGINT NOT NULL,
    remind_at DATETIME,
    due_at DATETIME,
    completed_at DATETIME,
    require_photo BOOLEAN NOT NULL DEFAULT FALSE,
    photo_url VARCHAR(255),
    location_remind VARCHAR(200),
    latitude DECIMAL(10, 8),
    longitude DECIMAL(11, 8),
    remind_radius INT,
    created_at DATETIME NOT NULL,
    updated_at DATETIME,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (category_id) REFERENCES categories(id),
    INDEX idx_user_id (user_id),
    INDEX idx_status (status)
); 