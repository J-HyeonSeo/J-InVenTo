INSERT INTO inbound(purchaseid, stockid, amount, at, note)
VALUES(1, 1, 10, '2023-06-11 18:14:14', '타이어입고');
UPDATE stocks SET inboundid = 1 WHERE id = 1;

INSERT INTO inbound(purchaseid, stockid, amount, at, note)
VALUES(1, 2, 10, '2023-06-12 18:14:14', '타이어입고2');
UPDATE stocks SET inboundid = 2 WHERE id = 2;

INSERT INTO inbound(purchaseid, stockid, amount, at, note)
VALUES(2, 3, 10, '2023-06-13 18:14:14', '레고입고');
UPDATE stocks SET inboundid = 3 WHERE id = 3;

-- 추가 입고
INSERT INTO inbound(purchaseid, stockid, amount, at, note)
VALUES(4, 4, 1, '2023-06-14 18:14:14', '프레임입고');
UPDATE stocks SET inboundid = 4 WHERE id = 4;

INSERT INTO inbound(purchaseid, stockid, amount, at, note)
VALUES(5, 5, 1, '2023-06-14 18:14:14', '엔진입고');
UPDATE stocks SET inboundid = 5 WHERE id = 5;

INSERT INTO inbound(purchaseid, stockid, amount, at, note)
VALUES(6, 6, 1, '2023-06-14 18:14:14', '체크밸브입고');
UPDATE stocks SET inboundid = 6 WHERE id = 6;
