FROM openjdk:17-alpine
ARG JAR_FILE=demo-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} shoplive.jar
ENTRYPOINT ["java","-jar","/shoplive.jar"]

RUN apk upgrade --no-cache && \
    apk add --update bash \
    ffmpeg

COPY --from=mwader/static-ffmpeg:5.1.2 /ffprobe /usr/local/bin/ffprobe
COPY --from=mwader/static-ffmpeg:5.1.2 /ffmpeg /usr/local/bin/ffmpeg