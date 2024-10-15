# 여정

>여정은 여행지에서의 감동과 추억을 함께 나누는 **커뮤니티 플랫폼**입니다.
>
>사용자들은 자신이 경험한 특별한 장소를 소개하고, 후기를 통해 진정한 여행의 의미를 공유합니다.
>
>여정은 다양한 여행지에서 얻을 수 있는 **실질적인 팁과 영감을 제공**합니다.

<br>

###

```
├── 게시글
│   ├── 구글 API를 활용한 위치 검색
│   ├── 게시글 작성 / 수정 / 삭제
│   ├── 게시글 읽기
│   ├── 게시글 검색
│   ├── 전체 게시글 읽기
│
├── 댓글
│   ├── 평점 작성
│   ├── 댓글 작성 / 수정 / 삭제
│
├── 회원
│   ├── 회원가입 / 로그인
│   ├── 회원 정보 수정
│   ├── 아이디/비밀번호 찾기
│   ├── 작성한 게시글 조회
│   ├── 작성한 댓글 조회
|
├── 메일
│   ├── 이메일 인증 메일
│   ├── 아이디 찾기 메일
│   ├── 비밀번호 찾기 메일
│   ├── 5개월 미접속시 알림 전
```



## 사용 기술
<img src="https://github.com/user-attachments/assets/f965f8b7-843b-45f4-89e2-339e42af386d"> 

### 아키텍처
<img src="https://github.com/user-attachments/assets/1a984f5e-d593-45c3-a631-7432e528bef7"> 

## 팀소개

| 건우 | 은이 | 성빈 | 형준 |
|------|------|------|-------|
|  BE  |  BE  |  BE  |  FE |

<br>

## 프로젝트 구조
```
├── autocomplete
│   └── aplication
│   └── presentaion.dto
│
├── config
│   ├── SwaggerConfig
│   └── batch
│   └── exception
│   └── redis
│   └── util
│
├── domain
│   └── board
│       └── board
│       └── comment
│   └── member
│       └── email
│       └── member
│
├── security
│   ├── JwtProvider
│   ├── SecurityConfig
│   ├── SecurityUtil
│   └── filter
│   └── refreshtoken

```
