@echo off
echo *****************************************************
echo ***   WANDORA - THE KNOWLEDGE MANAGEMENT STUDIO   ***
echo ***     Copyright (C) 2004-2015 Wandora Team      ***
echo ***              http://wandora.org               *** 
echo *****************************************************
echo Xms3000m Xmx3500m

set WANDORALIB=

call SetClasspath.bat
call SetR.bat
call SetProcessing.bat
call SetTesseract.bat

cd ..\build

"java" -Xms3000m -Xmx3500m "-Djava.library.path=%WANDORALIB%" -classpath "%WANDORACLASSES%" org.wandora.application.Wandora %*

