#!/bin/sh
INSTANCE_NAME="swip_pve"
export CATALINA_BASE="/cubeapps/www/swip_com/wasconf"
export TOMCAT_HOME="/cubeapps/pgms/apache-tomcat-9"
export CATALINA_HOME="$TOMCAT_HOME"
export JAVA_HOME="/cubeapps/pgms/openjdk11"
cd $TOMCAT_HOME/bin
./startup.sh
