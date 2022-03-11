@if "%DEBUG%" == "" @echo off
if "%OS%"=="Windows_NT" setlocal

set scriptDir=%~dp0
set projectDir=%~dp0..\
if "%scriptDir%" == "" set scriptDir=.\
if "%projectDir%" == "" set projectDir=..\

set image=fake-registry.thinkandcode.pl/samples/todo-app-sample:latest
cd "%projectDir%"
java --version
call gradlew.bat -s build -x check
docker build -f "%projectDir%\docker\Dockerfile" -t "%image%" "%projectDir%\build\libs"

cd "%scriptDir%"
docker-compose down
docker-compose up -d --force-recreate