FROM openjdk:11
RUN mkdir -p deploy
WORKDIR /deploy
COPY ./build/libs/inventoryManagement-0.0.1-SNAPSHOT.jar api.jar
ENTRYPOINT ["java", "-jar", "/deploy/api.jar"]