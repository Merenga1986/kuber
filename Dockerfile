FROM eclipse-temurin:17-jre-alpine
# для devapp — 8080, для prodapp — поменять на 8081
EXPOSE 8080
ADD target/kuber-0.0.1-SNAPSHOT.jar myapp.jar
ENTRYPOINT ["java","-jar","/myapp.jar"]
