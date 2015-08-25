##Run server:##
./gradlew clean server:run

##Run client:##
./gradlew clean client:run

##Build docker image##
./gradlew clean server:buildDocker

##Run docker image##
sudo docker run -p 8088:8088 image_name_or_id

-p external_port:internal_container_port
