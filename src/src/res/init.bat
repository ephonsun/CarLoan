@echo off

REM Creazione database
set PATH=%PATH%;C:\Programmi\MySQL\MySQL Server 5.6\bin
mysql -u root -p -e "Create database if not exists carloandb;"
mysql -u root -p carloandb < %~dp0\carloandb.sql
