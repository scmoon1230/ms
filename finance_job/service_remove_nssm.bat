@echo off

set SERVICE_NAME=finance_job

set NSSM=D:/cubeapps/pgms/nssm_window_service/nssm64.exe

net stop %SERVICE_NAME%
%NSSM% remove %SERVICE_NAME%