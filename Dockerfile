FROM adoptopenjdk:11-jre-hotspot as builder
WORKDIR sales-booster-app
ARG JAR_FILE=app/target/*-exec.jar
COPY ${JAR_FILE} app.jar
RUN java -Djarmode=layertools -jar app.jar extract

FROM adoptopenjdk:11-jre-hotspot
WORKDIR app
COPY --from=builder sales-booster-app/dependencies/ ./
COPY --from=builder sales-booster-app/spring-boot-loader/ ./
COPY --from=builder sales-booster-app/snapshot-dependencies/ ./
ENV test=test
COPY --from=builder sales-booster-app/application/ ./
ENV JAVA_OPTS=""
CMD java -server ${JAVA_OPTS} org.springframework.boot.loader.JarLauncher
#CMD "java -server ${JAVA_OPTS} org.springframework.boot.loader.JarLauncher"
