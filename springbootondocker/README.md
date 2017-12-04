Welcome to Spring Boot + Docker + JAX-RS JAR Deployment Demo Application!
===================

Table of contents
-------------

[TOC]

Introduction
-------------
This is an example application created to demonstrate the JAR deployment and use of the Spring Boot and Docker.

The Spring Boot app uses JAX-RS (Jersey), and the Docker deployment is to Docker Toolbox on Windows 7/10 machine.


----------


Pre-requisites
-------------

Install Docker Toolbox for Windows on your laptop.


Testing
-------------

Docker Commands to setup the environment

(1a) If you are using setting up docker environment for first time

```
docker-machine create --driver virtualbox {machine name}
```

where {machine name} could be replaced by a test machine instance name without spaces.

(1b) If you already have a docker installation and docker machine setup.

```
docker-machine start {machine name}
```

(2) To setup the environment run the command

```
docker-machine env {machine name}
```

Copy and paste all lines in the output that begin with SET.

**Note:**

Some other useful docker commands in case of issues

- To delete the machine (to recreate).
```
docker-machine rm {machine name}
```
- To regenerate certs of the machine in case of corrupted certs.
```
docker-machine regenerate-certs {machine name}
```

Maven Commands to deploy and start the application

- To do the docker deployment, execute the command
```
mvn clean package docker:build docker:start
```
- To run the health check (integration tests)
```
mvn verify
```

Testing
-------------

To access the Demo Springboot application, access the URL

```
http://{DOCKER_HOST_IP}:8090/springbootondocker/topics/ef
```

where {DOCKER_HOST_IP} is to be replaced with the IP address in the Docker environment variable - DOCKER_HOST.

