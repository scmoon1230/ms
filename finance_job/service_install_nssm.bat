@echo off

set SERVICE_NAME=finance_job

set NSSM=D:/cubeapps/pgms/nssm_window_service/nssm64.exe

set SERVICE_PGM=%cd%/start_%SERVICE_NAME%.bat

%NSSM% install %SERVICE_NAME% %SERVICE_PGM% Start SERVICE_AUTO_START