#!/bin/bash
queue_name=YourQueueName
queue_endpoint=YourAWSQueueURL
cloud_aws_s3_bucket=YourAWSS3Bucketname
cloud_aws_region=us-west-2
cloud_aws_credentials_accessKey=YourAccessKey
cloud_aws_credentials_secretKey=YourSecretKey
video_upload_folder=/tmp/
multipart_maxFileSize=100MB
multipart_maxRequestSize=100MB

java -Dserver.port=8888 -Dqueue.name=`echo $queue_name` -Dqueue.endpoint=`echo $queue_endpoint`    \
     -Dcloud.aws.s3.bucket=`echo $cloud_aws_s3_bucket` -Dcloud.aws.region=`echo $cloud_aws_region` \
     -Dcloud.aws.credentials.accessKey=`echo $cloud_aws_credentials_accessKey` \
     -Dcloud.aws.credentials.secretKey=`echo $cloud_aws_credentials_secretKey` \
     -Dvideo.upload.folder=`echo $video_upload_folder`                         \
     -Dmultipart.maxFileSize=`echo $multipart_maxFileSize` -Dmultipart.maxRequestSize=`echo $multipart_maxRequestSize`  \
     -jar build/libs/spring-video-upload-aws-1.0-SNAPSHOT.jar
