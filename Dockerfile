#FROM ubuntu:trusty-20160711
# Install ubuntu instllation tools and the java 8 repo
#RUN apt-get update && apt-get -y install software-properties-common && add-apt-repository -y ppa:webupd8team/java
# Auto accept the java 8 licence
#RUN echo debconf shared/accepted-oracle-license-v1-1 select true | debconf-set-selections
#RUN echo debconf shared/accepted-oracle-license-v1-1 seen true | debconf-set-selections
# Install java8, git and wget
#RUN apt-get update && apt-get -y install oracle-java8-installer git wget vim
FROM java:8
RUN apt-get update && apt-get -y --no-install-recommends install \
    ca-certificates \
    curl
RUN gpg --keyserver ha.pool.sks-keyservers.net --recv-keys B42F6819007F00F88E364FD4036A9C25BF357DD4
RUN curl -o /usr/local/bin/gosu -SL "https://github.com/tianon/gosu/releases/download/1.4/gosu-$(dpkg --print-architecture)" \
    && curl -o /usr/local/bin/gosu.asc -SL "https://github.com/tianon/gosu/releases/download/1.4/gosu-$(dpkg --print-architecture).asc" \
    && gpg --verify /usr/local/bin/gosu.asc \
    && rm /usr/local/bin/gosu.asc \
    && chmod +x /usr/local/bin/gosu

EXPOSE 8888
########
RUN  mkdir /opt/video
RUN  mkdir /opt/video/build
RUN  mkdir /opt/video/gradle
RUN  mkdir /opt/video/src

COPY build             /opt/video/build
COPY gradle            /opt/video/gradle
COPY src               /opt/video/src
COPY build.gradle      /opt/video/

########
RUN mkdir /home/video
RUN  echo "#!/bin/bash                                    " >/home/video/start-service.sh
RUN  echo "cd /opt/video        "                           >>/home/video/start-service.sh

RUN  echo "java -Dserver.port=8888 -Dqueue.name=\`echo \$queue_name\` -Dqueue.endpoint=\`echo \$queue_endpoint\` -Dcloud.aws.s3.bucket=\`echo \$cloud_aws_s3_bucket\` -Dcloud.aws.region=\`echo \$cloud_aws_region\` -Dcloud.aws.credentials.accessKey=\`echo \$cloud_aws_credentials_accessKey\` -Dcloud.aws.credentials.secretKey=\`echo \$cloud_aws_credentials_secretKey\` -Dvideo.upload.folder=\`echo \$video_upload_folder\` -Dmultipart.maxFileSize=\`echo \$multipart_maxFileSize\` -Dmultipart.maxRequestSize=\`echo \$multipart_maxRequestSize\` -jar build/libs/spring-video-upload-aws-1.0-SNAPSHOT.jar" >>/home/video/start-service.sh


RUN chmod +x /home/video/start-service.sh

COPY entrypoint.sh /usr/local/bin/entrypoint.sh
RUN  chmod +x /usr/local/bin/entrypoint.sh
CMD  entrypoint.sh /home/video/start-service.sh
