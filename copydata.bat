@echo off

rem copy脚本

rem 团队编号
if not "%1"=="" set base=%1%

if not "%2"=="" set host=%2%

if not "%3"=="" set port=%3%

set psql=psql -h %host% -p %port% -d %base% -U postgres

if not "%4"=="" set PGPASSWORD=%4%

if not "%5"=="" set fileUrl=%5%

if not "%6"=="" set tableName=%6%

rem 执行copy语句
%psql% -c "copy tableName from E'%fileUrl%' USING delimiters ',';"


echo "data copy end"