FROM eclipse-temurin:21-jre-ubi9-minimal
LABEL org.opencontainers.image.source=https://github.com/GPIG-group-B-2024/gitHub-ai-assistant
LABEL org.opencontainers.image.description="A Github app that automatically produces solutions to pull requests"
COPY build/libs/ai-assistant-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]