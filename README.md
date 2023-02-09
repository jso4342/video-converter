# video-converter
영상을 업로드하여 사이즈를 변환하는 API 서버입니다
<hr>

## 🛠 **프로젝트 빌드 및 서버 실행 방법**

- local에서 사용할 시 MySql을 설치해주세요


1. 상단의 Code 버튼을 눌러 경로를 복사한 후 클론 받습니다.

```
$ git clone https://github.com/jso4342/video-converter.git
```

2. 패키지를 설치합니다.

### 📣 [배경 및 공통 요구사항]
- 데이터베이스 환경은 별도로 제공하지 않습니다.
 **RDB중 원하는 방식을 선택**하면 되며, sqlite3 같은 별도의 설치없이 이용 가능한 in-memory DB도 좋으며, 가능하다면 Docker로 준비하셔도 됩니다.
- 단, 결과 제출 시 README.md 파일에 실행 방법을 완벽히 서술하여 DB를 포함하여 전체적인 서버를 구동하는데 문제없도록 해야합니다.
- 데이터베이스 관련처리는 raw query가 아닌 **ORM을 이용하여 구현**합니다.
- Response Codes API를 성공적으로 호출할 경우 200번 코드를 반환하고, 그 외의 경우에는 아래의 코드로 반환합니다.

<br>


<br>

### 📙 [구현한 API 목록]
✔️ 영상 업로드 및 변환 API
- 썸네일 추출 및 저장 기능 구현

✔️ 영상의 상세 정보를 조회할 수 있는 API

```java
// 요청 :  
GET /video/{id}

// 응답 예시 : 
{
  "id": 1,
  "title": "title",
  "original": {
    "width": 480,
    "height": 270,
    "videoUrl": "http://localhost:8085/imagePath/7c0a4460-283c-4e03-9fca-402930c8e080.mp4"
  },
"resized": {
    "width": 360,
    "height": 202,
    "videoUrl": "http://localhost:8085/imagePath/2a940a0b-a2b1-46c5-b5ea-502e917f909f.mp4"
  },
  "thumbnailUrl": "http://localhost:8085/imagePath/thumbnail/7c0a4460-283c-4e03-9fca-402930c8e080_thumbnail.jpg",
  "createdAt": "2023-02-09T07:06:24"
}
```

<br>
