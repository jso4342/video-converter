# video-converter
영상을 업로드하여 사이즈를 변환하는 API 서버입니다
<hr>

## 📎개발 환경
* Openjdk/17
* Gradle/7.5.1
* MySQL/8.0.32
* MacOS (Intel)

<br>

## 🛠 **프로젝트 빌드 및 서버 실행 방법**
### A.DockerFile 을 이용하여 Docker Image 만들기(MySQL)

1. 상단의 Code 버튼을 눌러 경로를 복사한 후 클론 받습니다.
```
$ git clone https://github.com/jso4342/video-converter.git
```
2. 패키지를 설치합니다.
3. Dockerfile 경로로 디렉토리를 이동합니다.
```
$ cd build/libs/
```
4. 도커 네트워크 생성
```
$ docker network create springboot-mysql-net
```
5. 도커 환경에서 MySQL 구동
```
$ docker pull mysql:8.0
$ docker run --name db-mysql -p 3307:3306 --network springboot-mysql-net -e MYSQL_ROOT_PASSWORD=12345678 -e MYSQL_DATABASE=shoplive -d mysql:8.0
```
6. 도커 환경에서 springboot app 구동
* Dockerfile을 이용한 이미지 빌드
```
$ docker build -t springboot-mysql:1.0
```
* 빌드된 이미지로 컨테이너 구동
```
$ docker run -p 8085:8085 --name springboot-mysql --network springboot-mysql-net -d springboot-mysql:1.0
```

### B.Docker Hub 에서 Docker Image 내려 받기
* 아래 명령어를 통해 [Docker hub](https://hub.docker.com/repository/docker/jso4342/springboot-mysql/general) 에서 이미지를 내려 받을 수 있습니다.
```
$ docker push jso4342/springboot-mysql:tagname
```


<br>
<br>

### 📙 [구현한 API 목록]
✔️ 영상 업로드 및 변환 API
- 썸네일 추출 및 저장 기능 구현
* 서버 구동 후, [localhost:8085](http://localhost:8085/) 에 접속하면 요청을 보낼 수 있는 테스트페이지로 접속합니다. 
- <img width="777" alt="스크린샷 2023-02-14 오후 4 12 20" src="https://user-images.githubusercontent.com/57066693/218665125-cd22e103-7642-45fb-ade1-0d6d4cb1fcbf.png">

<br>

‼️ 위 페이지는 요청을 보내기 위해 만들어진 테스트 페이지로, 요청 성공 및 실패 시에 특별한 작동을 하지 않습니다. 
- 요청 성공 시 console 
```java
[DEBUG] 2023-02-14 ..... DispatcherServlet - Completed 201 CREATED
```
- 요청 실패 시 console 
```java
[ERROR] 2023-02-14 ..... FileDataDto - file must be in MP4 format. requested file is in = docx
...
[DEBUG] 2023-02-14 ..... DispatcherServlet - Completed 400 BAD_REQUEST
// 위 에러 메세지는 예시로, 실제로는 발생한 오류에 따라 다릅니다. 
```



<br> 

✔️ 영상의 상세 정보를 조회할 수 있는 API
* 서버 구동 후, `localhost:8085/video/{id}` 로 요청을 보낼 수 있습니다.
* 업로드한 영상, 변환한 영상, 썸네일 이미지 등의 리소스는 static resource로 제공됩니다.
* `localhost:8085/imagePath/업로드한 영상의 이름.mp4` - 업로드한 영상, 변환한 영상
* `localhost:8085/imagePath/thumbnail/업로드한 영상의 이름_thumbnail.jpg` - 업로드한 영상, 변환한 영상
