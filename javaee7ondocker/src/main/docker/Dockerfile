FROM tomcat:8-jre8
VOLUME /tmp
EXPOSE 8080
ADD /maven/javaee7ondocker.war /usr/local/tomcat/webapps/
CMD ["catalina.sh", "run"]