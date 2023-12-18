![LOGO_BG_WH](https://github.com/J-HyeonSeo/J-InVenTo/assets/47245112/bc4fc2df-6311-424a-a6af-9d30e501519a)


# J-InVenTo (재고관리 프로그램)

---

## 프로젝트 소개

- `스프링부트`기반으로 재고관리 시스템을 구현한 프로젝트 입니다.
- 스프링부트로 재고관리에 관련된 API를 작성하였습니다.
- `구매 -> 입고 -> 출고`, 프로세스를 따르는 간단한 재고관리 프로그램입니다.
- 해당 API를 가지고, 클라이언트를 구현할 수 있게 서버는 인증부터 요청 응답을 `전부 RestAPI형태`로 처리합니다.
- 프론트엔드 부분은 ThymeLeaf Template 엔진과 `HTML, CSS, JS`을 통해 구현하였습니다.
- 일반적인 출고 뿐만 아니라, BOM품목을 형성하여 BOM단위로 출고할 수 있는 기능을 제공합니다.

---

## 프로젝트 기한 및 인원

- `2023-5-28 ~ 2023-07-16`
- `인원 : 1명(본인)`
- `개발 범위 : 풀스택`

---

## 기술 스택


|  Back-end  |     DB      |  Dependencies   | Front-end  |
|:----------:|:-----------:|:---------------:|:----------:|
| SpringBoot |    MySQL    | Spring Security |    HTML    |
|    Java    | H2 DataBase |      jjwt       |    CSS     |
|    JPA     |    Redis    |    Thymeleaf    | JavaScript |
|            |             |     Lombok      |      |
|            |             |      Junit      |  |

---

## 프로젝트 구조

- `필수 사용 : Redis, MySQL, SpringBoot`
- `권장 사용 : NGINX`
- `Spring Boot : API통신 및, 템플릿엔진을 통해 웹페이지를 제공합니다.`
- `Redis Caching : Redis를 통해, 일부데이터를 캐싱합니다. 외부 서버에서 Caching을 하기 때문에,
분산 환경에서도 동작이 가능합니다.`
- `Redis Locking : Redis를 통해, 동시성 접근 이슈를 방지합니다. 특정 데이터를 접근할 때,
접근 ID를 기준으로 Locking이 걸리며, Lock된 ID에 다른 요소가 접근하면, 접근에 실패합니다.
또한, Redis를 통해 Locking을 수행하므로, 분산 환경에서도 동작이 가능합니다.`
- `MySQL : 실질적인 데이터는 MySQL에 저장됩니다.`
- `NGINX : 해당 프로젝트는 Docker환경 및 NGINX환경을 지원합니다.`

---

## ERD 설계

![image](https://github.com/J-HyeonSeo/J-InVenTo/assets/47245112/6560b8f0-e03c-4cc2-9ab8-3a84ea1ea8c3)

---

## 인증 방식
- 초반에 `DB`가 비어있는 상태에서 로그인을 수행하면, `id: admin, pass: admin` 계정이 형성됩니다.
- 이는 `관리자 권한`만 가지고 있는 계정이며, 이를 통해 `계정을 추가`적으로 생성하거나 수정하거나, 유저로그를 볼 수 있습니다.
- 인증 방식은 서버의 부하를 줄이기 위해 `Json Web Token` 방식을 사용합니다.(인증시 user DB접근 X)
- 보안을 위해 `AccessToken`뿐만 아니라, `RefreshToken`을 추가하였습니다.
- 로그인시 `IP`와 다른 IP에서 RefreshToken를 사용할 경우에 RefreshToken를 차단할 수 있습니다.(옵션)
- 로그인시 `User-Agent`와 다른 User-Agent에서 RefreshToken를 사용할 경우에도 RefreshToken을 차단할 수 있습니다.(옵션)
- RefreshToken을 대조할 때는, `Redis`를 사용하여, `속도를 향상`합니다.

---

## application.properties 설정 파일

```
#Spring-Boot Server PROPERTIES

#server.address=localhost -> 스프링부트서버주소
server.port=8080 -> 스프링부트서버포트

#REDIS SERVER PROPERTIES (Caching & Locking Server)

spring.redis.host=jinvento-redis -> redis 서버 주소
spring.redis.port=6379 -> redis 서버 포트

#MySQL SERVER PROPERTIES ( DATABASE SERVER )
spring.datasource.url=jdbc:mysql://jinvento-mysql:3306/inventory?useSSL=false&allowPublicKeyRetrieval=true&characterEncoding=UTF-8&serverTimezone=Asia/Seoul&useLegacyDatetimeCode=false -> mysql 서버 주소 및 포트 및 설정
spring.datasource.username=inventory -> mysql 로그인 유저
spring.datasource.password=inventory -> mysql 로그인 암호

#Json Web Token Secret Key
#Json Web Token를 암,복호화 하는 비밀키 설정.(최대한 어렵게 해야함. 그대로 사용 절대 XXX)
spring.jwt.secret=VGhpcy1pcy1teS1maXJzdC1qd3QtdG9rZW4ta2V5LWdlbmVyYXRlLXNvLWV4Y2l0ZWQtd2VsbAo=

#Whether to save the GET method.
log.save-get-log=true -> GET요청을 log에 남길지 확인 (GET요청이 너무 많다고 생각하면 false설정)

#Whether access is possible only with the IP recorded when logging in
auth.access-ip.origin=true -> 로그인 할 때 기록된 IP만 RefreshToken 사용가능.

#Whether access is possible only with the User-Agent recorded when logging in
auth.access-user-agent-origin=true -> 로그인 할 때 기록된 User-Agent만 RefreshToken 사용가능.

#Whether to use NGINX And Client ip header name included in NGINX

nginx.use=false -> NGINX 사용여부
nginx.origin-ip.header=x-original-forwarded-for -> NGINX 사용시 x-forward-for 헤더이름 기입.
```

---

## 프로젝트 배포 파일

- `Dockerfile` : 스프링부트의 Docker설정 file입니다.
- `NGINX/Dockerfile` : NGINX의 Docker설정 file입니다.
- `NGINX/default.conf` : NGINX의 설정을 지정하는 파일입니다.
- `Docker/docker-build.sh` : 프로젝트를 빌드하고, 스프링부트 및 NGINX 도커 이미지를 만듭니다. (윈도우에서는 WSL로 실행가능함.)
- `Docker/docker-compose.yml` : 로컬 환경 테스트를 위해, 제공되는 컴포즈 파일입니다. (docker-compose up -d 로 실행가능.)

---

## API 문서
- 프로젝트 내 `Swagger` 문서 및 `Dto파일 내 ~Response` 클래스 참조.

---

## 컨트롤러 맵핑 정보

`일반 유저`
- `/` : 메인 페이지
- `/login` : 로그인 페이지
- `/edit-password` : 유저 비밀번호 변경
- `/page/purchase-show` : 구매 조회 전용 페이지
- `/page/purchase-manage` : 구매 조회 및 관리 페이지
- `/page/purchase-chart` : 구매 차트 통계 페이지
- `/page/inbound-show` : 입고 조회 전용 페이지
- `/page/inbound-manage` : 입고 조회 및 관리 페이지
- `/page/outbound-show` : 출고 조회 전용 페이지
- `/page/outbound-manage` : 출고 조회 및 관리 페이지
- `/page/outbound-chart` : 출고 차트 통계 페이지
- `/page/plan-show` : 출고 계획 조회 전용 페이지
- `/page/plan-manage` : 출고 계획 조회 및 관리 페이지
- `/page/stocks-show` : 재고 조회 전용 페이지
- `/page/stocks-manage` : 재고 조회 및 관리 페이지
- `/page/product-show` : 품목 조회 전용 페이지
- `/page/product-manage` : 품목 조회 및 관리 페이지

`관리자`
- `/admin/page/admin-register` : 관리자 권한으로 계정 생성 (+ 권한 부여)
- `/admin/page/admin-modify` : 관리자 권한으로 계정 정보 수정 및 비밀번호 초기화
- `/admin/page/admin-log` : user가 요청한 request기록을 조회함.

---

## 프로젝트 시연

### 로그인

![login](https://github.com/J-HyeonSeo/J-InVenTo/assets/47245112/fd561926-0fb1-4c58-9c89-bef533a332af)

### 비밀번호 변경

![edit-password](https://github.com/J-HyeonSeo/J-InVenTo/assets/47245112/33dca5bb-b95e-4e2d-a0fd-c1527e7fff3f)

### 메인 페이지

![main-page](https://github.com/J-HyeonSeo/J-InVenTo/assets/47245112/ac0dbc46-25dd-43db-bb41-bc4fab84ef06)

### 품목 추가

![add-product](https://github.com/J-HyeonSeo/J-InVenTo/assets/47245112/598697ab-919a-4fe1-8a51-963727324362)

### BOM 추가

![add-bom](https://github.com/J-HyeonSeo/J-InVenTo/assets/47245112/1d97dd40-9d7c-436b-8246-e4318be27020)

### 출고 계획 추가

![add-plan](https://github.com/J-HyeonSeo/J-InVenTo/assets/47245112/ab78dc45-f46d-4b66-bde6-07832ba20403)

### 구매 요청

![add-purchase](https://github.com/J-HyeonSeo/J-InVenTo/assets/47245112/4aa3a0d2-7c13-4143-bfad-8bc414bdf3dd)

### 입고 수행

![add-inbound](https://github.com/J-HyeonSeo/J-InVenTo/assets/47245112/1c4d5aa9-4c6b-46e9-8ecd-2afc1f661737)

### 재고 조회

![show-stocks](https://github.com/J-HyeonSeo/J-InVenTo/assets/47245112/a44e2142-0090-4678-9ea8-9ec4345d08ab)

### 출고 수행

![add-outbound](https://github.com/J-HyeonSeo/J-InVenTo/assets/47245112/5064e1f7-7aaf-4d85-b2a3-7721150b0aba)

### 구매 통계 차트

![purchase-chart](https://github.com/J-HyeonSeo/J-InVenTo/assets/47245112/8e731680-6624-40e9-a11c-e88fcff220d5)

### 회원 가입 (관리자 페이지)

![admin-register](https://github.com/J-HyeonSeo/J-InVenTo/assets/47245112/d592b6e1-ce6b-461c-9217-ff4371442849)

### 회원 수정 (관리자 페이지)

![admin-modify](https://github.com/J-HyeonSeo/J-InVenTo/assets/47245112/30f1630d-6aed-46c1-9c0e-e965b42c0dff)

### 유저 로그 조회 (관리자 페이지)

![admin-log](https://github.com/J-HyeonSeo/J-InVenTo/assets/47245112/d00b5a40-a82b-4595-a6c5-5ad7874bc8af)
