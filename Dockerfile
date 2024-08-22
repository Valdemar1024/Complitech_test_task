FROM alpine:3.20.2

RUN apk update \
    && apk add --no-cache openjdk21-jre \
    && rm -rf /var/cache/apk/* /tmp/* /var/tmp/*

EXPOSE 8080

COPY ./build/libs/user-manager-SNAPSHOT.jar ./user-manager.jar

CMD ["java", "-jar", "user-manager.jar"]
