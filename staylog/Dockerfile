# 기존: FROM eclipse-temurin:17-jre
FROM eclipse-temurin:17-jre

# 1) 타임존 데이터 설치 (Debian/Ubuntu 베이스)
RUN apt-get update && apt-get install -y --no-install-recommends tzdata \
    && rm -rf /var/lib/apt/lists/*

# 2) 기본 TZ + JDBC 타임존 옵션 (ORA-01882 예방)
ENV TZ=Asia/Seoul
ENV JAVA_TOOL_OPTIONS="-Duser.timezone=Asia/Seoul -Doracle.jdbc.timezoneAsRegion=false"

WORKDIR /
ARG JAR_FILE=target/*-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

EXPOSE 9000
ENTRYPOINT ["java","-jar","/app.jar"]
