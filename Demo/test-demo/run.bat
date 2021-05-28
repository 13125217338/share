title test-demo
@echo off
chcp 65001
cd %~dp0target\classes
:start
java -Dfile.encoding=UTF-8 -Xms256m -Xmx1024m -classpath .;../../mylib/http.jar -agentlib:jdwp=transport=dt_socket,address=9959,server=y,suspend=n org.ice.demo.start.Application
@echo.
@echo ********************************************************************************************************
timeout /t 15 /nobreak
goto:start