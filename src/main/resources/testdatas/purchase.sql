INSERT INTO purchase(productid, company, price, amount, at, note)
VALUES(1, '전컴퍼니', 70000, 100, '2023-06-10 16:14:14', '전사장님으로부터 100개 어음발행.');
INSERT INTO purchase(productid, company, price, amount, at, note)
VALUES(2, '전컴퍼니', 20000, 100, '2023-06-11 16:14:14', '');
INSERT INTO purchase(productid, company, price, amount, at, note)
VALUES(2, '전컴퍼니', 20000, 100, '2023-06-11 16:14:14', '');
INSERT INTO purchase(productid, company, price, amount, at, note)
VALUES(4, '폼다이모비스', 5000000, 1, '2023-06-12 16:14:14', '파산예정');

-- outbound를 위한 구매 이력 추가
INSERT INTO purchase(productid, company, price, amount, at, note)
VALUES(5, '폼다이모비스', 9000000, 1, '2023-06-13 16:14:14', '');
INSERT INTO purchase(productid, company, price, amount, at, note)
VALUES(7, '폼다이모비스', 15000, 2, '2023-06-13 16:15:14', '');