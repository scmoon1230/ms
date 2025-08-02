@echo off

set LANG=UTF-8
set APP_NAME=finance_job
set RUN_CLASS=kr.co.ucp.JobCommRun
SET JAVA_HOME=D:/cubeapps/pgms/openjdk11

SET APP_HOME=%cd%
SET LIB_HOME=%APP_HOME%\lib
SET CLASSPATH=%APP_HOME%\src-conf
SET JAVA_OPTS="-D%APP_NAME% -Xms128m -Xmx128m"
TITLE %APP_NAME%

CD %LIB_HOME%
set progpath=%CLASSPATH%
SET count=0
for %%i in (*) do (
	echo %%i
	call set progpath=%%progpath%%;%LIB_HOME%\%%i
  set /a count+=1
)
echo lib jar count:%count%
SET CLASSPATH=%progpath%
echo classpath:%CLASSPATH%
CD 	%APP_HOME%

echo =======================================================
echo  *  Process Running! %APP_HOME%
echo =======================================================

%JAVA_HOME%\bin\java %JAVA_OPTS% -classpath %CLASSPATH% %RUN_CLASS%
