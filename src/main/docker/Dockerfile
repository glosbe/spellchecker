FROM java:8
 
MAINTAINER Glosbe Parfieniuk i Stawinski sp. j.

ENV DEBIAN_FRONTEND noninteractive

# web app
EXPOSE 5612

ENV APP_JAR Spellchecker-0.0.1.jar
	
ADD ${APP_JAR} /tmp/
RUN touch /tmp/${APP_JAR} && mv /tmp/${APP_JAR} /tmp/app.jar
    
ENTRYPOINT ["java","-Xmx25g","-Dcom.mongodb.slaveAcceptableLatencyMS=1","-server","-Djava.security.egd=file:/dev/./urandom","-jar","/tmp/app.jar"]
