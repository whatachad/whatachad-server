# 🔱 What a Chad
## 프로젝트 소개
What a Chad 프로젝트는 자기 계발에 열정을 다하는 모든 분들에게 도움이 될 수 있는 서비스를 구현하기 위해 시작되었습니다.   

주변 스포츠 시설을 검색하고, 해당 스포츠 시설에 대한 사용자의 리뷰도 찾아볼 수 있습니다.   
뿐만 아니라 철봉이나 평행봉 같은 야외 운동 기구에 대한 위치 정보와 사용 후기 등을 유저가 직접 등록하여 많은 사람들이 공유할 수 있도록 합니다.   

캘린더 형식으로 매일 가계부를 관리하고, 오늘 할 일을 기록하고 체크하는 기능을 제공합니다.   
팔로우된 유저끼리는 서로 할 일을 완수했는지 확인하여 더욱 의지를 불태울 수 있습니다.

## 👨‍💻 Contributors

| Contributor                                      | Role     |
| ------------------------------------------------ | -------- |
| [Seongyeon Ha](https://github.com/iDevBrandon)   | Frontend |
| [GyeongSeok Lee](https://github.com/lekosk2001)  | Frontend |
| [Jinhyeon Kwak](https://github.com/JinhyeonKwak) | Backend  |
| [HyunJung Kim](https://github.com/HyunJng)       | Backend  |

## Stacks
### Frontend

![Next.js](https://img.shields.io/badge/Next.js-000000?style=for-the-badge&logo=Next.js&logoColor=white)
![React](https://img.shields.io/badge/react-%2320232a.svg?style=for-the-badge&logo=react&logoColor=%2361DAFB)
![JavaScript](https://img.shields.io/badge/javascript-%23323330.svg?style=for-the-badge&logo=javascript&logoColor=%23F7DF1E)
![Styled Components](https://img.shields.io/badge/styled--components-DB7093?style=for-the-badge&logo=styled-components&logoColor=white)

### Backend
![Java](https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white)
![Spring](https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![SpringBoot](https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)

### Cloud
![AWS](https://img.shields.io/badge/aws-FF9900?style=for-the-badge&logo=aws&logoColor=white)

## 기능 목록
### ⭐️ 주요 기능 ⭐️
- 사용자 위치를 기준으로 반경 거리를 설정하여 스포츠 시설을 조회합니다.
- 야외 운동 기구 사용 후 위치 정보를 등록하고 후기를 남깁니다.
- 캘린더에서 Todo 리스트 전체를 훑어볼 수 있습니다.
- 최근 Todo 리스트나 가계부를 조회하여 할 일 완료 여부와 수입/지출 기록을 확인합니다.


## ERD
![WaC ERD (1)](https://github.com/whatachad/whatachad-server/assets/93817551/ce0f144d-42b0-4298-bc91-0f3774c6ffb5)


## 예외처리 계층 구조
<img width="2899" alt="예외처리 계층 구조" src="https://github.com/whatachad/whatachad-server/assets/93817551/3a07b8ec-780c-412b-908e-3e7d713548f6">


-----------------------------------

# Project Setting

## PostgreSQL
- port : 5432
- db : whatachad
- username : postgres
- password : postgres

## swagger에서 인증/인가
- 회원가입 (아이디 : admin / 비밀번호 : admin) -> admin용 계정
- login 후 access token 을 발급 받고
- 로컬 : http://localhost:3000/swagger-ui/index.html
- 배포 : http://whatachad.site:3000/swagger-ui/index.html 
- 위 링크에서 자물쇠 모양을 누르고 "Bearer" + " " + <access token\> 입력
- 빠른 authorization을 원한다면 debug만 입력
