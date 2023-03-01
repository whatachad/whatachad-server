# Project Setting

## Pull Request
- feature 브랜치 작성 및 remote 생성
- push하고 이를 Pull request 생성
- Approve 되면 Merge 하기

## swagger
- http://localhost:8080/swagger-ui/index.html

## h2
- jdbc:h2:mem:whatachad
- http://localhost:8080/h2-console/

## 인증/인가
- 회원가입 (아이디 : admin / 비밀번호 : admin) -> admin용 계정
- login 후 access token 을 발급 받고
- http://localhost:8080/swagger-ui/index.html 에서 자물쇠 모양을 누르고 Bearer(한 칸 띄우고)access token 입력
