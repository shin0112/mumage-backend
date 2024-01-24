# MUMAGE powered by Karlo API
## AI가 생성해주는 이미지와 함께 음악을 공유하는 SNS 서비스

### 🏆 2023 경희대 컴퓨터공학과 트랙 스터디 대상

### BackEnd Tech Stack
<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"> <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"> <img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white"> <img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white"> <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white"> <img src="https://img.shields.io/badge/redis-DC382D?style=for-the-badge&logo=redis&logoColor=white"> <img src="https://img.shields.io/badge/intellijidea-000000?style=for-the-badge&logo=intellijidea&logoColor=white"> <img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white"> <img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white">


### FrontEnd url
https://github.com/Musicstagram/mumage-frontend

### ERD
![mumage-backend-erd](https://github.com/Musicstagram/mumage-backend/assets/83761128/82c49e20-b00b-4c41-ab93-aaf58650c128)

---
### 주요 기능
✅ 회원가입, 로그인
- 회원 가입 : 아이디, 비밀번호, 이름, 닉네임 입력
- 로그인 : 아이디, 비밀번호 입력 - jwt토큰 발급

<img width="600" alt="스크린샷 2023-11-23 오후 9 55 07" src="https://github.com/Musicstagram/mumage-frontend/assets/49388937/bb47c5f5-11ec-4f31-8670-0e575a5c5512">

✅ 이미지 생성 및 업로드
- Spotify API : 곡 검색, 리스트 업, 미리듣기 제공
- OpenAI API : 곡 가사를 바탕으로 이미지 생성 위한 프롬프트 생성  
- Karlo API : 프롬프트를 기반으로 이미지 생성

<img width="600" alt="스크린샷 2023-11-23 오후 9 56 22" src="https://github.com/Musicstagram/mumage-frontend/assets/49388937/b1071abf-da6f-4d7a-a4b5-c7a36af156a4">

✅ 게시물 피드
- 게시물 별 미리듣기, 외부링크(Spotify) 제공
- 서비스 사용 튜토리얼 지원

<img width="600" alt="스크린샷 2023-11-23 오후 9 59 19" src="https://github.com/Musicstagram/mumage-frontend/assets/49388937/f54a77a7-e982-4f6d-83dc-1b809c2e3c85">

✅ 게시물 추천과 탐색
- 반응형 웹 디자인
- 사용자의 UI 선택 가능

<img width="600" alt="스크린샷 2023-11-23 오후 10 01 13" src="https://github.com/Musicstagram/mumage-frontend/assets/49388937/800e9f78-2ad1-4c07-ad64-13ddf9966c0e">

✅ 마이 페이지
- 프로필 편집, 팔로우/팔로잉 확인
- 업로드, 좋아요 한 게시물 확인
- 개발자에게 메일로 문의 기능 지원

<img width="600" alt="스크린샷 2023-11-23 오후 10 01 57" src="https://github.com/Musicstagram/mumage-frontend/assets/49388937/0390a931-c3d6-4718-af6e-618bb6bf7b29">

### Introduction
- shin0112 : wnslcosltimo12@khu.ac.kr
  - 회원 : 회원 가입/탈퇴/변경, 로그인/로그아웃
  - 게시글 : 게시글 추천(장르, 팔로우), 조회(장르, 노래, 가수, 회원)
- leeeunda : da_un0219@khu.ac.kr
  - 게시글 : 게시글 작성/조회/삭제, 댓글 작성/삭제, 팔로우/팔로잉, 좋아요
