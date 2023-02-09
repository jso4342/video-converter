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
* 영상을 업로드하여 사이즈를 변환하는 API 서버를 제작합니다.
* 사용자나 별도의 인증은 구현하지 않아도 됩니다.
* 기본적인 요청과 응답은 json으로 구성하고 영상은 multipart로 업로드합니다.
* 빌드 도구는 gradle 혹은 maven을 사용합니다. framework은 자유롭게 선택하시면 됩니다. (gradle 사용)
* storage는 h2, mysql, redis, mongodb 등 편한 제품을 사용하시면 됩니다.         (mysql 사용) 
* 스토리지, 초기화 스크립트 및 빌드 결과물을 포함한 Dockerfile을 작성하여 docker run 혹은 docker compose를 통해 실행할 수 있도록 구성합니다.
* 서버의 구동 및 테스트 방법을 README.md로 작성합니다.

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
