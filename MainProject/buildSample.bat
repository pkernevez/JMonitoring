cd ..\core_monitoring
call maven -o clean jar:install 

cd ..\sample_core
call maven -o clean jar:install 

cd ..\sample_aspect
call maven -o clean jar:install 

cd ..\sample_core_not_weaved
call maven -o clean jar:install 

cd ..\sample_webapp
call maven -o clean war copy
cd ..\MainProject