# J-InVenTo (재고관리 프로그램)

---

## 프로젝트 소개

- 스프링부트기반으로 재고관리 시스템을 구현한 프로젝트 입니다.
- 스프링부트로 재고관리에 관련된 API를 작성하였습니다.
- 구매 -> 입고 -> 출고, 프로세스를 따르는 간단한 재고관리 프로그램입니다.
- 해당 API를 가지고, 클라이언트를 구현할 수 있게 서버는 인증부터 요청 응답을 전부 API형태로 처리합니다.
- 예시를 보여주기 위해, 간단하게 HTML, CSS, JS로 프론트엔드 부분을 제작하였습니다.

## 프로젝트 기한 및 인원

- 2023-5-28 ~ 2023-07-16
- 인원 : 1명(본인)
- 개발 범위 : 풀스택

## 기술 스택

Back-end : SpringBoot, Java

DB : MySQL(배포용), H2 DataBase(통합테스트), Redis(Caching 및 Locking)

Dependencies : Spring Security(인증), jjwt(인증), thymeleaf(템플릿엔진), lombok(편의성), Junit(단위 및 통합테스트)

Front-end : HTML, CSS, JavaScript

## 프로젝트 구조

- 필수 사용 : Redis, MySQL, SpringBoot
- 권장 사용 : NGINX

- Spring Boot : API통신 및, 템플릿엔진을 통해 웹페이지를 제공합니다.
- Redis Caching : Redis를 통해, 일부데이터를 캐싱합니다. 외부 서버에서 Caching을 하기 때문에,
분산 환경에서도 동작이 가능합니다.
- Redis Locking : Redis를 통해, 동시성 접근 이슈를 방지합니다. 특정 데이터를 접근할 때,
접근 ID를 기준으로 Locking이 걸리며, Lock된 ID에 다른 요소가 접근하면, 접근에 실패합니다.
또한, Redis를 통해 Locking을 수행하므로, 분산 환경에서도 동작이 가능합니다.
- MySQL : 실질적인 데이터는 MySQL에 저장됩니다.
- NGINX : 해당 프로젝트는 Docker환경 및 NGINX환경을 지원합니다.

## 인증 방식
- 초반에 DB가 비어있는 상태에서 로그인을 수행하면, id: admin, pass: admin 계정이 형성됩니다.
- 이는 관리자 권한만 가지고 있는 계정이며, 이를 통해 계정을 추가적으로 생성하거나 수정하거나, 유저로그를 볼 수 있습니다.
- 인증 방식은 서버의 부하를 줄이기 위해 Json Web Token 방식을 사용합니다.(인증시 user DB접근 X)
- 보안을 위해 AccessToken뿐만 아니라, RefreshToken을 추가하였습니다.
- 로그인시 IP와 다른 IP에서 RefreshToken를 사용할 경우에 RefreshToken를 차단할 수 있습니다.(옵션)
- 로그인시 User-Agent와 다른 User-Agent에서 RefreshToken를 사용할 경우에도 RefreshToken을 차단할 수 있습니다.(옵션)
- RefreshToken을 대조할 때는, Redis를 사용하여, 속도를 향상합니다.

## application.properties 설정 파일

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

---

## 프로젝트 배포 파일

- Dockerfile : 스프링부트의 Docker설정 file입니다.
- NGINX/Dockerfile : NGINX의 Docker설정 file입니다.
- NGINX/default.conf : 로드밸런서 및 리버스프록시 설정을 지정하는 파일입니다.
- Docker/docker-build.sh : 프로젝트를 빌드하고, 스프링부트 및 NGINX 도커 이미지를 만듭니다. (윈도우에서는 WSL로 실행가능함.)
- Docker/docker-compose.yml : 로컬 환경 테스트를 위해, 제공되는 컴포즈 파일입니다. (docker-compose up -d 로 실행가능.)

## 프로젝트 배포 방식

- 단순 외부 배포 : 서버가되는 컴퓨터에 redis 및 mysql, 스프링부트를 실행하여 배포하는 방법입니다.
- 단순 외부 도커 배포 : redis와 mysql 이미지를 도커에서 실행하고, mysql에는 inventoryDB를 만들어줍니다.
spring boot 프로젝트를 .jar로 빌드하고, 이를 docker에 올려서 실행합니다.
- 분산 외부 배포 : 분산된 각각의 서버에, redis, mysql, 스프링부트 서버를 올려서 배포합니다.
이 때 NGINX을 사용하여, default.conf의 upstream에 분산된 서버를 기입해야합니다.
외부로 노출되는 서버는 NGINX만 해당됩니다. NGINX를 통한 배포시에는, src/main/com/jhsfully/inventoryManagement/MvcConfiguration에서 CORS 주소에 NGINX의 주소를 추가해야합니다.
- 분산 외부 도커 배포 : Dockerfile을 통해 각각의 이미지를 만들어, 도커환경에 배포합니다.
- 로컬 환경 도커 배포 : docker-build.sh를 수행한 후에, docker-compose up -d를 수행하여, 로컬환경 배포를 수행합니다. (테스트 용도)

## API 문서
- 프로젝트 내 Swagger 문서 및 Dto파일 내 *Response 클래스 참조.