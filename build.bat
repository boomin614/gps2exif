echo off
rem ** ------ �K�{�I�v�V���� �������� ------------------------------------------------------------**

set TOOL_DIR=%~dp0
rem cd /d %TOOL_DIR%

rem �摜�t�@�C�����i�[���Ă���f�B���N�g��
set ANT_DIR=D:\programs\pleiades\apache-ant-1.9.6\bin
set JAVA_HOME=C:\Program Files\Java\jdk1.8.0_60\bin
set path=%path%;%ANT_DIR%;%JAVA_HOME%

rem ** ------ �K�{�I�v�V���� �����܂� ------------------------------------------------------------**

ant -buildfile %TOOL_DIR%\makeJAR.xml
pause

