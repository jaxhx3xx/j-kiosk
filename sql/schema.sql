-- =========================================
--  J의 키오스크 - DB 스키마 (MySQL 8)
--  테이블 5개 / 3NF
--  * 장바구니는 Java 메모리(List)에서 관리, 결제 시 orders + order_item 저장
-- =========================================
DROP DATABASE IF EXISTS j_kiosk;
CREATE DATABASE IF NOT EXISTS j_kiosk DEFAULT CHARACTER SET utf8mb4;
USE j_kiosk;

-- 1. 카테고리
CREATE TABLE category (
    category_id  INT AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(30) NOT NULL
);

-- 2. 메뉴 (카테고리 1 : N 메뉴)
CREATE TABLE menu (
    menu_id      INT AUTO_INCREMENT PRIMARY KEY,
    category_id  INT NOT NULL,
    name         VARCHAR(50) NOT NULL,
    price        INT NOT NULL,
    sold_out     BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (category_id) REFERENCES category(category_id)
);

-- 3. 스탬프 회원 (전화번호가 곧 PK)
CREATE TABLE customer (
    phone        VARCHAR(13) PRIMARY KEY,
    stamp        INT NOT NULL DEFAULT 0
);

-- 4. 주문 (회원 1 : N 주문, 스탬프 안 쓰면 phone NULL)
CREATE TABLE orders (
    order_id     INT AUTO_INCREMENT PRIMARY KEY,
    phone        VARCHAR(13) NULL,
    order_time   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (phone) REFERENCES customer(phone)
);

-- 5. 주문 메뉴 (주문 1 : N 주문메뉴, 메뉴 1 : N 주문메뉴)
--    temp/size: 이 줄에서 고른 옵션
--    price: 옵션 추가금까지 포함한 1개 가격 (주문 당시 가격 기록)
CREATE TABLE order_item (
    order_item_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id      INT NOT NULL,
    menu_id       INT NOT NULL,
    temp          ENUM('HOT', 'ICE') NULL,   -- 디저트는 NULL
    size          ENUM('R', 'L') NULL,
    shot          INT NOT NULL DEFAULT 0,                              -- 샷 추가 횟수
    syrup         ENUM('VANILLA', 'HAZELNUT', 'CARAMEL') NULL,  
    quantity      INT NOT NULL DEFAULT 1,
    price         INT NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    FOREIGN KEY (menu_id)  REFERENCES menu(menu_id)
);
SHOW TABLES;

-- 주문 총액은 저장하지 않고 계산
-- SELECT SUM(price * quantity) FROM order_item WHERE order_id = ?;