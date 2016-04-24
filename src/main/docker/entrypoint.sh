#!/bin/bash

while true; do
	java -Xmx2g -Dcom.mongodb.slaveAcceptableLatencyMS=1 -server -Djava.security.egd=file:/dev/./urandom -jar /tmp/app-spellchecker.jar;
	# after jvm crashes let server take a rest for 10 minutes
	sleep 600;
done