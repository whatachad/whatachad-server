# Project Setting

## Pull Request
- feature 브랜치 작성 및 remote 브랜치 생성
- push하고 이에 대한 Pull Request 생성
- Approve 되면 PR을 요청한 사람이 Merge 하기

## swagger
- 아래 URL로 실행 가능
- 로컬 : http://localhost:3000/swagger-ui/index.html
- 배포 : http://129.154.212.45:3000/swagger-ui/index.html 

## H2
- [H2 DBMS 설치](http://h2database.com/html/main.html)
- 애플리케이션 서버를 실행한 후
- 로컬 : http://localhost:3000/h2-console
- 배포 : http://129.154.212.45:3000/h2-console
- 각 환경에 맞는 링크로 접속
- JDBC URL에 jdbc:h2:mem:whatachad을 입력
- username: sa / password: 
- connect(연결)

## 인증/인가
- 회원가입 (아이디 : admin / 비밀번호 : admin) -> admin용 계정
- login 후 access token 을 발급 받고
- 로컬 : http://localhost:3000/swagger-ui/index.html
- 배포 : http://129.154.212.45:3000/swagger-ui/index.html 
- 위 링크에서 자물쇠 모양을 누르고 Bearer(한 칸 띄우고)access token 입력
- 빠른 authorization을 원한다면 debug만 입력
