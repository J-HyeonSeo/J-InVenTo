#!bin/bash

# 버전 설정
VERSION='0.0.1'

#상위 디렉토리로 이동
cd ..

#gradle로 빌드 수행
./gradlew clean build -x test

ROOT_PATH=`pwd`
echo $ROOT_PATH

echo 'Spring Boot Docker Image Bulid'
cd $ROOT_PATH && docker build -t jinvento:$VERSION .
echo 'Spring Boot Docker Image build... Done!!'

echo 'NGINX Docker Image Bulid'
cd $ROOT_PATH/NGINX && docker build -t jinvento-nginx:$VERSION .
echo 'NGINX Docker Image build... Done!!'