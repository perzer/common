#!/bin/sh
# set path 
ANT_HOME=/usr/py/ant
SVN_HOME=/usr/local/svn
TOMCAT_HOME=/home/www/apache-tomcat-6.0.29

# get svn check url
echo -n "enter svn check url: "
read SVN_CHECK_URL

# get source code,build war
$SVN_HOME/bin/svn co $SVN_CHECK_URL newversion
cp ./build.properties ./newversion/
cd ./newversion
$ANT_HOME/bin/ant war

# stop tomcat,remove old_version
$TOMCAT_HOME/bin/catalina.sh stop
rm -rf $TOMCAT_HOME/webapps/czcenmss.war
rm -rf $TOMCAT_HOME/webapps/czcenmss/*

cp ./war/czcenmss.war $TOMCAT_HOME/webapps/czcenmss
rm -rf ../newversion
cd  $TOMCAT_HOME/webapps/czcenmss
jar xvf czcenmss.war
rm -rf $TOMCAT_HOME/webapps/czcenmss/czcenmss.war
$TOMCAT_HOME/bin/startup.sh
