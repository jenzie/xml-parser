@echo off
setlocal enabledelayedexpansion
set S=temp.cpp_23423.xml
set I=0
set L=-1
:l
if "!S:~%I%,1!"=="" goto ld
if "!S:~%I%,4!"==".cpp" set L=%I%
set /a I+=1
goto l
:ld
set four=4 
set /a filenameLength =%L%+%four% 
set filename=!S:~0,%filenameLength%!
echo %filename%
pause