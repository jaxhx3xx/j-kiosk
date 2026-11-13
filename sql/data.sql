-- =========================================
--  J의 키오스크 - 초기 메뉴 데이터
--  ⚠️ schema.sql 실행 직후 딱 한 번만 실행
--  (실수로 또 실행했으면 schema.sql → data.sql 순서로 다시 실행)
-- =========================================

USE j_kiosk;

INSERT INTO category (category_id, name) VALUES
(1, '커피'),
(2, '음료'),
(3, '디저트');

INSERT INTO menu (category_id, name, price, sold_out) VALUES
(1, '아메리카노',        2000, FALSE),
(1, '카페라떼',          3000, FALSE),
(1, '바닐라라떼',        3500, FALSE),
(1, '콜드브루',          3300, TRUE),
(2, '레몬에이드',        3500, FALSE),
(2, '딸기 스무디',       4000, FALSE),
(2, '녹차라떼',          3500, FALSE),
(2, '복숭아 아이스티',   3000, FALSE),
(3, '딸기 생크림 케이크', 4500, FALSE),
(3, '치즈케이크',        4800, FALSE),
(3, '크로플',            3500, FALSE),
(3, '초코칩 쿠키',       2000, FALSE);