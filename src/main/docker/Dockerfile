FROM tomcat:jre9
 
MAINTAINER Glosbe Parfieniuk i Stawinski sp. j.

ENV DEBIAN_FRONTEND noninteractive

# web app
EXPOSE 7180

COPY hunspell /var/hunspell
COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

ENV APP_JAR Spellchecker-0.0.1.jar

ADD ${APP_JAR} /tmp/
RUN touch /tmp/${APP_JAR} && mv /tmp/${APP_JAR} /tmp/app-spellchecker.jar

ENTRYPOINT ["/entrypoint.sh"]
