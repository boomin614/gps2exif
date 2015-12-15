echo off
rem ** ------ 必須オプション ここから ------------------------------------------------------------**

set TOOL_DIR=%~dp0
rem cd /d %TOOL_DIR%

rem 画像ファイルを格納しているディレクトリ
set ANT_DIR=D:\programs\pleiades\apache-ant-1.9.6\bin
set JAVA_HOME=C:\Program Files\Java\jdk1.8.0_60\bin
set path=%path%;%ANT_DIR%;%JAVA_HOME%

rem ** ------ 必須オプション ここまで ------------------------------------------------------------**

ant -buildfile %TOOL_DIR%\makeJAR.xml
pause

