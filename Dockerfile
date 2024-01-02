FROM apache/spark:v3.4.1
USER root
ARG http_proxy=http://web-proxy.houston.hpecorp.net:8080
ARG https_proxy=http://web-proxy.houston.hpecorp.net:8080
 
ENV http_proxy=$http_proxy
ENV https_proxy=$https_proxy
 
#Install python and pip module
RUN apt-get update && apt-get upgrade -y
 
 
#Copy the local jar file.
#COPY inputdata.json  /tmp/inputdata.json
COPY target/scala-2.12/spark-scala-k8-app-assembly-0.1.jar /opt/spark/jars/.