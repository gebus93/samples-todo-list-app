@if "%DEBUG%" == "" @echo off
if "%OS%"=="Windows_NT" setlocal

set scriptDir=%~dp0
if "%scriptDir%" == "" set scriptDir=.\

cd "%scriptDir%"
docker-compose down