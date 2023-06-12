INSERT INTO productinfo(name, company, price, enabled, spec)
VALUES('타이어', '전컴퍼니', 70000, TRUE, '205/50/17R');
INSERT INTO productinfo(name, company, price, enabled, spec)
VALUES('레고', '전컴퍼니', 20000, FALSE, 'Standard');

--스포츠카
/*                     3
                     / | \
                   4   5   6
                           |
                           7
*/
INSERT INTO productinfo(name, company, price, enabled, spec)
VALUES('스포츠카', '폼다이모비스', 46000000, TRUE, '5500CC 500hp');
--자식 품목
INSERT INTO productinfo(name, company, price, enabled, spec)
VALUES('메인프레임', '폼다이모비스', 5000000, TRUE, '4600mm 1000mm');
INSERT INTO productinfo(name, company, price, enabled, spec)
VALUES('엔진', '폼다이모비스', 9000000, TRUE, '5500CC I8');
INSERT INTO productinfo(name, company, price, enabled, spec)
VALUES('연료탱크', '폼다이모비스', 250000, TRUE, '55L');
INSERT INTO productinfo(name, company, price, enabled, spec)
VALUES('체크밸브', '폼다이모비스', 15000, TRUE, '20mm 18mm');


INSERT INTO productinfo(name, company, price, enabled, spec)
VALUES('버그이터', '샘숭일렉트로닉스', 37000, TRUE, '50000V');
INSERT INTO productinfo(name, company, price, enabled, spec)
VALUES('트랜스포머', '미쳤다전자', 8000, TRUE, '60000V');
INSERT INTO productinfo(name, company, price, enabled, spec)
VALUES('슈퍼커패시터', '미쳤다전자', 1000, TRUE, '5000uF 100000V');