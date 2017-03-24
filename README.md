# video-upload-service-aws #

Use Spring Boot, AWS, and Docker to perform video upload.

### What is this repository for? ###


This repository demonstrate how to integrate different software components to allow user upload video file to the cloud, mainly AWS S3. The application is written in Java using Spring Framework. The deployment of the application is using docker. 


## Prerequisites

Create EC2 instance in AWS to run video upload application.

* Modify inbound rule in security group to allow 8080 port from
anywhere



Create a S3 bucket in AWS. And have following information ready

* Name of s3 bucket
* AWS Region
* AWS credentials accessKey
* AWS credentials secretKey




Create docker hub account to hold docker container image.

Make sure you have gradle, jdk, docker installed. 



## How to build the artifact, deploy docker container

* git clone https://github.com/doughuang168/video-upload-service-aws.git 
* cd video-upload-service-aws
* ./gradlew build
*  docker build -t yournamespace/video-upload .
*  docker login
*  docker push yournamespace/video-upload
*  if you don't use docker, please use video-upload.sh to launch the application. make sure video-upload.sh is executable 


In the EC2 instance run the application using docker
 
*  docker login
*  docker pull yournamespace/video-upload
*  get envfile.template from repository 
*  cp envfile.template envfile.txt
*  fill in the information in envfile.txt
*  docker run -d --name videoupload --env-file envfile.txt -p 8080:8888 video-upload


## Security

Security is implemented in Spring Security. User need to be authenticated to be able to login and perform operation. There are 3 per-populate users, each of them have different role.

* ADMIN: can create new user with proper role
* USER: normal user
* VIDEOLOAD: can upload video
 
User have different role will have different view. The video upload instruction please see the pdf document. 

