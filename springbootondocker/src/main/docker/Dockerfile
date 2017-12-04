FROM java:8
VOLUME /tmp
RUN mkdir -p /apps/springbootdemo
COPY /maven/springbootondocker.jar /apps/springbootdemo
WORKDIR /apps/springbootdemo
EXPOSE 8080
CMD ["java", "-jar", "springbootondocker.jar"]