# 1. 베이스 이미지 (AWS니까 Amazon Corretto 17 버전 추천 - Java 17 기준)
FROM amazoncorretto:17

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. 빌드된 JAR 파일을 컨테이너로 복사
# (Gradle 빌드 시 보통 build/libs 안에 jar가 생김)
COPY build/libs/*.jar app.jar

# 4. 포트 열기 (스프링 부트 기본 포트 8080)
EXPOSE 8080

# 5. 실행 명령어
CMD ["java", "-jar", "app.jar"]