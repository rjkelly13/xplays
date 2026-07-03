@echo off

javac -cp ".;libs/*" *.java

if errorlevel 1 (
    echo.
    echo Compile failed.
    exit /b
)

java -cp ".;libs/*" Main %*