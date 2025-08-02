@echo off

set APP_NAME=finance_job
set APP_DESC=[%APP_NAME%]

title %APP_NAME%

taskkill /fi "WindowTitle eq %APP_NAME%"

