FROM eclipse-temurin:17-jre-alpine
COPY selfcloud-security-application/target/selfcloud-security-application-1.0.5.jar selfcloud-security-1.0.5.jar
EXPOSE 8090
ENTRYPOINT ["java","-jar","/selfcloud-security-1.0.5.jar"]