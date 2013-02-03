git add -u 
git add .
git status
@echo off
set /P comment= "Comment:" %=%
@echo on
git commit -m "%comment%"
git push