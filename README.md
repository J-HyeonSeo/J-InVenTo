![LOGO_BG_WH](https://github.com/J-HyeonSeo/inventory_management/assets/47245112/569db4a4-765e-4fa8-9ef9-a8640e498bfb)

# J-InVenTo (재고관리 프로그램)

---

## 프로젝트 소개

- `스프링부트`기반으로 재고관리 시스템을 구현한 프로젝트 입니다.
- 스프링부트로 재고관리에 관련된 API를 작성하였습니다.
- `구매 -> 입고 -> 출고`, 프로세스를 따르는 간단한 재고관리 프로그램입니다.
- 해당 API를 가지고, 클라이언트를 구현할 수 있게 서버는 인증부터 요청 응답을 `전부 RestAPI형태`로 처리합니다.
- 예시를 보여주기 위해, 간단하게 `HTML, CSS, JS`로 프론트엔드 부분을 제작하였습니다.

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

- Json Web Token를 암,복호화 하는 비밀키 설정.(최대한 어렵게 해야함. 그대로 사용 절대 XXX)
spring.jwt.secret=VGhpcy1pcy1teS1maXJzdC1qd3QtdG9rZW4ta2V5LWdlbmVyYXRlLXNvLWV4Y2l0ZWQtd2VsbAo=

#Whether to save the GET method.

log.save-get-log=true -> GET요청을 log에 남길지 확인 (GET요청이 너무 많다고 생각하면 false설정)

#Whether access is possible only with the IP recorded when logging in

auth.access-ip.origin=true -> 로그인 할 때 기록된 IP만 RefreshToken 사용가능.

#Whether access is possible only with the User-Agent recorded when logging in

auth.access-user-agent-origin=true -> 로그인 할 때 기록된 User-Agent만 RefreshToekn 사용가능.

#Whether to use NGINX And Client ip header name included in NGINX

nginx.use=false -> NGINX 사용여부

nginx.origin-ip.header=x-original-forwarded-for -> NGINX 사용시 x-forward-for 헤더이름 기입.
```

---

## 프로젝트 배포 파일

- `Dockerfile` : 스프링부트의 Docker설정 file입니다.
- `NGINX/Dockerfile` : NGINX의 Docker설정 file입니다.
- `NGINX/default.conf` : 로드밸런서 및 리버스프록시 설정을 지정하는 파일입니다.
- `Docker/docker-build.sh` : 프로젝트를 빌드하고, 스프링부트 및 NGINX 도커 이미지를 만듭니다. (윈도우에서는 WSL로 실행가능함.)
- `Docker/docker-compose.yml` : 로컬 환경 테스트를 위해, 제공되는 컴포즈 파일입니다. (docker-compose up -d 로 실행가능.)

---

## 프로젝트 배포 방식

- `단순 외부 배포` : 서버가되는 컴퓨터에 `redis` 및 `mysql`, 스프링부트를 실행하여 배포하는 방법입니다.
- `단순 외부 도커 배포` : `redis`와 `mysql` 이미지를 도커에서 실행하고, `mysql`에는 `inventory`DB를 만들어줍니다.
spring boot 프로젝트를 `.jar`로 빌드하고, 이를 `docker`에 올려서 실행합니다.
- `분산 외부 배포` : 분산된 각각의 서버에, `redis`, `mysql`, `스프링부트` 서버를 올려서 배포합니다.
이 때 `NGINX`을 사용하여, `default.conf`의 `upstream`에 분산된 `서버`를 기입해야합니다.
외부로 노출되는 서버는 `NGINX`만 해당됩니다. `NGINX`를 통한 배포시에는, `src/main/com/jhsfully/inventoryManagement/MvcConfiguration`에서 `CORS` 주소에 NGINX의 주소를 추가해야합니다.
- `분산 외부 도커 배포` : `Dockerfile`을 통해 각각의 이미지를 만들어, 도커환경에 배포합니다.
- `로컬 환경 도커 배포` : `docker-build.sh`를 수행한 후에, `docker-compose up -d`를 수행하여, 로컬환경 배포를 수행합니다. (테스트 용도)

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

![login](https://github.com/J-HyeonSeo/inventory_management/assets/47245112/ed525bf5-fa1c-4b42-a6d7-1e221d06431f)

### 비밀번호 변경

![edit-password](https://github.com/J-HyeonSeo/inventory_management/assets/47245112/5f46bd86-f5e4-4946-89df-4157498b8fcc)

### 메인 페이지

![main-page](https://github.com/J-HyeonSeo/inventory_management/assets/47245112/10c68777-2f45-4de0-b684-f91daae77c6b)

### 품목 추가

![add-product](https://github.com/J-HyeonSeo/inventory_management/assets/47245112/dadd3fca-da2b-4af0-ac6b-a7c081788520)

### BOM 추가

![add-bom](https://github.com/J-HyeonSeo/inventory_management/assets/47245112/ac6c593b-d4b2-4ede-8912-3fc46fd8216f)

### 출고 계획 추가

![add-plan](https://github.com/J-HyeonSeo/inventory_management/assets/47245112/ead0555e-de75-4a0b-bf4c-472b1aa3a5b6)

### 구매 요청

![add-purchase](https://github.com/J-HyeonSeo/inventory_management/assets/47245112/54ce0f56-08f5-4e6c-a68c-f5cbba19a97a)

### 입고 수행

![add-inbound](https://github.com/J-HyeonSeo/inventory_management/assets/47245112/17c888c7-f0d8-441e-8c32-616cbfff0d6c)

### 재고 조회

![show-stocks](https://github.com/J-HyeonSeo/inventory_management/assets/47245112/c139b9b0-f05f-4bc8-9227-64b9a4195c9d)

### 출고 수행

![add-outbound](https://github.com/J-HyeonSeo/inventory_management/assets/47245112/5c13c467-3c21-4862-99bd-03780e71f788)

### 구매 통계 차트

![purchase-chart](https://github.com/J-HyeonSeo/inventory_management/assets/47245112/53e8899d-88e4-4659-85c0-658d7164b446)

### 회원 가입 (관리자 페이지)

![admin-register](https://github.com/J-HyeonSeo/inventory_management/assets/47245112/7bef3097-9cf9-41a4-a851-ed7acf657f42)

### 회원 수정 (관리자 페이지)

![admin-modify](https://github.com/J-HyeonSeo/inventory_management/assets/47245112/fc174687-15ee-4eb1-9382-73af21403191)

### 유저 로그 조회 (관리자 페이지)

![admin-log](https://github.com/J-HyeonSeo/inventory_management/assets/47245112/6b8dd72c-6c25-4555-a003-d436935929a1)
