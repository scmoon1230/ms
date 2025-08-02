#!/bin/sh
INSTANCE_NAME="swip_pve"
APP_HOME="/cubeapps"
export TOMCAT_HOME="$APP_HOME/pgms/apache-tomcat-9"
export JAVA_HOME="$APP_HOME/pgms/openjdk11"
export CATALINA_BASE="$APP_HOME/www/$INSTANCE_NAME/wasconf"
export CATALINA_HOME="$TOMCAT_HOME"
cd $TOMCAT_HOME/bin
./shutdown.sh
