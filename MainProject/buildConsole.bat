cd ..\core_monitoring
call maven -o clean jar:install &
cd ..\administration_console &
call maven -o clean war copy
cd ..\MainProject