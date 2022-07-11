FROM openjdk:8-jre-alpine
LABEL maintainer="liubity <330450826@qq.com>"
EXPOSE 80
COPY ./management_platform.jar /management_platform.jar
ENTRYPOINT ["java","-jar","-Duser.timezone=GMT+08","/management_platform.jar","--spring.profiles.active=prod"]
